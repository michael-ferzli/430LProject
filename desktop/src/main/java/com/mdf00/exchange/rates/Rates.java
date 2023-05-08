package com.mdf00.exchange.rates;

import com.mdf00.exchange.Authentication;
import com.mdf00.exchange.api.ExchangeService;
import com.mdf00.exchange.api.model.ExchangeRates;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mdf00.exchange.api.model.Transaction;

public class Rates {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    public TextField lbpTextField;
    public TextField usdTextField;
    public ToggleGroup transactionType;
    public TextField calculationTextField;
    public ToggleGroup calculationType;
    public Label calculationResult;

    public void initialize() {
        fetchRates();
    }

    private void fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(new Callback<ExchangeRates>() {
            @Override
            public void onResponse(Call<ExchangeRates> call,
                                   Response<ExchangeRates> response) {
                ExchangeRates exchangeRates = response.body();
                Platform.runLater(() -> {
                    buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                    sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                });
            }

            @Override
            public void onFailure(Call<ExchangeRates> call, Throwable throwable) {
                System.out.println("API call failed: " + throwable.getMessage());

            }
        });
    }

    public void addTransaction(ActionEvent actionEvent) {
        if (!usdTextField.getText().equals("") && !lbpTextField.getText().equals("") && Float.parseFloat(usdTextField.getText()) > 0
                && Float.parseFloat(lbpTextField.getText()) > 0) {
            Transaction transaction = new Transaction(
                    Float.parseFloat(usdTextField.getText()),
                    Float.parseFloat(lbpTextField.getText()),
                    ((RadioButton)
                            transactionType.getSelectedToggle()).getText().equals("Sell USD")
            );

            String userToken = Authentication.getInstance().getToken();
            String authHeader = userToken != null ? "Bearer " + userToken : null;
            ExchangeService.exchangeApi().addTransaction(transaction, authHeader).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    fetchRates();
                    Platform.runLater(() -> {
                        usdTextField.setText("");
                        lbpTextField.setText("");
                    });
                }

                @Override
                public void onFailure(Call<Object> call, Throwable throwable) {
                    System.out.println("API call failed: " + throwable.getMessage());

                }
            });


        }
    }

    public void calculate() {
        if (!calculationTextField.getText().equals("") && Float.parseFloat(calculationTextField.getText()) > 0) {
            boolean calculationT = ((RadioButton) calculationType.getSelectedToggle()).getText().equals("To USD");
            float amount = Float.parseFloat(calculationTextField.getText());

            if (calculationT){
                if (!buyUsdRateLabel.equals("NO ENTRIES YET") && !buyUsdRateLabel.equals("Buy Rate")) {
                    calculationResult.setText("Result: "+String.valueOf(amount / Float.parseFloat(buyUsdRateLabel.getText()))+"USD");
                }
            }else{
                if (!sellUsdRateLabel.equals("NO ENTRIES YET") && !sellUsdRateLabel.equals("Sell Rate")) {
                    calculationResult.setText("Result: "+String.valueOf(amount * Float.parseFloat(sellUsdRateLabel.getText()))+"LBP");
                }
            }
        }
    }
}

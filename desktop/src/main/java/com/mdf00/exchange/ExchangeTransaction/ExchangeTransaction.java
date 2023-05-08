package com.mdf00.exchange.ExchangeTransaction;

import com.mdf00.exchange.Authentication;
import com.mdf00.exchange.api.Exchange;
import com.mdf00.exchange.api.ExchangeService;
import com.mdf00.exchange.api.model.Offer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExchangeTransaction {

    @FXML private TextField usdAmountField;
    @FXML private TextField lbpAmountField;

    @FXML private TableView<Offer> offersTableView;
    @FXML private TableColumn<Offer, Float> usdAmountColumn;
    @FXML private TableColumn<Offer, Float> lbpAmountColumn;
    @FXML private TableColumn<Offer, Integer> userIdColumn;
    @FXML private TableColumn<Offer, Boolean> TypeColumn;
    @FXML private TableColumn<Offer, Void> acceptOfferColumn;




    @FXML private ComboBox<String> transactionTypeComboBox;
    private Exchange exchangeApi;
    @FXML
    protected void handlePostTransaction(ActionEvent event) {
        String usdAmount = usdAmountField.getText();
        String lbpAmount = lbpAmountField.getText();
        String transactionType = transactionTypeComboBox.getSelectionModel().getSelectedItem();
        // handle the transaction
        postOffer(transactionType,usdAmount,lbpAmount);


    }



    public void initialize() {
        usdAmountColumn.prefWidthProperty().bind(offersTableView.widthProperty().multiply(0.1));
        lbpAmountColumn.prefWidthProperty().bind(offersTableView.widthProperty().multiply(0.2));
        TypeColumn.prefWidthProperty().bind(offersTableView.widthProperty().multiply(0.2));
        userIdColumn.prefWidthProperty().bind(offersTableView.widthProperty().multiply(0.2));
        acceptOfferColumn.prefWidthProperty().bind(offersTableView.widthProperty().multiply(0.1));
        transactionTypeComboBox.getItems().setAll("USD To LBP", "LBP To USD");

        // Initialize table columns
        usdAmountColumn.setCellValueFactory(new PropertyValueFactory<>("usdAmount"));
        lbpAmountColumn.setCellValueFactory(new PropertyValueFactory<>("lbpAmount"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("usdToLbp"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        acceptOfferColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Accept");

            {
                btn.setOnAction(event -> {
                    Offer offer = getTableView().getItems().get(getIndex());
                    handleAcceptOffer(offer);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        // Load initial data
        loadOffers();
    }

    private void handleAcceptOffer(Offer offer) {
        // TODO: implement your offer acceptance logic here
        Map<String, Integer> offerIdMap = new HashMap<>();
        offerIdMap.put("offer_id", offer.getOffer_id());

        ExchangeService.exchangeApi().acceptOffer(offerIdMap,"Bearer " + Authentication.getInstance().getToken())
                .enqueue(new Callback<Offer>() {
                    @Override
                    public void onResponse(Call<Offer> call, Response<Offer> response) {
                        if(response.isSuccessful()){
                            loadOffers();

                        } else if(response.code() == 403 ){
                            Platform.runLater(()-> {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("You cannot accept your own offer.");
                                alert.show();
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<Offer> call, Throwable throwable) {
                        System.out.println("API call failed: " + throwable.getMessage());
                    }
                });
    }

    public void postOffer(String Type, String usd, String lbp) {
        // Get the transaction type from the ComboBox


        // Create a new Offer instance
        Offer offer = new Offer();
        offer.setUsdAmount(Float.parseFloat(usd));
        offer.setLbpAmount(Float.parseFloat(lbp));
        offer.setUsdToLbp(Type.equals("USD To LBP"));

        // Use the API to post the offer
        ExchangeService.exchangeApi().postOffer(offer, "Bearer " + Authentication.getInstance().getToken())
                .enqueue(new Callback<Offer>() {
                    @Override
                    public void onResponse(Call<Offer> call, Response<Offer> response) {
                        if (response.isSuccessful()) {
                            loadOffers();
                        }
                    }

                    @Override
                    public void onFailure(Call<Offer> call, Throwable throwable) {
                        System.out.println("API call failed: " + throwable.getMessage());
                    }
                });
    }

    private void loadOffers() {
        // TODO: implement your API calls here
        ExchangeService.exchangeApi().getOffers().enqueue(new Callback<List<Offer>>() {

            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                if (response.isSuccessful()) {
                    List<Offer> offers = response.body();

                    Platform.runLater(() -> {
                        offersTableView.getItems().setAll(
                                offers.stream()
                                        .filter(offer -> !offer.isAccepted() )
                                        .collect(Collectors.toList())
                        );
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Offer>> call, Throwable throwable) {
                System.out.println("API call failed: " + throwable.getMessage());
            }
        });

    }

}

package com.mdf00.exchange.Currency_game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.*;

public class Currency_game {
    @FXML
    private Label questionLabel;

    @FXML
    private Button option1Button;

    @FXML
    private Button option2Button;

    @FXML
    private Button option3Button;

    @FXML
    private Button option4Button;

    @FXML
    private Label scoreLabel;

    private Map<String, String> countriesAndCurrencies;
    private int score;
    private String correctAnswer;

    public void initialize() {
        countriesAndCurrencies = new LinkedHashMap<>();
        countriesAndCurrencies.put("United States", "USD");
        countriesAndCurrencies.put("Canada", "CAD");
        countriesAndCurrencies.put("Japan", "JPY");
        countriesAndCurrencies.put("United Kingdom", "GBP");
        countriesAndCurrencies.put("Mexico", "MXN");
        countriesAndCurrencies.put("Australia", "AUD");
        countriesAndCurrencies.put("Russia", "RUB");
        countriesAndCurrencies.put("China", "CNY");
        countriesAndCurrencies.put("Argentina", "ARS");
        countriesAndCurrencies.put("Brazil", "BRL");
        countriesAndCurrencies.put("Switzerland", "CHF");
        countriesAndCurrencies.put("Chile", "CLP");
        countriesAndCurrencies.put("Czech Republic", "CZK");
        countriesAndCurrencies.put("Denmark", "DKK");
        countriesAndCurrencies.put("Egypt", "EGP");
        countriesAndCurrencies.put("Hong Kong", "HKD");
        countriesAndCurrencies.put("Hungary", "HUF");
        countriesAndCurrencies.put("India", "INR");
        countriesAndCurrencies.put("Indonesia", "IDR");
        countriesAndCurrencies.put("Kenya", "KES");
        countriesAndCurrencies.put("South Korea", "KRW");
        countriesAndCurrencies.put("Malaysia", "MYR");
        countriesAndCurrencies.put("New Zealand", "NZD");
        countriesAndCurrencies.put("Philippines", "PHP");
        countriesAndCurrencies.put("Singapore", "SGD");
        countriesAndCurrencies.put("South Africa", "ZAR");
        countriesAndCurrencies.put("Thailand", "THB");


        score = 0;
        nextQuestion();
    }

    private void nextQuestion() {
        List<String> countries = new ArrayList<>(countriesAndCurrencies.keySet());
        Collections.shuffle(countries);
        String country = countries.get(0);
        correctAnswer = countriesAndCurrencies.get(country);

        questionLabel.setText("What is the currency of " + country + "?");

        List<String> options = new ArrayList<>(countriesAndCurrencies.values());
        Collections.shuffle(options);
        options.remove(correctAnswer);
        options = options.subList(0, 3);
        options.add(correctAnswer);
        Collections.shuffle(options);

        option1Button.setText(options.get(0));
        option2Button.setText(options.get(1));
        option3Button.setText(options.get(2));
        option4Button.setText(options.get(3));
    }

    @FXML
    void handleOptionClicked(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String selectedOption = clickedButton.getText();

        if (selectedOption.equals(correctAnswer)) {
            score++;
            scoreLabel.setText(String.valueOf(score));
        }

        nextQuestion();
    }

}
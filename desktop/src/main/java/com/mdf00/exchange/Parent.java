package com.mdf00.exchange;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class Parent implements Initializable, OnPageCompleteListener {
    public BorderPane borderPane;
    public Button transactionButton;
    public Button loginButton;
    public Button registerButton;
    public Button logoutButton;
    public Button StatisticsButton;
    public Button offersButton;
    public Button forumButton;
    public Button gameButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateNavigation();
    }

    public void ratesSelected() {
        swapContent(Section.RATES);
    }

    public void transactionsSelected() {
        swapContent(Section.TRANSACTIONS);
    }

    public void loginSelected() {
        swapContent(Section.LOGIN);
    }

    public void registerSelected() {
        swapContent(Section.REGISTER);
    }

    public void statisticsSelected() {swapContent(Section.STATISTICS);}

    public void offersSelected() {swapContent(Section.OFFERS);}

    public void forumSelected() {swapContent(Section.FORUM);}

    public void gameSelected() {swapContent(Section.MINIGAME);}
    public void logoutSelected() {
        Authentication.getInstance().deleteToken();
        swapContent(Section.RATES);
    }

    private void swapContent(Section section) {
        try {
            URL url = getClass().getResource(section.getResource());
            FXMLLoader loader = new FXMLLoader(url);
            borderPane.setCenter(loader.load());
            if (section.doesComplete()) {
                ((PageCompleter)
                        loader.getController()).setOnPageCompleteListener(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateNavigation();
    }

    @Override
    public void onPageCompleted() {
        swapContent(Section.RATES);
    }

    private void updateNavigation() {
        boolean authenticated = Authentication.getInstance().getToken() != null;
        transactionButton.setManaged(authenticated);
        transactionButton.setVisible(authenticated);
        gameButton.setManaged(authenticated);
        gameButton.setVisible(authenticated);
        forumButton.setManaged(authenticated);
        forumButton.setVisible(authenticated);
        offersButton.setManaged(authenticated);
        offersButton.setVisible(authenticated);
        loginButton.setManaged(!authenticated);
        loginButton.setVisible(!authenticated);
        registerButton.setManaged(!authenticated);
        registerButton.setVisible(!authenticated);
        logoutButton.setManaged(authenticated);
        logoutButton.setVisible(authenticated);
    }
    private enum Section {
        RATES,
        TRANSACTIONS,
        LOGIN,
        STATISTICS,
        OFFERS,
        FORUM,
        MINIGAME,
        REGISTER;

        public String getResource() {
            return switch (this) {
                case RATES -> "/rates/rates.fxml";
                case TRANSACTIONS -> "/com/mdf00/exchange/transactions/transactions.fxml";
                case LOGIN -> "/com/mdf00/exchange/login/login.fxml";
                case REGISTER -> "/com/mdf00/exchange/register/register.fxml";
                case STATISTICS -> "/com/mdf00/exchange/Statistics/Statistics.fxml"; // Add the new resource
                case OFFERS -> "/com/mdf00/exchange/UserExchangeTransaction/ExchangeTransaction.fxml";
                case FORUM -> "/com/mdf00/exchange/Forum/Forum.fxml";
                case MINIGAME -> "/com/mdf00/exchange/Minigame/currency_game.fxml";
                default -> null;
            };
        }
        public boolean doesComplete() {
            return switch (this) {
                case LOGIN, REGISTER -> true;
                default -> false;
            };
        }

    }
}
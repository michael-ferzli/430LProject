module com.mdf00.exchange {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires jjwt.api;
    requires gson;
    requires retrofit2.converter.gson;
    requires java.prefs;
    opens com.mdf00.exchange.transactions to javafx.fxml;
    opens com.mdf00.exchange to javafx.fxml;
    opens com.mdf00.exchange.api.model to javafx.base, gson;
    opens com.mdf00.exchange.login to javafx.fxml;
    opens com.mdf00.exchange.register to javafx.fxml;
    opens com.mdf00.exchange.Statistics to javafx.fxml;
    opens com.mdf00.exchange.ExchangeTransaction to javafx.fxml;
    opens com.mdf00.exchange.Forum to javafx.fxml;
    opens com.mdf00.exchange.Currency_game to javafx.fxml;
    exports com.mdf00.exchange;
    opens com.mdf00.exchange.api to gson;
    exports com.mdf00.exchange.rates;
    opens com.mdf00.exchange.rates to javafx.fxml;
}
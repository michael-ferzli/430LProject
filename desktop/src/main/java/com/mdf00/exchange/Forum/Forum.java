package com.mdf00.exchange.Forum;

import com.mdf00.exchange.Authentication;
import com.mdf00.exchange.api.Exchange;
import com.mdf00.exchange.api.ExchangeService;
import com.mdf00.exchange.api.model.Message;
import com.mdf00.exchange.api.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Forum {
    @FXML
    private TableView<Message> messageTable;

    @FXML
    private TableColumn<Message, String> usernameColumn;

    @FXML
    private TableColumn<Message, String> messageColumn;

    @FXML
    private TableColumn<Message, String> dateColumn;

    @FXML
    private TextArea messageInput;

    private Exchange exchangeApi;
    private static final List<String> BAD_WORDS = Arrays.asList("fuck", "bitch", "sex","kill","death","ousama","binlanden","isis")
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList());

    private final ObservableList<Message> messageData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("added_date"));

        messageColumn.prefWidthProperty().bind(messageTable.widthProperty().multiply(0.7));
        dateColumn.prefWidthProperty().bind(messageTable.widthProperty().multiply(0.2));
        usernameColumn.prefWidthProperty().bind(messageTable.widthProperty().multiply(0.1));

        messageTable.setItems(messageData);

        exchangeApi = ExchangeService.exchangeApi();
        loadMessages();
    }

    public void sendMessage() {
        String messageText = messageInput.getText().trim();

        if (containsBadWord(messageText)) {
            showAlert("Inappropriate Content", "Please do not use inappropriate words in your messages.");
            return;
        }
        if (!messageText.isEmpty()) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("message", messageText);

            String authorization = "Bearer " + Authentication.getInstance().getToken();

            exchangeApi.postMessage(messageMap, authorization).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        Platform.runLater(() -> {
                            messageInput.clear();
                            loadMessages();
                        });
                    } else {
                        System.out.println("Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable throwable) {
                    System.out.println("API call failed: " + throwable.getMessage());
                }
            });
        }
    }

    private boolean containsBadWord(String message) {
        String[] words = message.toLowerCase().split("\\s+");
        for (String word : words) {
            if (BAD_WORDS.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }



    private void loadMessages(){
        ExchangeService.exchangeApi().getMessages().enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if(response.isSuccessful()){
                    messageData.clear();
                    for(Message message : response.body()){
                        loadUserAndSetUsername(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable throwable) {
                System.out.println("Call failed");
                System.out.println("Error message: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
    private void loadUserAndSetUsername(Message message){
        ExchangeService.exchangeApi().getUserFromId(message.getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    message.setUsername(user.getUsername());
                    messageData.add(message);
                } else {
                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                System.out.println("API call failed: " + throwable.getMessage());
            }
        });
    }

}

package com.mdf00.exchange.Statistics;

import com.mdf00.exchange.api.Exchange;
import com.mdf00.exchange.api.ExchangeService;
import com.mdf00.exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Statistics implements Initializable {

    @FXML
    private Label dailylbp_to_usd;
    @FXML
    private Label weeklylbp_to_usd;
    @FXML
    private Label monthlylbp_to_usd;
    @FXML
    private Label yearlylbp_to_usd;

    ////////////////////////////////////////////////////
    @FXML
    private Label dailyusd_to_lbp;
    @FXML
    private Label weeklyusd_to_lbp;

    @FXML
    private Label monthlyusd_to_lbp;
    @FXML
    private Label yearlyusd_to_lbp;

    //////////////////////////////////////////
    @FXML
    private Label dailylbp_to_usd_std;
    @FXML
    private Label weeklylbp_to_usd_std;
    @FXML
    private Label monthlylbp_to_usd_std;
    @FXML
    private Label yearlylbp_to_usd_std;

    ////////////////////////////////////////////////////
    @FXML
    private Label dailyusd_to_lbp_std;
    @FXML
    private Label weeklyusd_to_lbp_std;
    @FXML
    private Label monthlyusd_to_lbp_std;
    @FXML
    private Label yearlyusd_to_lbp_std;

    //////////////////////////////////////////
    @FXML
    private Label dailylbp_to_usd_max;
    @FXML
    private Label weeklylbp_to_usd_max;
    @FXML
    private Label monthlylbp_to_usd_max;
    @FXML
    private Label yearlylbp_to_usd_max;

    ////////////////////////////////////////////////////
    @FXML
    private Label dailyusd_to_lbp_max;
    @FXML
    private Label weeklyusd_to_lbp_max;
    @FXML
    private Label monthlyusd_to_lbp_max;
    @FXML
    private Label yearlyusd_to_lbp_max;

    //////////////////////////////////////////
    @FXML
    private Label dailylbp_to_usd_min;
    @FXML
    private Label weeklylbp_to_usd_min;
    @FXML
    private Label monthlylbp_to_usd_min;
    @FXML
    private Label yearlylbp_to_usd_min;

    ////////////////////////////////////////////////////
    @FXML
    private Label dailyusd_to_lbp_min;
    @FXML
    private Label weeklyusd_to_lbp_min;
    @FXML
    private Label monthlyusd_to_lbp_min;
    @FXML
    private Label yearlyusd_to_lbp_min;
    //////////////////////////////////////////

    @FXML
    private ComboBox<String> statsSelector;
    @FXML
    private LineChart<String, Number> transactionChart;

    @FXML
    private VBox USD_To_LBP_STD;
    @FXML
    private VBox LBP_To_USD_STD;
    @FXML
    private VBox USD_To_LBP_MIN;
    @FXML
    private VBox LBP_To_USD_MIN;
    @FXML
    private VBox USD_To_LBP_MAX;
    @FXML
    private VBox LBP_To_USD_MAX;
    @FXML
    private VBox USD_To_LBP;
    @FXML
    private VBox LBP_To_USD;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;


    @FXML
    private Button applyDateRangeButton;


    private List<Transaction> transactions;
    private VBox prev_vbox = null;
    private VBox curr_vbox = null;
    private Exchange exchangeApi;
    private XYChart XYChart;
    @FXML
    private Button resetButton;

    public void resetGraph(ActionEvent actionEvent) {
        // Remove all series from the chart
        transactionChart.getData().clear();

        // Create a TreeMap with a comparator that sorts by keys in ascending order
        TreeMap<Date, Float> sortedMap = new TreeMap<>(new Comparator<Date>() {
            @Override
            public int compare(Date date1, Date date2) {
                return date1.compareTo(date2);
            }
        });
        sortedMap.putAll(getAverageDailyRate(transactions));

        // Display the original data on the graph
        displayingraph(sortedMap);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionChart.setCreateSymbols(false);
        exchangeApi = ExchangeService.exchangeApi();
        statsSelector.getItems().setAll("USD To LBP STD", "LBP To USD STD", "USD To LBP MIN", "LBP To USD MIN", "USD To LBP MAX", "LBP To USD MAX", "USD To LBP", "LBP To USD");
        List<VBox> vboxList = Arrays.asList(LBP_To_USD,USD_To_LBP,LBP_To_USD_MAX,USD_To_LBP_MAX,LBP_To_USD_MIN,USD_To_LBP_MIN,LBP_To_USD_STD,USD_To_LBP_STD);

        vboxList.forEach(vbox -> vbox.setVisible(false));
        statsSelector.setOnAction(event -> {
            String selectedItem = statsSelector.getValue();

            if (curr_vbox != null) {
                curr_vbox.setVisible(false); // Set the visibility of the previous VBox to false
            }

            switch (selectedItem) {
                case "USD To LBP STD":
                    USD_To_LBP_STD.setVisible(true);
                    curr_vbox = USD_To_LBP_STD;
                    break;
                case "USD To LBP MIN":
                    USD_To_LBP_MIN.setVisible(true);
                    curr_vbox = USD_To_LBP_MIN;
                    break;
                case "USD To LBP MAX":
                    USD_To_LBP_MAX.setVisible(true);
                    curr_vbox = USD_To_LBP_MAX;
                    break;
                case "USD To LBP":
                    USD_To_LBP.setVisible(true);
                    curr_vbox = USD_To_LBP;
                    break;
                case "LBP To USD STD":
                    LBP_To_USD_STD.setVisible(true);
                    curr_vbox = LBP_To_USD_STD;
                    break;
                case "LBP To USD MIN":
                    LBP_To_USD_MIN.setVisible(true);
                    curr_vbox = LBP_To_USD_MIN;
                    break;
                case "LBP To USD MAX":
                    LBP_To_USD_MAX.setVisible(true);
                    curr_vbox = LBP_To_USD_MAX;
                    break;
                case "LBP To USD":
                    LBP_To_USD.setVisible(true);
                    curr_vbox = LBP_To_USD;
                    break;
            }
        });



        fetchDataFromBackend();
        fetchAllTransactions();

    }
    public Map<Date, Float> getAverageDailyRate(List<Transaction> transactions) {
        Map<String, float[]> dailyRateInfo = new HashMap<>();

        for (Transaction transaction : transactions) {
            String date = (transaction.getAddedDate()).substring(0,10);

            float rate = transaction.getUsdAmount() / transaction.getLbpAmount();

            if (!transaction.getUsdToLbp()) {
                rate = 1 / rate;
            }

            float[] rateInfo = dailyRateInfo.getOrDefault(date, new float[]{0, 0});
            rateInfo[0] += rate;
            rateInfo[1] += 1;
            dailyRateInfo.put(date, rateInfo);
        }

        // Compute the average rate for each day
        Map<Date, Float> averageDailyRate = new HashMap<>();
        for (Map.Entry<String, float[]> entry : dailyRateInfo.entrySet()) {
            String date = entry.getKey();
            float totalRate = entry.getValue()[0];
            float count = entry.getValue()[1];
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date Date = null;
            try {
                Date = dateFormat.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            averageDailyRate.put(Date, totalRate / count);
        }

        return averageDailyRate;
    }

    @FXML
    private void applyDateRange() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        System.out.println("dwdewddw");
        System.out.println(startDate);
        System.out.println(endDate);
        if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
            System.out.println("deeeeeee");
            List<Transaction> filteredTransactions = filterTransactionsByDateRange(transactions, startDate, endDate);
            TreeMap<Date, Float> sortedMap = createSortedMapFromTransactions(filteredTransactions);
            transactionChart.getData().clear();
            displayingraph(sortedMap);
        }
    }

    private List<Transaction> filterTransactionsByDateRange(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Transaction transaction : transactions) {
            try {
                Date transactionDate = sdf.parse(transaction.getAddedDate().substring(0, 10));
                LocalDate localTransactionDate = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (!localTransactionDate.isBefore(startDate) && !localTransactionDate.isAfter(endDate)) {
                    filteredTransactions.add(transaction);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return filteredTransactions;
    }

    private TreeMap<Date, Float> createSortedMapFromTransactions(List<Transaction> transactions) {
        Map<Date, Float> averageDailyRate = getAverageDailyRate(transactions);
        TreeMap<Date, Float> sortedMap = new TreeMap<>(new Comparator<Date>() {
            @Override
            public int compare(Date date1, Date date2) {
                return date1.compareTo(date2);
            }
        });
        sortedMap.putAll(averageDailyRate);
        return sortedMap;
    }


    private void displayingraph(TreeMap<Date,Float> data){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rate by Day");

        for (Map.Entry<Date, Float> entry : data.entrySet()){
            String date = sdf.format(entry.getKey());

            // Add data to series
            series.getData().add(new XYChart.Data<>(date, entry.getValue()));
        }

        // Add series to LineChart
        transactionChart.getData().add(series);
        series.getNode().getStyleClass().add("custom-series");

    }


    private void fetchAllTransactions() {
        ExchangeService.exchangeApi().getAllTransactions().enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful()) {
                    transactions = response.body();
                    Map<Date,Float> test = getAverageDailyRate(transactions);
                    // Create a TreeMap with a comparator that sorts by keys in ascending order
                    TreeMap<Date, Float> sortedMap = new TreeMap<>(new Comparator<Date>() {
                        @Override
                        public int compare(Date date1, Date date2) {
                            return date1.compareTo(date2);
                        }
                    });
                    sortedMap.putAll(test);

                    Platform.runLater(() -> {
                        displayingraph(sortedMap);
                    });

                } else {
                    System.out.println("Error fetching transactions: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable throwable) {
                System.out.println("API call failed: " + throwable.getMessage());
            }
        });
    }


    private void fetchDataFromBackend() {
        ExchangeService.exchangeApi().getStatistics().enqueue(new Callback<com.mdf00.exchange.api.model.Statistics>() {
            @Override
            public void onResponse(Call<com.mdf00.exchange.api.model.Statistics> call, Response<com.mdf00.exchange.api.model.Statistics> response) {
                if (response.isSuccessful()){
                    com.mdf00.exchange.api.model.Statistics stat = response.body();

                    Platform.runLater(() -> {
                        //USD to LBP
                        dailyusd_to_lbp.setText("Daily Change: " + stat.usdToLbp.day);
                        weeklyusd_to_lbp.setText("Weekly Change: " + stat.usdToLbp.week);
                        monthlyusd_to_lbp.setText("Monthly Change: " + stat.usdToLbp.month);
                        yearlyusd_to_lbp.setText("Yearly Change: " + stat.usdToLbp.year);
                        //LBP to USD
                        dailylbp_to_usd.setText("Daily Change: " + stat.lbpToUsd.day);
                        weeklylbp_to_usd.setText("Weekly Change: " + stat.lbpToUsd.week);
                        monthlylbp_to_usd.setText("Monthly Change: " + stat.lbpToUsd.month);
                        yearlylbp_to_usd.setText("Yearly Change: " + stat.lbpToUsd.year);
                        //USD to LBP Standard Deviation
                        dailyusd_to_lbp_std.setText("Daily Change: " + stat.usdToLbpStd.day);
                        weeklyusd_to_lbp_std.setText("Weekly Change: " + stat.usdToLbpStd.week);
                        monthlyusd_to_lbp_std.setText("Monthly Change: " + stat.usdToLbpStd.month);
                        yearlyusd_to_lbp_std.setText("Yearly Change: " + stat.usdToLbpStd.year);
                        //LBP to USD Standard Deviation
                        dailylbp_to_usd_std.setText("Daily Change: " + stat.lbpToUsdStd.day);
                        weeklylbp_to_usd_std.setText("Weekly Change: " + stat.lbpToUsdStd.week);
                        monthlylbp_to_usd_std.setText("Monthly Change: " + stat.lbpToUsdStd.month);
                        yearlylbp_to_usd_std.setText("Yearly Change: " + stat.lbpToUsdStd.year);
                        //USD to LBP Maximum
                        dailyusd_to_lbp_max.setText("Daily Change: " + stat.usdToLbpMax.day);
                        weeklyusd_to_lbp_max.setText("Weekly Change: " + stat.usdToLbpMax.week);
                        monthlyusd_to_lbp_max.setText("Monthly Change: " + stat.usdToLbpMax.month);
                        yearlyusd_to_lbp_max.setText("Yearly Change: " + stat.usdToLbpMax.year);
                        //LBP to USD Maximum
                        dailylbp_to_usd_max.setText("Daily Change: " + stat.lbpToUsdMax.day);
                        weeklylbp_to_usd_max.setText("Weekly Change: " + stat.lbpToUsdMax.week);
                        monthlylbp_to_usd_max.setText("Monthly Change: " + stat.lbpToUsdMax.month);
                        yearlylbp_to_usd_max.setText("Yearly Change: " + stat.lbpToUsdMax.year);
                        //USD to LBP Minimum
                        dailyusd_to_lbp_min.setText("Daily Change: " + stat.usdToLbpMin.day);
                        weeklyusd_to_lbp_min.setText("Weekly Change: " + stat.usdToLbpMin.week);
                        monthlyusd_to_lbp_min.setText("Monthly Change: " + stat.usdToLbpMin.month);
                        yearlyusd_to_lbp_min.setText("Yearly Change: " + stat.usdToLbpMin.year);
                        //LBP to USD
                        dailylbp_to_usd_min.setText("Daily Change: " + stat.lbpToUsdMin.day);
                        weeklylbp_to_usd_min.setText("Weekly Change: " + stat.lbpToUsdMin.week);
                        monthlylbp_to_usd_min.setText("Monthly Change: " + stat.lbpToUsdMin.month);
                        yearlylbp_to_usd_min.setText("Yearly Change: " + stat.lbpToUsdMin.year);
                    });
                }
            }
            @Override
            public void onFailure(Call<com.mdf00.exchange.api.model.Statistics> call, Throwable throwable) {
                System.out.println("API call failed: " + throwable.getMessage());

            }
        });
    }

}

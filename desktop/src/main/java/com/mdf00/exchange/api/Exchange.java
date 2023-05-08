package com.mdf00.exchange.api;

import com.mdf00.exchange.api.model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface Exchange {
    @POST("/user")
    Call<User> addUser(@Body User user);
    //Changed to authenticate instead of authentication to match my backend
    @POST("/authenticate")
    Call<Token> authenticate(@Body User user);
    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();
    @POST("/transaction")
    Call<Object> addTransaction(@Body Transaction transaction, @Header("Authorization") String authorization);
    @GET("/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization") String authorization);

    @GET("/statistics")
    Call<Statistics> getStatistics();

    @GET("/graph")
    Call<List<Transaction>> getAllTransactions();

    @POST("/acceptOffer")
    Call<Offer> acceptOffer(@Body Map<String, Integer> offerIdMap, @Header("Authorization") String authorization);

    @GET("/getOffers")
    Call<List<Offer>> getOffers();

    @POST("/postOffer")
    Call<Offer> postOffer(@Body Offer offer, @Header("Authorization") String authorization);

    @POST("/postMessage")
    Call<Message> postMessage(@Body Map<String, String> messageMap, @Header("Authorization") String authorization);

    @GET("/getMessages")
    Call<List<Message>> getMessages();
    @GET("/getUserFromId")
    Call<User> getUserFromId(@Query("user_id") int userId);

}

package com.mdf00.exchange.api.model;

import com.google.gson.annotations.SerializedName;
public class ExchangeRates {
    //Had to change from String to Object to match my backend because I return "NO ENTRIES YET" if no entries in last 3 days
    @SerializedName("usd_to_lbp")
    public Object usdToLbp;
    @SerializedName("lbp_to_usd")
    public Object lbpToUsd;
}
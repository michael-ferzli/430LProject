package com.mdf00.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Statistics {
    @SerializedName("usd_to_lbp")
    public StatRates usdToLbp;
    @SerializedName("lbp_to_usd")
    public StatRates lbpToUsd;
    @SerializedName("usd_to_lbp_std")
    public StatRates usdToLbpStd;
    @SerializedName("lbp_to_usd_std")
    public StatRates lbpToUsdStd;
    @SerializedName("usd_to_lbp_max")
    public StatRates usdToLbpMax;
    @SerializedName("lbp_to_usd_max")
    public StatRates lbpToUsdMax;
    @SerializedName("usd_to_lbp_min")
    public StatRates usdToLbpMin;
    @SerializedName("lbp_to_usd_min")
    public StatRates lbpToUsdMin;
}


package com.mdf00.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Offer {
    @SerializedName("id")
    private int offer_id;
    @SerializedName("usd_amount")
    private float usdAmount;

    @SerializedName("lbp_amount")
    private float lbpAmount;

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    @SerializedName("usd_to_lbp")
    private boolean usdToLbp;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("accepted")
    private boolean accepted;

    public float getUsdAmount() {
        return usdAmount;
    }

    @SerializedName("accepted_user_id")
    private int acceptedUserId;

    public boolean isUsdToLbp() {
        return usdToLbp;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public int getAcceptedUserId() {
        return acceptedUserId;
    }

    public void setUsdAmount(float usdAmount) {
        this.usdAmount = usdAmount;
    }

    public void setLbpAmount(float lbpAmount) {
        this.lbpAmount = lbpAmount;
    }

    public void setUsdToLbp(boolean usdToLbp) {
        this.usdToLbp = usdToLbp;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setAcceptedUserId(int acceptedUserId) {
        this.acceptedUserId = acceptedUserId;
    }

    public float getLbpAmount() {
        return lbpAmount;
    }
}

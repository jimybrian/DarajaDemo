package com.outreach.darajademo.api

import com.google.gson.annotations.SerializedName

data class STKPayload(
    @SerializedName("BusinessShortCode") val businessShortcode: String,
    @SerializedName("Password") val password : String,
    @SerializedName("Timestamp") val timestamp : String,
    @SerializedName("TransactionType") val TransactionType: String,
    @SerializedName("Amount") val Amount: String,
    @SerializedName("PartyA") val PartyA: String,
    @SerializedName("PartyB") val PartyB: String,
    @SerializedName("PhoneNumber") val PhoneNumber: String,
    @SerializedName("CallBackURL") val CallBackURL: String,
    @SerializedName("AccountReference") val AccountReference: String,
    @SerializedName("TransactionDesc") val TransactionDesc: String
)
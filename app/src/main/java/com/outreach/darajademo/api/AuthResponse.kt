package com.outreach.darajademo.api

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("expiry_in") val expiry_in: String,
    @SerializedName("access_token") val access_token: String
)
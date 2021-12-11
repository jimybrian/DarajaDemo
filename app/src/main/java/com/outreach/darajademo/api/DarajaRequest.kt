package com.outreach.darajademo.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar
import java.util.Locale

interface DarajaRequest {

    @GET("/oauth/v1/generate")
    fun getAuthorizationToken(
        @Header("authorization") authorization: String,
        @Query("grant_type") grantType: String
    ) : Call<AuthResponse>

    @POST("/mpesa/stkpush/v1/processrequest")
    fun stkPush(
        @Header("Authorization") authorization: String,
        @Body stkPayload: STKPayload
    ) : Call<Any>

}

class RetrofitInstance {
    val baseUrl = "https://sandbox.safaricom.co.ke"

    companion object {
        val consumerKey = "YOUR_CONSUMER_KEY_HERE"
        val consumerSecret = "YOUR_CONSUMER_SECRET_HERE"

        val shortCode = "174379"
        val lipaPasskey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"

        fun getB64AuthorizationKey(): String {
            val bytes = "$consumerKey:$consumerSecret"
                .toByteArray(charset("ISO-8859-1"))
            val base64String = Base64.getEncoder().encodeToString(bytes)

            return base64String
        }

        fun getTimestamp() : String {
            val format = "yyyyMMddhhmmss"
            val timeStamp = SimpleDateFormat(format, Locale.ENGLISH)
                .format(Calendar.getInstance().time)

            return timeStamp
        }

        fun getPassword(timeStamp: String): String{
            val pass = "$shortCode$lipaPasskey$timeStamp".toByteArray(charset("ISO-8859-1"))
            val base64String = Base64.getEncoder().encodeToString(pass)
            return base64String
        }
    }

    var instance: RetrofitInstance? = null
    var darajaInstance: DarajaRequest? = null

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .client(okHttp).baseUrl(baseUrl).
                addConverterFactory(GsonConverterFactory.create())
            .build()

        darajaInstance = retrofit.create(DarajaRequest::class.java)
    }

   @Synchronized
   fun getRetrofitInstance() : RetrofitInstance?{
       if(instance == null)
           instance = RetrofitInstance()

       return instance
   }

    fun getDarajaRequest(): DarajaRequest?{
        return darajaInstance
    }
}
package com.outreach.darajademo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.outreach.darajademo.api.AuthResponse
import com.outreach.darajademo.api.RetrofitInstance
import com.outreach.darajademo.api.STKPayload
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    lateinit var etAmount: EditText
    lateinit var etPhoneNumber: EditText
    lateinit var btnSendRequest: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etAmount = findViewById(R.id.etAmount)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        btnSendRequest = findViewById(R.id.btnSendRequest)


        btnSendRequest.setOnClickListener {
            getAuthToken()
        }
    }


    fun getAuthToken(){
        val authTokenCall = RetrofitInstance()
            .getRetrofitInstance()?.getDarajaRequest()?.getAuthorizationToken(
                "Basic ${RetrofitInstance.getB64AuthorizationKey()}",
                "client_credentials"
            )

        authTokenCall?.enqueue(object: retrofit2.Callback<AuthResponse>{
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val token = response.body()!!
                Toast.makeText(this@MainActivity, "Request Successful", Toast.LENGTH_SHORT).show()
                stkPush(token.access_token)
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Sorry request FAILED", Toast.LENGTH_SHORT).show()
            }

        })
    }


    fun stkPush(token: String){
        val timeStamp = RetrofitInstance.getTimestamp()

        val stkPayload = STKPayload(
            businessShortcode = RetrofitInstance.shortCode,
            password = RetrofitInstance.getPassword(timeStamp),
            timestamp = timeStamp,
            TransactionType = "CustomerPayBillOnline",
            Amount = etAmount.text.toString(),
            PartyA = etPhoneNumber.text.toString(),
            PartyB = RetrofitInstance.shortCode,
            PhoneNumber = etPhoneNumber.text.toString(),
            CallBackURL = "https://example.com/anydomain",
            AccountReference = "Daraja DEMO",
            TransactionDesc = "Daraja DEMO"
        )

        val stkCall = RetrofitInstance()
            .getRetrofitInstance()
            ?.getDarajaRequest()?.stkPush("Bearer $token", stkPayload)

        stkCall?.enqueue(object : retrofit2.Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Toast.makeText(this@MainActivity, "STK PUSH INITIATED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@MainActivity, "STK PUSH REQUEST FAILED", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
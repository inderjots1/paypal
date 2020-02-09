package com.example.paypal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_payment_detail.*
import org.json.JSONException
import org.json.JSONObject

class PaymentDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)
        val extras = intent.extras
        //  val extras4 = intent.extras

        if (extras != null) {
            var jsonObject = JSONObject(extras.getString("paymentdetail"))
            showDetail(jsonObject.getJSONObject("response"), extras.getString("paymentamount"))
        }
    }

    private fun showDetail(jsonObject: JSONObject, string: String?) {
        try {
            tv_txn.text = jsonObject.getString("id")
            tv_status.text = jsonObject.getString("state")
            tv_amount.text = string
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}

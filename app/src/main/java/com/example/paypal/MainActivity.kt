package com.example.paypal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paypal.android.sdk.payments.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    val PAYPAL_REQUEST_CODE = 7171;
    val config = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
        .clientId("Afg3kt8Mh7TaSYrnPdA3SaTZXx2vDYkVQmrHTQYx99tlyJSpJzWvTeS3NMD2qYo9hEqToG1X2zYrdlsK")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, PayPalService::class.java)

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        startService(intent)
        btn_pay.setOnClickListener {
            pay()
        }
    }

    private fun pay() {
        val payment = PayPalPayment(
            BigDecimal("1.75"), "USD", "GAme",
            PayPalPayment.PAYMENT_INTENT_SALE
        )
        val intent = Intent(this, PaymentActivity::class.java)

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)

        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val confirm =
                data!!.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4))

                    var paymentdetial = confirm.toJSONObject().toString(4)
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                    startActivity(
                        Intent(this@MainActivity, PaymentDetail::class.java)
                            .putExtra("paymentdetail", paymentdetial)
                            .putExtra("paymentamount", "1.75")
                    )
                } catch (e: JSONException) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e)
                }

            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this@MainActivity, "Cancel", Toast.LENGTH_LONG).show()
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this@MainActivity, "Payment Inavald", Toast.LENGTH_LONG).show()
        }
    }

    public override fun onDestroy() {
        stopService(Intent(this, PayPalService::class.java))
        super.onDestroy()
    }
}

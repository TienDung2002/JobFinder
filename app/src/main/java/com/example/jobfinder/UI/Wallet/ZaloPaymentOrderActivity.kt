package com.example.jobfinder.UI.Wallet

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.Datas.Api.CreateOrder
import com.example.jobfinder.R
import com.example.jobfinder.UI.Notifications.NotificationViewModel
import com.example.jobfinder.UI.PostedJob.PostedJobViewModel
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.Utils.PreventDoubleClick
import com.example.jobfinder.databinding.ActivityZaloPaymentOrderBinding
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.NumberFormat
import java.util.Currency


class ZaloPaymentOrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityZaloPaymentOrderBinding
    private val postJobVm: PostedJobViewModel by viewModels()
    private val notifyVM: NotificationViewModel by viewModels()
    private val uid = GetData.getCurrentUserId()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZaloPaymentOrderBinding.inflate(layoutInflater)
        setContentView(binding.root )

        // nút back về
        binding.backbtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // Lấy data bên Wallet Acti và hiện ra
        val amount = intent.getDoubleExtra("amount", 0.0)
        val amountString = amount.toInt().toString()

        // format to vnđ
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("VND")
        binding.inputNum.setText(format.format(amount))



        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)



        binding.confirmOrder.setOnClickListener {
            if (PreventDoubleClick.checkClick()) {
                val orderApi = CreateOrder()
                try {
                    val data = orderApi.createOrder(amountString)
                    val code = data.getString("return_code")
                    val message: String = data.optString("return_message", "Không xác định")


                    if (code == "1") {
                        val token: String = data.getString("zp_trans_token")

                        ZaloPaySDK.getInstance().payOrder(this@ZaloPaymentOrderActivity, token, "demozpdk://app", object : PayOrderListener {
                            override fun onPaymentSucceeded(var1: String, var2: String, var3: String) {
                                val detailNotify = "${getString(R.string.deposit)} ${format.format(amountString.toInt())}"
                                val today = GetData.getCurrentDateTime()

                                val intent = Intent(this@ZaloPaymentOrderActivity, WalletActivity::class.java )
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                postJobVm.addWalletAmount(uid.toString(), amountString.toFloat())
                                notifyVM.addNotificationForUser(uid.toString(),"Admin", detailNotify, today)

                                startActivity(intent)

                                showPaymentResultDialog("Thanh toán thành công: $var1, $var2, $var3", R.drawable.ic_payment_success)
                            }
//
                            override fun onPaymentCanceled(var1: String, var2: String) {
                                showPaymentResultDialog("Thanh toán bị hủy: $var1, $var2", R.drawable.ic_payment_failed)
                                val intent = Intent(this@ZaloPaymentOrderActivity, WalletActivity::class.java )
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
//
                            override fun onPaymentError(var1: ZaloPayError, var2: String, var3: String) {
                                showPaymentResultDialog("Lỗi thanh toán: $var1, $var2, $var3", R.drawable.ic_payment_failed)
                                val intent = Intent(this@ZaloPaymentOrderActivity, WalletActivity::class.java )
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        })
                    } else {
                        Log.d("createOrderFailed", message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showPaymentResultDialog("Lỗi không xác định xảy ra: ${e.message}", R.drawable.ic_payment_failed)
                }
            }
        }
    }

    // Nếu thanh toán xong thành công sẽ trả về kết quả
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }


    private fun showPaymentResultDialog(message: String, imageResId: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.zalo_dialog_payment_result)

        val resultImage = dialog.findViewById<ImageView>(R.id.result_image)
        val resultText = dialog.findViewById<TextView>(R.id.result_text)
        val closeButton = dialog.findViewById<TextView>(R.id.close_button)

        resultImage.setImageResource(imageResId)
        resultText.text = message

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
package com.cheqin.booking.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cheqin.booking.R
import kotlinx.android.synthetic.main.send_purchase_otp.*

class OderDeliveredOtp : DialogFragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mobileNumber = arguments!!.getString(MOBILE, "")
//        setStyle(STYLE_NO_TITLE, )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.send_purchase_otp, container, false)
        mobile.setText(mobileNumber)
        sendOtp.setOnClickListener(this)
        buttonDelivered.setOnClickListener(this)
        cancelDialogButton.setOnClickListener(this)
        return v
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
    }

    private lateinit var handler: Handler
    private lateinit var mobileNumber: String
    private lateinit var listener: DialogClickInterface

    var clickEnabled = true
    override fun onClick(v: View) {
        when (v.id) {
            R.id.sendOtp -> {
                sendOtp.setEnabled(false)
                if (listener != null) {
                    listener.sendOtp()
                }
            }
            R.id.buttonDelivered -> {
                if (clickEnabled) {
                    clickEnabled = false
                    Handler().postDelayed({ clickEnabled = true }, 2000) // set time as per your requirement , current is 2 sec
                }
                if (listener != null) {
                    val otp: String = otpET.getText().toString()
                    if (isValidOtp(otp)) {
                        listener.submitOtp(otp)
                    } else {
                        otp_error.setVisibility(View.VISIBLE)
                        otp_error.setText("Invalid Otp")
                    }
                }
            }
            R.id.cancelDialogButton -> dismiss()
        }
    }

    private fun isValidOtp(otp: String?): Boolean {
        if (otp == null) return false
        return if (otp.length < 3) false else true
    }

    fun resendActivate() {
        sendOtp.setEnabled(true)
        otp_resend_message.setVisibility(View.INVISIBLE)
    }

    fun setOtpError(message: String?) {
        otp_error.setVisibility(View.VISIBLE)
        otp_error.setText(message)
    }

    fun resendTimeRemaining(millis: Long) {
        val minutes = (millis / 1000 / 60).toInt()
        val seconds = (millis / 1000 % 60).toInt()
        otp_resend_message.setVisibility(View.VISIBLE)
        otp_resend_message.setText(String.format(getString(R.string.otp_resend_message_text), minutes, seconds))
    }

    fun setOtpMessage(message: String?) {
        otp_error.setVisibility(View.VISIBLE)
        otp_error.setText(message)
        handler = Handler()
        handler.postDelayed({ otp_error.setVisibility(View.INVISIBLE) }, 5000)
    }

    interface DialogClickInterface {
        fun sendOtp()
        fun submitOtp(otp: String?)
    }

    companion object {
        private const val MOBILE = "mobile"
        fun newInstance(mobile: String?): OderDeliveredOtp {
            val args = Bundle()
            args.putString(MOBILE, mobile)
            val fragment = OderDeliveredOtp()
            fragment.arguments = args
            return fragment
        }
    }

    fun setClicksInterface(dialogClickInterface: DialogClickInterface) {
        listener = dialogClickInterface
    }
}

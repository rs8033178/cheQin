package com.cheqin.booking.dialogs

import android.app.Dialog
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.cheqin.booking.R
import com.cheqin.booking.viewmodel.UserBookingViewModel
import kotlinx.android.synthetic.main.post_request_confirmation_dialog.*


class PostResquestConfirmationDialog : DialogFragment() {

    private lateinit var callbackListener: ActivityCallback
    private var model: UserBookingViewModel? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.let { ViewModelProvider(it).get(UserBookingViewModel::class.java) };
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    fun setListener(callback: ActivityCallback) {
        callbackListener = callback;

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.post_request_confirmation_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adultValue.text = model?.confirmationBean?.adults
        roomvalue.text = model?.confirmationBean?.rooms
        nightvalue.text = model?.confirmationBean?.nights
        selectedvalue.text = model?.confirmationBean?.selectedRoom
        hotelClassValue.text = model?.confirmationBean?.hotelClass
        cancleButton.setOnClickListener({ dismiss() });
        proceedButton.setOnClickListener({ callbackListener?.proceedClicked()
            dismiss() })
    }


    override fun onResume() {
        super.onResume()
        val window: Window = dialog!!.window
        val size = Point()
        val display: Display = window.getWindowManager().getDefaultDisplay()
        display.getSize(size)
        val width: Int = size.x
        window.setLayout((width * 0.95).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
    }

    interface ActivityCallback {
        fun proceedClicked()
    }

}
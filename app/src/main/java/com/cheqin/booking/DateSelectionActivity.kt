package com.cheqin.booking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.kizitonwose.calendarviewsample.DateSelectionFragment
import com.cheqin.booking.utils.Constants
import com.cheqin.booking.utils.Constants.CHECK_IN_DATE
import com.cheqin.booking.utils.Constants.CHECK_OUT_DATE

class DateSelectionActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun startActivityForResult(
            context: Activity,
            startDate: String,
            endDate: String,
            requestCode: Int
        ) {
            val intent = Intent(context, DateSelectionActivity::class.java)
            intent.apply {
                putExtra(CHECK_IN_DATE, startDate)
                putExtra(CHECK_OUT_DATE, endDate)
            }
            context.startActivityForResult(intent, requestCode)
        }
    }

    private val fragmentContainer by lazy { findViewById<FragmentContainerView>(R.id.fragmentContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)

        val startDate = intent.getStringExtra(CHECK_IN_DATE)
        val endDate = intent.getStringExtra(CHECK_OUT_DATE)

        if (savedInstanceState == null) {

            val datePickerFragment = DateSelectionFragment.getInstance(startDate, endDate)

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, datePickerFragment)
                .commit()
        }

    }

    fun setResultAndFinish(startDate: String, endDate: String) {
        val resultIntent = Intent()
        resultIntent.apply {
            putExtra(Constants.CHECK_IN_DATE, startDate)
            putExtra(Constants.CHECK_OUT_DATE, endDate)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
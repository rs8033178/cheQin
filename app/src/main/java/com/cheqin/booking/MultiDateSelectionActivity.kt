package com.cheqin.booking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.kizitonwose.calendarviewsample.MultiDateSelectionFragment
import com.cheqin.booking.utils.Constants

class MultiDateSelectionActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun startActivityForResult(
            context: Activity,
            isMultiSelectionMode: Boolean,
            selectedDates: ArrayList<String>?,
            requestCode: Int
        ) {
            val intent = Intent(context, MultiDateSelectionActivity::class.java)
            intent.apply {
                putExtra(Constants.MULTIDATE_SELECTION_MODE, isMultiSelectionMode)
                putStringArrayListExtra(Constants.CHECK_IN_DATE, selectedDates)
            }
            context.startActivityForResult(intent, requestCode)
        }
    }

    private val fragmentContainer by lazy { findViewById<FragmentContainerView>(R.id.fragmentContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)

        if (savedInstanceState == null) {

            val multiDateSelectionFragment = MultiDateSelectionFragment.getInstance(intent.extras)

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, multiDateSelectionFragment)
                .commit()
        }

    }

    fun setResultAndFinish(startDate: List<String>) {
        val resultIntent = Intent()
        resultIntent.apply {
            putStringArrayListExtra(
                Constants.CHECK_IN_DATE,
                startDate as java.util.ArrayList<String>?
            )
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}
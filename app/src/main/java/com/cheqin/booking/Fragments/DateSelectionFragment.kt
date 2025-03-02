package com.kizitonwose.calendarviewsample


import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.children
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.cheqin.booking.DateSelectionActivity
import com.cheqin.booking.utils.Constants.*
import kotlinx.android.synthetic.main.calendar_day_legend.*
import kotlinx.android.synthetic.main.date_selction_calendar_day.view.*
import kotlinx.android.synthetic.main.date_selection_calendar_header.view.*
import kotlinx.android.synthetic.main.date_selection_fragment.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*


class DateSelectionFragment : BaseFragment(), HasToolbar, HasBackButton {

    companion object {

        @JvmStatic
        fun getInstance(startDate: String, endDate: String): DateSelectionFragment {
            val fragment = DateSelectionFragment().apply {
                val bundle = Bundle()
                bundle.putString(CHECK_IN_DATE, startDate)
                bundle.putString(CHECK_OUT_DATE, endDate)
                arguments = bundle
            }

            return fragment
        }
    }

    private val doneBtn by lazy { view!!.findViewById<ExtendedFloatingActionButton>(com.cheqin.booking.R.id.doneBtn) }

    override val toolbar: Toolbar?
        get() = dateFragmentToolbar

    override val titleRes: Int? = null

    private val today = LocalDate.now()

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE'\n'd MMM")

    private val startBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(com.cheqin.booking.R.drawable.date_continuous_selected_bg_start) as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(com.cheqin.booking.R.drawable.date_continuous_selected_bg_end) as GradientDrawable
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(
            com.cheqin.booking.R.layout.date_selection_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // We set the radius of the continuous selection background drawable dynamically
        // since the view size is `match parent` hence we cannot determine the appropriate
        // radius value which would equal half of the view's size beforehand.
        exFourCalendar.post {
            val radius = ((exFourCalendar.width / 7) / 2).toFloat()
            startBackground.setCornerRadius(topLeft = radius, bottomLeft = radius)
            endBackground.setCornerRadius(topRight = radius, bottomRight = radius)
        }

        // Set the First day of week depending on Locale
        val daysOfWeek = daysOfWeekFromLocale()
        legendLayout.children.forEachIndexed { index, childView ->
            (childView as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTextColorRes(com.cheqin.booking.R.color.calendar_grey)
            }
        }

        val currentMonth = YearMonth.now()
        exFourCalendar.setup(currentMonth, currentMonth.plusMonths(12), daysOfWeek.first())
        exFourCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.exFourDayText
            val roundBgView = view.exFourRoundBgView

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(
                            today
                        ))
                    ) {
                        val date = day.date
                        if (startDate != null) {
                            if (date < startDate || endDate != null) {
                                startDate = date
                                endDate = null
                                hideDoneButton()
                            } else if (date != startDate) {
                                endDate = date
                                showDoneButton()
                            }
                        } else {
                            startDate = date
                            hideDoneButton()
                        }
                        exFourCalendar.notifyCalendarChanged()
                        bindSummaryViews()
                    }
                }
            }

        }
        exFourCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val roundBgView = container.roundBgView

                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.day.toString()

                    if (day.date.isBefore(today)) {
                        textView.setTextColorRes(com.cheqin.booking.R.color.calendar_grey_past)
                    } else {
                        when {
                            startDate == day.date && endDate == null -> {
                                textView.setTextColorRes(com.cheqin.booking.R.color.white)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(com.cheqin.booking.R.drawable.date_single_selected_bg)
                            }
                            day.date == startDate -> {
                                textView.setTextColorRes(com.cheqin.booking.R.color.white)
                                textView.background = startBackground
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView.setTextColorRes(com.cheqin.booking.R.color.white)
                                textView.setBackgroundResource(com.cheqin.booking.R.drawable.date_continuous_selected_bg_middle)
                            }
                            day.date == endDate -> {
                                textView.setTextColorRes(com.cheqin.booking.R.color.white)
                                textView.background = endBackground
                            }
                            day.date == today -> {
                                textView.setTextColorRes(com.cheqin.booking.R.color.calendar_grey)
                                //roundBgView.makeVisible()
                                //roundBgView.setBackgroundResource(R.drawable.example_4_today_bg)
                            }
                            else -> textView.setTextColorRes(com.cheqin.booking.R.color.calendar_grey)
                        }
                    }
                } else {

                    // This part is to make the coloured selection background continuous
                    // on the blank in and out dates across various months and also on dates(months)
                    // between the start and end dates if the selection spans across multiple months.

                    val startDate = startDate
                    val endDate = endDate
                    if (startDate != null && endDate != null) {
                        // Mimic selection of inDates that are less than the startDate.
                        // Example: When 26 Feb 2019 is startDate and 5 Mar 2019 is endDate,
                        // this makes the inDates in Mar 2019 for 24 & 25 Feb 2019 look selected.
                        if ((day.owner == DayOwner.PREVIOUS_MONTH
                                    && startDate.monthValue == day.date.monthValue
                                    && endDate.monthValue != day.date.monthValue) ||
                            // Mimic selection of outDates that are greater than the endDate.
                            // Example: When 25 Apr 2019 is startDate and 2 May 2019 is endDate,
                            // this makes the outDates in Apr 2019 for 3 & 4 May 2019 look selected.
                            (day.owner == DayOwner.NEXT_MONTH
                                    && startDate.monthValue != day.date.monthValue
                                    && endDate.monthValue == day.date.monthValue) ||

                            // Mimic selection of in and out dates of intermediate
                            // months if the selection spans across multiple months.
                            (startDate < day.date && endDate > day.date
                                    && startDate.monthValue != day.date.monthValue
                                    && endDate.monthValue != day.date.monthValue)
                        ) {
                            textView.setBackgroundResource(com.cheqin.booking.R.drawable.date_continuous_selected_bg_middle)
                        }
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
        }
        exFourCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle =
                    "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
            }
        }

        doneBtn.setOnClickListener click@{
            val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)
            val startDate = formatter.format(startDate)
            val endDate = formatter.format(endDate)

            val activity = requireActivity() as DateSelectionActivity
            activity.setResultAndFinish(startDate, endDate)
        }

        bindSummaryViews()

        setUpInitialDates()

    }

    private fun setUpInitialDates() {
        val startDateString = arguments?.getString(CHECK_IN_DATE)
        val endDateString = arguments?.getString(CHECK_OUT_DATE)

        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)
        startDate = LocalDate.parse(startDateString, formatter)
        endDate = LocalDate.parse(endDateString, formatter)

        if (startDate != null && endDate != null) {
            showDoneButton()
            exFourCalendar.scrollToMonth(startDate!!.yearMonth)
        }

        bindSummaryViews()
    }

    private fun showDoneButton() {
        val x = doneBtn.width / 2
        val y = doneBtn.height / 2

        val startRadius = 0
        val endRadius =
            Math.hypot(doneBtn.width.toDouble(), doneBtn.height.toDouble())
                .toInt()

        val isAttachedToWindow = ViewCompat.isAttachedToWindow(doneBtn)

        if (isAttachedToWindow) {
            val anim = ViewAnimationUtils.createCircularReveal(
                doneBtn,
                x,
                y,
                startRadius.toFloat(),
                endRadius.toFloat()
            ).setDuration(500)

            doneBtn.visibility = View.VISIBLE

            anim.start()
        } else {
            doneBtn.visibility = View.VISIBLE
        }

    }

    private fun hideDoneButton() {
        val x = doneBtn.width
        val y = doneBtn.height

        val startRadius = doneBtn.width / 2
        val endRadius = 0

        val isAttachedToWindow = ViewCompat.isAttachedToWindow(doneBtn)

        if (isAttachedToWindow) {
            val anim = ViewAnimationUtils.createCircularReveal(
                doneBtn,
                x,
                y,
                startRadius.toFloat(),
                endRadius.toFloat()
            ).setDuration(500)

            doneBtn.visibility = View.INVISIBLE

            anim.start()
        } else {
            doneBtn.visibility = View.INVISIBLE

        }
    }


    private fun bindSummaryViews() {
        if (startDate != null) {
            exFourStartDateText.text = headerDateFormatter.format(startDate)
            exFourStartDateText.setTextColorRes(com.cheqin.booking.R.color.calendar_grey)
        } else {
            exFourStartDateText.text = getString(com.cheqin.booking.R.string.start_date)
            exFourStartDateText.setTextColor(Color.GRAY)
        }
        if (endDate != null) {
            exFourEndDateText.text = headerDateFormatter.format(endDate)
            exFourEndDateText.setTextColorRes(com.cheqin.booking.R.color.calendar_grey)
        } else {
            exFourEndDateText.text = getString(com.cheqin.booking.R.string.end_date)
            exFourEndDateText.setTextColor(Color.GRAY)
        }

        // Enable save button if a range is selected or no date is selected at all, Airbnb style.
        doneBtn.isEnabled = endDate != null || (startDate == null && endDate == null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.cheqin.booking.R.menu.date_picker_menu, menu)
        dateFragmentToolbar.post {
            // Configure menu text to match what is in the Airbnb app.
            dateFragmentToolbar.findViewById<TextView>(com.cheqin.booking.R.id.menuItemClear)
                .apply {
                    setTextColor(requireContext().getColorCompat(com.cheqin.booking.R.color.calendar_grey))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    isAllCaps = false
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.cheqin.booking.R.id.menuItemClear) {
            startDate = null
            endDate = null
            hideDoneButton()
            exFourCalendar.notifyCalendarChanged()
            bindSummaryViews()
            return true
        } else if (item.itemId == android.R.id.home) {
            requireActivity().finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val closeIndicator =
            requireContext().getDrawableCompat(com.cheqin.booking.R.drawable.ic_close)?.apply {
                setColorFilter(
                    requireContext().getColorCompat(com.cheqin.booking.R.color.calendar_grey),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(closeIndicator)
        requireActivity().window.apply {
            // Update statusbar color to match toolbar color.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarColor =
                    requireContext().getColorCompat(com.cheqin.booking.R.color.white)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                statusBarColor = Color.GRAY
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.apply {
            // Reset statusbar color.
            statusBarColor =
                requireContext().getColorCompat(com.cheqin.booking.R.color.colorPrimaryDark)
            decorView.systemUiVisibility = 0
        }
    }
}

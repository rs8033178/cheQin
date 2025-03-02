package com.kizitonwose.calendarviewsample


import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.ViewCompat
import androidx.core.view.children
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.yearMonth
import com.cheqin.booking.MultiDateSelectionActivity
import com.cheqin.booking.R
import com.cheqin.booking.utils.Constants
import kotlinx.android.synthetic.main.calendar_day_legend.*
import kotlinx.android.synthetic.main.date_selection_calendar_header.view.*
import kotlinx.android.synthetic.main.example_1_calendar_day.view.*
import kotlinx.android.synthetic.main.example_1_fragment.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*


class MultiDateSelectionFragment : BaseFragment(), HasToolbar {


    companion object {
        fun getInstance(bundle: Bundle): MultiDateSelectionFragment {
            return MultiDateSelectionFragment().apply {
                arguments = bundle
            }
        }
    }

    override val toolbar: Toolbar?
        get() = null

    override val titleRes: Int = R.string.dialog_default_title

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.example_1_fragment, container, false)
    }

    private val selectedDates = mutableSetOf<LocalDate>()
    private var isMultiSelectionMode: Boolean = false
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private fun setResultAndFinish() {
        val formatter =
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_PATTERN)

        val result = ArrayList<String>()

        selectedDates.forEach {
            result.add(formatter.format(it))
        }

        val activity = activity as MultiDateSelectionActivity
        activity.setResultAndFinish(result)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isMultiSelectionMode = arguments!!.getBoolean(Constants.MULTIDATE_SELECTION_MODE)

        val daysOfWeek = daysOfWeekFromLocale()
        legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    .toUpperCase(Locale.ENGLISH)
                setTextColorRes(R.color.calendar_grey)
            }
        }

        doneBtn.setOnClickListener {
            setResultAndFinish()
        }

        if (isMultiSelectionMode) {
            exOneMonthText.text = "You can select upto 3 dates"
        } else {
            exOneMonthText.text = "Please select event date"
        }

        val currentMonth = YearMonth.now()
        val endMonth = currentMonth.plusMonths(10)
        exOneCalendar.setup(currentMonth, endMonth, daysOfWeek.first())
        exOneCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = view.exOneDayText

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH && !day.date.isBefore(today)) {
                        if (selectedDates.contains(day.date)) {
                            selectedDates.remove(day.date)
                        } else {
                            if (isMultiSelectionMode) {
                                if (selectedDates.size == 3) {
                                    val firstDate = selectedDates.first()
                                    selectedDates.remove(firstDate)
                                }
                                selectedDates.add(day.date)
                            } else {
                                if (selectedDates.size == 1) {
                                    val firstDate = selectedDates.first()
                                    selectedDates.remove(firstDate)
                                }
                                selectedDates.add(day.date)
                            }
                        }
                        if (selectedDates.isEmpty()) {
                            hideDoneButton()
                        } else {
                            showDoneButton()
                        }
                        exOneCalendar.notifyCalendarChanged()
                    }
                }
            }

        }

        exOneCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        selectedDates.contains(day.date) -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)

                        }
                        today == day.date -> {
                            textView.setTextColorRes(R.color.utel_color)
                        }
                        day.date.isBefore(today) -> {
                            textView.setTextColorRes(R.color.calendar_grey_past)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.calendar_grey_past)
                    textView.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
        }
        exOneCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle =
                    "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
            }
        }


        exOneCalendar.monthScrollListener = {
            if (exOneCalendar.maxRowCount == 6) {
                exOneYearText.text = it.yearMonth.year.toString()
                //exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
            } else {
                // In week mode, we show the header a bit differently.
                // We show indices with dates from different months since
                // dates overflow and cells in one index can belong to different
                // months/years.
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    exOneYearText.text = firstDate.yearMonth.year.toString()
                    //exOneMonthText.text = monthTitleFormatter.format(firstDate)
                } else {
                    /*exOneMonthText.text =
                        "${monthTitleFormatter.format(firstDate)} - ${monthTitleFormatter.format(
                            lastDate
                        )}"*/
                    if (firstDate.year == lastDate.year) {
                        exOneYearText.text = firstDate.yearMonth.year.toString()
                    } else {
                        exOneYearText.text =
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }

        }

        weekModeCheckBox.setOnCheckedChangeListener { _, monthToWeek ->
            val firstDate =
                exOneCalendar.findFirstVisibleDay()?.date ?: return@setOnCheckedChangeListener
            val lastDate =
                exOneCalendar.findLastVisibleDay()?.date ?: return@setOnCheckedChangeListener

            val oneWeekHeight = exOneCalendar.dayHeight
            val oneMonthHeight = oneWeekHeight * 6

            val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
            val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight

            // Animate calendar height changes.
            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
            animator.addUpdateListener { animator ->
                exOneCalendar.layoutParams = exOneCalendar.layoutParams.apply {
                    height = animator.animatedValue as Int
                }
            }

            // When changing from month to week mode, we change the calendar's
            // config at the end of the animation(doOnEnd) but when changing
            // from week to month mode, we change the calendar's config at
            // the start of the animation(doOnStart). This is so that the change
            // in height is visible. You can do this whichever way you prefer.

            animator.doOnStart {
                if (!monthToWeek) {
                    exOneCalendar.inDateStyle = InDateStyle.ALL_MONTHS
                    exOneCalendar.maxRowCount = 6
                    exOneCalendar.hasBoundaries = true
                }
            }
            animator.doOnEnd {
                if (monthToWeek) {
                    exOneCalendar.inDateStyle = InDateStyle.FIRST_MONTH
                    exOneCalendar.maxRowCount = 1
                    exOneCalendar.hasBoundaries = false
                }

                if (monthToWeek) {
                    // We want the first visible day to remain
                    // visible when we change to week mode.
                    exOneCalendar.scrollToDate(firstDate)
                } else {
                    // When changing to month mode, we choose current
                    // month if it is the only one in the current frame.
                    // if we have multiple months in one frame, we prefer
                    // the second one unless it's an outDate in the last index.
                    if (firstDate.yearMonth == lastDate.yearMonth) {
                        exOneCalendar.scrollToMonth(firstDate.yearMonth)
                    } else {
                        exOneCalendar.scrollToMonth(minOf(firstDate.yearMonth.next, endMonth))
                    }
                }
            }
            animator.duration = 250
            animator.start()
        }

    }

    private fun showDoneButton() {

        if (doneBtn.visibility == View.VISIBLE)
            return;

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

        if (doneBtn.visibility == View.INVISIBLE)
            return;

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

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            requireContext().getColorCompat(R.color.colorPrimaryDark)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor =
            requireContext().getColorCompat(R.color.colorPrimaryDark)
    }

}

package com.kandyba.mygeneration.presentation.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.models.presentation.user.Region
import com.kandyba.mygeneration.models.presentation.user.UserField
import com.kandyba.mygeneration.presentation.utils.datetime.formatDateWithWords
import com.kandyba.mygeneration.presentation.utils.datetime.formatTime
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel
import com.kandyba.mygeneration.presentation.viewmodel.ViewModelFactory
import java.util.*

class AddEventBottomSheetFragment :
    BaseBottomSheetFragment<MainFragmentViewModel>(R.layout.add_event_bottom_fragment) {

    override val viewModelClass: Class<MainFragmentViewModel>
        get() = MainFragmentViewModel::class.java

    override val viewModelFactory: ViewModelFactory<MainFragmentViewModel>
        get() = appComponent.getMainFragmentViewModelFactory()

    private lateinit var eventName: EditText
    private lateinit var eventDescription: EditText
    private lateinit var eventDate: EditText
    private lateinit var startTime: EditText
    private lateinit var finishTime: EditText
    private lateinit var addEventButton: Button

    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var startTimePicker: MaterialTimePicker
    private lateinit var finishTimePicker: MaterialTimePicker
    private lateinit var settings: SharedPreferences

    private var timestamp: Long = 0

    override fun initFields(root: View) {
        with(root) {
            eventName = findViewById(R.id.event_name)
            eventDescription = findViewById(R.id.event_description)
            startTime = findViewById(R.id.start_time)
            finishTime = findViewById(R.id.finish_time)
            addEventButton = findViewById(R.id.add_new_event)
            eventDate = findViewById(R.id.event_date)
        }
        setDateValue()
        initListeners()
    }

    private fun setDateValue() {
        val timeInMs = arguments?.getLong(TIME_IN_MS) ?: 0
        if (timeInMs != 0L) {
            val date = Calendar.getInstance().apply { timeInMillis = timeInMs }
            eventDate.setText(date.formatDateWithWords())
            timestamp = timeInMs
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settings = appComponent.getSharedPreferences()
    }

    private fun initListeners() {
        addEventButton.setOnClickListener {
            if (checkFields()) {
                viewModel.addNewEvent(collectNewEvent())
                dismiss()
            }
        }
        setDatePicker()
        setTimePickers()
        eventDate.setOnClickListener { viewModel.openBottomFragment(datePicker) }
        startTime.setOnClickListener { viewModel.openBottomFragment(startTimePicker) }
        finishTime.setOnClickListener { viewModel.openBottomFragment(finishTimePicker) }
    }

    private fun checkFields(): Boolean {
        var valid = true
        val fields = listOf(eventName, eventDescription, eventDate, startTime, finishTime)
        for (f in fields) {
            if (f.text.toString().isEmpty()) {
                f.error = resources.getString(R.string.field_cannot_be_empty)
                valid = false
            }
        }
        return valid
    }

    private fun collectNewEvent() = Event(
        eventName.text.toString(),
        eventDescription.text.toString(),
        settings.getString(UserField.REGION_CODE.preferencesKey, Region.COMMON.regionCode)
            ?: Region.COMMON.regionCode,
        timestamp,
        startTime.text.toString(),
        finishTime.text.toString()
    )

    private fun setTimePickers() {
        startTimePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.choose_time)
            .setTheme(R.style.ThemeOverlay_App_TimePicker)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        startTimePicker.addOnPositiveButtonClickListener {
            val time = formatTime(startTimePicker.hour, startTimePicker.minute)
            startTime.setText(time)
        }

        finishTimePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.choose_time)
            .setTheme(R.style.ThemeOverlay_App_TimePicker)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        finishTimePicker.addOnPositiveButtonClickListener {
            val time = formatTime(finishTimePicker.hour, finishTimePicker.minute)
            finishTime.setText(time)
        }
    }

    private fun setDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.choose_date)
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            timestamp = it
            val date = Calendar.getInstance().apply { timeInMillis = it }
            val dateString = date.formatDateWithWords()
            eventDate.setText(dateString)
        }
    }

    companion object {
        private const val TIME_IN_MS = "TIME"
        fun newInstance(timeInMs: Long) = AddEventBottomSheetFragment().apply {
            arguments = Bundle().apply { putLong(TIME_IN_MS, timeInMs) }
        }
    }

}
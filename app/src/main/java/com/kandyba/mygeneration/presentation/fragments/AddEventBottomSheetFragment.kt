package com.kandyba.mygeneration.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.presentation.utils.formatDateForFirebase
import com.kandyba.mygeneration.presentation.utils.formatDateForUser
import com.kandyba.mygeneration.presentation.utils.formatTime
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel
import java.util.*

class AddEventBottomSheetFragment : BaseBottomSheetFragment() {

    private lateinit var eventName: EditText
    private lateinit var eventDescription: EditText
    private lateinit var eventDate: EditText
    private lateinit var startTime: EditText
    private lateinit var finishTime: EditText

    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var startTimePicker: MaterialTimePicker
    private lateinit var finishTimePicker: MaterialTimePicker

    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.add_event_bottom_fragment, container, false)
        with(root) {
            eventName = findViewById(R.id.event_name)
            eventDescription = findViewById(R.id.event_description)
            eventDate = findViewById(R.id.event_date)
            startTime = findViewById(R.id.start_time)
            finishTime = findViewById(R.id.finish_time)
        }
        setDatePicker()
        setTimePickers()
        eventDate.setOnClickListener { viewModel.openBottomFragment(datePicker) }
        startTime.setOnClickListener { viewModel.openBottomFragment(startTimePicker) }
        finishTime.setOnClickListener { viewModel.openBottomFragment(finishTimePicker) }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val component = (requireActivity().application as App).appComponent
        viewModel =
            ViewModelProvider(requireActivity(), component.getMainFragmentViewModelFactory())
                .get(MainFragmentViewModel::class.java)
    }

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
            val date = Calendar.getInstance()
            date.timeInMillis = it
            val dateString = date.formatDateForUser()
            val firebaseString = date.formatDateForFirebase()
            eventDate.setText(dateString)
        }
    }

    companion object {
        fun newInstance() = AddEventBottomSheetFragment()
    }
}
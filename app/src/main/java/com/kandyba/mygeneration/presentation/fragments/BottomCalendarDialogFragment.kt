package com.kandyba.mygeneration.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.presentation.adapters.EventAdapter
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel
import com.kandyba.mygeneration.presentation.viewmodel.ViewModelFactory


class BottomCalendarDialogFragment :
    BaseBottomSheetFragment<MainFragmentViewModel>(R.layout.bottom_calendar_fragment) {

    override val viewModelClass: Class<MainFragmentViewModel>
        get() = MainFragmentViewModel::class.java

    override val viewModelFactory: ViewModelFactory<MainFragmentViewModel>
        get() = appComponent.getMainFragmentViewModelFactory()

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventAdapter
    private lateinit var addEventButton: FloatingActionButton
    private lateinit var rootView: View

    private var eventsList: ArrayList<Event> = ArrayList()

    override fun initFields(root: View) {
        addEventButton = rootView.findViewById(R.id.add_event)
        setAddButtonVisibility()
        addEventButton.setOnClickListener {
            val time = arguments?.getLong(TIME_IN_MS) ?: 0
            viewModel.openBottomFragment(AddEventBottomSheetFragment.newInstance(time))
        }
        eventsRecyclerView = rootView.findViewById(R.id.events_list)
        eventsList = arguments?.getParcelableArrayList<Event>(EVENT_LIST) as ArrayList<Event>
        eventsAdapter = EventAdapter(eventsList)
        eventsRecyclerView.adapter = eventsAdapter

        dialog?.setOnShowListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(rootView.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            val div = rootView.findViewById<ImageView>(R.id.stick)
            bottomSheetBehavior.peekHeight = eventsRecyclerView[0].height + div.height
        }
    }

    private fun setAddButtonVisibility() {
        val visible = arguments?.getBoolean(SHOW_ADD_BUTTON) ?: false
        addEventButton.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        private const val EVENT_LIST = "events"
        private const val TIME_IN_MS = "TIME"
        private const val SHOW_ADD_BUTTON = "SHOW_ADD_BUTTON"

        fun newInstance(
            events: List<Event>,
            time_in_ms: Long,
            showAddButton: Boolean
        ): BottomCalendarDialogFragment {
            val fragment = BottomCalendarDialogFragment()
            val args = Bundle()
            args.putParcelableArrayList(EVENT_LIST, ArrayList(events))
            args.putLong(TIME_IN_MS, time_in_ms)
            args.putBoolean(SHOW_ADD_BUTTON, showAddButton)
            fragment.arguments = args
            return fragment
        }

    }
}
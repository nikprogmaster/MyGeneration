package com.kandyba.mygeneration.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.presentation.adapters.EventAdapter
import com.kandyba.mygeneration.presentation.viewmodel.CalendarDialogViewModel


class BottomCalendarDialogFragment : BaseBottomSheetFragment() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventAdapter
    private lateinit var addEventButton: FloatingActionButton
    private lateinit var rootView: View

    private var eventsList: ArrayList<Event> = ArrayList()

    private lateinit var viewModel: CalendarDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(R.layout.bottom_calendar_fragment, container, false)
        addEventButton = rootView.findViewById(R.id.add_event)
        addEventButton.setOnClickListener { viewModel.addNewEvent("NewEvent") }
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

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = (requireActivity().application as App).appComponent
            .getCalendarDialogViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[CalendarDialogViewModel::class.java]
        viewModel.addNewEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Ну привет, дорогой", Toast.LENGTH_LONG).show()
        }
    }


    companion object {
        private const val EVENT_LIST = "events"

        fun newInstance(events: List<Event>): BottomCalendarDialogFragment {
            val args = Bundle()
            args.putParcelableArrayList(EVENT_LIST, ArrayList(events))
            val fragment = BottomCalendarDialogFragment()
            fragment.arguments = args
            return fragment
        }

    }
}
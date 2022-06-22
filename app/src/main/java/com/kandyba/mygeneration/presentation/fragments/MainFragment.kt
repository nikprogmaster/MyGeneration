package com.kandyba.mygeneration.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.CalendarManager
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.models.presentation.calendar.addEventsToMap
import com.kandyba.mygeneration.presentation.activities.MainActivity
import com.kandyba.mygeneration.presentation.adapters.PostsAdapter
import com.kandyba.mygeneration.presentation.binder.CalendarDayBinder
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel
import com.kizitonwose.calendarview.CalendarView

class MainFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var monthTitle: TextView
    private lateinit var arrow: ImageView
    private lateinit var titleLayout: LinearLayout
    private lateinit var calendarManager: CalendarManager
    private lateinit var postsRecyclerView: RecyclerView

    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var calendarDayBinder: CalendarDayBinder
    private var postsAdapter: PostsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.main_fragment, container, false)
        calendarView = root.findViewById(R.id.calendar_view)
        monthTitle = root.findViewById(R.id.date_title)
        arrow = root.findViewById(R.id.month_arrow)
        titleLayout = root.findViewById(R.id.title_layout)
        postsRecyclerView = root.findViewById(R.id.posts_recycler)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = (requireActivity().application as App).appComponent
            .getMainFragmentViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)
            .get(MainFragmentViewModel::class.java)
        initObservers()
        viewModel.init()
    }

    private fun initObservers() {
        viewModel.events.observe(requireActivity(), Observer { events ->
            setCalendarDayBinder(events)
            setCalendarManager()
        })
        viewModel.openBottomEventSheet.observe(requireActivity(), Observer { events ->
            openBottomSheetFragment(events)
        })
        viewModel.vkPosts.observe(requireActivity(), Observer { posts ->
            postsAdapter = PostsAdapter(posts)
            postsRecyclerView.adapter = postsAdapter
        })
    }

    private fun openBottomSheetFragment(events: List<Event>) {
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.openBottomSheetFragment(events)
        }
    }

    private fun setCalendarManager() {
        val arrowUp = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_drop_up)
        val arrowDown = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_drop_down)
        calendarManager =
            CalendarManager(titleLayout, calendarView, arrow, monthTitle, arrowUp, arrowDown)
        calendarManager.setupCalendar()
    }

    private fun setCalendarDayBinder(allEvents: List<Event>) {
        val map = addEventsToMap(allEvents)
        calendarDayBinder = CalendarDayBinder(map, requireContext()) { events ->
            viewModel.openBottomCalendarFragment(events)
        }
        calendarView.dayBinder = calendarDayBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        calendarDayBinder.utilizeContext()
    }

    companion object {
        fun newInstance(): MainFragment {
            val args = Bundle()

            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
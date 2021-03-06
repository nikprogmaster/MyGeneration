package com.kandyba.mygeneration.presentation.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.models.presentation.user.AccountType
import com.kandyba.mygeneration.models.presentation.user.Region.COMMON
import com.kandyba.mygeneration.models.presentation.user.UserField
import com.kandyba.mygeneration.models.presentation.user.UserField.REGION_CODE
import com.kandyba.mygeneration.presentation.adapters.PostsAdapter
import com.kandyba.mygeneration.presentation.binder.CalendarDayBinder
import com.kandyba.mygeneration.presentation.utils.datetime.addEventsToMap
import com.kandyba.mygeneration.presentation.view.CalendarManager
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel
import com.kandyba.mygeneration.presentation.viewmodel.ViewModelFactory
import com.kizitonwose.calendarview.CalendarView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragment : BaseFragment<MainFragmentViewModel>(R.layout.main_fragment) {

    override val viewModelClass: Class<MainFragmentViewModel>
        get() = MainFragmentViewModel::class.java

    override val viewModelFactory: ViewModelFactory<MainFragmentViewModel>
        get() = appComponent.getMainFragmentViewModelFactory()

    private lateinit var calendarView: CalendarView
    private lateinit var monthTitle: TextView
    private lateinit var arrow: ImageView
    private lateinit var titleLayout: LinearLayout
    private lateinit var calendarManager: CalendarManager
    private lateinit var postsRecyclerView: RecyclerView

    private lateinit var calendarDayBinder: CalendarDayBinder
    private lateinit var settings: SharedPreferences
    private var postsAdapter: PostsAdapter? = null

    override fun initFields(root: View) {
        with(root) {
            calendarView = findViewById(R.id.calendar_view)
            monthTitle = findViewById(R.id.date_title)
            arrow = findViewById(R.id.month_arrow)
            titleLayout = findViewById(R.id.title_layout)
            postsRecyclerView = findViewById(R.id.posts_recycler)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settings = appComponent.getSharedPreferences()
        val regionCode =
            settings.getString(REGION_CODE.preferencesKey, COMMON.regionCode) ?: COMMON.regionCode
        viewModel.init(regionCode)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getEvents(settings.getString(REGION_CODE.preferencesKey, COMMON.regionCode))
    }

    override fun onDestroy() {
        super.onDestroy()
        calendarDayBinder.utilizeContext()
    }

    override fun observeViewModel() {
        super.observeViewModel()
        viewModel.events.onEach {
            setCalendarDayBinder(it)
            setCalendarManager()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.vkPosts.onEach {
            postsAdapter = PostsAdapter(it)
            postsRecyclerView.adapter = postsAdapter
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.openBottomSheet.observe(viewLifecycleOwner, ::showDialog)
    }

    override fun showDialog(fragment: DialogFragment) {
        fragment.show(childFragmentManager, null)
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
        val hasRights = checkRightsToEditCalendar()
        calendarDayBinder = CalendarDayBinder(map, requireContext(),
            { events, time ->
                val bottomDialog = BottomCalendarDialogFragment.newInstance(events, time, hasRights)
                viewModel.openBottomFragment(bottomDialog)
            },
            { time ->
                if (hasRights)
                    viewModel.openBottomFragment(AskAddEventDialogFragment.newInstance(time))
            }
        )
        calendarView.dayBinder = calendarDayBinder
    }

    private fun checkRightsToEditCalendar(): Boolean {
        val accountType = settings.getString(UserField.ACCOUNT_TYPE.preferencesKey, null)
        return accountType != null && accountType == AccountType.MODERATOR.title
    }

    companion object {
        private const val TAG = "MainFragment"
        fun newInstance(): MainFragment {
            val args = Bundle()

            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
package com.kandyba.mygeneration.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.marginTop
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.presentation.adapters.EventAdapter
import kotlinx.android.synthetic.main.bottom_calendar_fragment.*


class BottomCalendarDialogFragment : BottomSheetDialogFragment() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventAdapter

    private var eventsList: ArrayList<Event> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.bottom_calendar_fragment, container, false)
        eventsRecyclerView = root.findViewById(R.id.events_list)
        eventsList = arguments?.getParcelableArrayList<Event>(EVENT_LIST) as ArrayList<Event>
        eventsAdapter = EventAdapter(eventsList)
        eventsRecyclerView.adapter = eventsAdapter

        dialog?.setOnShowListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(root.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            val div = root.findViewById<ImageView>(R.id.stick)
            bottomSheetBehavior.peekHeight = eventsRecyclerView[0].height + div.height
        }
        return root
    }

    private fun solveNotRoundedCornersProblem() {
        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    val newMaterialShapeDrawable = createMaterialShapeDrawable(bottomSheet)
                    bottomSheet.background = newMaterialShapeDrawable
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun createMaterialShapeDrawable(@NonNull bottomSheet: View): MaterialShapeDrawable {
        val shapeAppearanceModel =
            //Create a ShapeAppearanceModel with the same shapeAppearanceOverlay used in the style
            ShapeAppearanceModel.builder(context, 0, R.style.MyShapeAppearance)
                .build()

        //Create a new MaterialShapeDrawable (you can't use the original MaterialShapeDrawable in the BottoSheet)
        val currentMaterialShapeDrawable = bottomSheet.background as MaterialShapeDrawable
        val newMaterialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        //Copy the attributes in the new MaterialShapeDrawable
        newMaterialShapeDrawable.initializeElevationOverlay(context)
        newMaterialShapeDrawable.fillColor = currentMaterialShapeDrawable.fillColor
        newMaterialShapeDrawable.tintList = currentMaterialShapeDrawable.tintList
        newMaterialShapeDrawable.elevation = currentMaterialShapeDrawable.elevation
        newMaterialShapeDrawable.strokeWidth = currentMaterialShapeDrawable.strokeWidth
        newMaterialShapeDrawable.strokeColor = currentMaterialShapeDrawable.strokeColor
        return newMaterialShapeDrawable
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
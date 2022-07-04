package com.kandyba.mygeneration.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.di.AppComponent
import com.kandyba.mygeneration.presentation.viewmodel.BaseViewModel
import com.kandyba.mygeneration.presentation.viewmodel.ViewModelFactory

abstract class BaseBottomSheetFragment<T : BaseViewModel>(private val layoutId: Int) :
    BottomSheetDialogFragment() {

    protected abstract val viewModelClass: Class<T>

    protected abstract val viewModelFactory: ViewModelFactory<T>

    protected val appComponent: AppComponent by lazy {
        (requireActivity().application as App).appComponent
    }

    protected val viewModel: T by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(viewModelClass)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)
        .apply(::initFields)
        .also {
            solveNotRoundedCornersProblem()
            observeViewModel()
        }

    protected open fun initFields(root: View) {}

    protected open fun observeViewModel() {}

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
}
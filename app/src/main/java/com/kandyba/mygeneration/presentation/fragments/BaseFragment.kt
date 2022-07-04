package com.kandyba.mygeneration.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.di.AppComponent
import com.kandyba.mygeneration.presentation.viewmodel.BaseViewModel
import com.kandyba.mygeneration.presentation.viewmodel.ViewModelFactory

abstract class BaseFragment<T : BaseViewModel>(private val layoutId: Int) : Fragment() {

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
        .also { observeViewModel() }

    protected open fun initFields(root: View) {}

    protected open fun observeViewModel() {}

    protected open fun showDialog(fragment: DialogFragment) {}
}
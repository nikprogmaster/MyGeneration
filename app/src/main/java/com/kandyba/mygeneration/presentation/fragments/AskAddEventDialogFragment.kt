package com.kandyba.mygeneration.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kandyba.mygeneration.App
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.presentation.viewmodel.MainFragmentViewModel


class AskAddEventDialogFragment : DialogFragment() {

    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyDialogStyle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val component = (requireActivity().application as App).appComponent
        viewModel =
            ViewModelProvider(requireActivity(), component.getMainFragmentViewModelFactory())
                .get(MainFragmentViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.add_event_ask_dialog, container, false)
        addButton = root.findViewById(R.id.add)
        cancelButton = root.findViewById(R.id.cancel)
        addButton.setOnClickListener {
            viewModel.openBottomFragment(AddEventBottomSheetFragment.newInstance())
            dismiss()
        }
        cancelButton.setOnClickListener { dismiss() }
        return root
    }

    companion object {
        fun newInstance() = AskAddEventDialogFragment()
    }
}
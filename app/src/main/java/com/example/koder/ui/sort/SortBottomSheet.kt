package com.example.koder.ui.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.koder.R
import com.example.koder.databinding.FragmentSortBottomsheetBinding
import com.example.koder.ui.main.ALPHABET_SORT_KEY
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val CHECKED_BUNDLE_KEY = "CHECKED_BUNDLE_KEY"

class SortBottomSheet : BottomSheetDialogFragment() {
    private val binding by viewBinding(FragmentSortBottomsheetBinding::bind)

    private lateinit var listener: OnActionCompleteListener

    fun setOnActionCompleteListener(listener: OnActionCompleteListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sort_bottomsheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val checkedPosition = arguments?.getInt(CHECKED_BUNDLE_KEY) ?: ALPHABET_SORT_KEY
        setInitialSortValue(checkedPosition)

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedRadioButtonId ->
            val checkedRadioButton = radioGroup.findViewById(checkedRadioButtonId) as? RadioButton
                ?: return@setOnCheckedChangeListener
            listener.onActionComplete(radioGroup.indexOfChild(checkedRadioButton))
            dismiss()
        }
    }

    private fun setInitialSortValue(checkedPosition: Int) {
        val savedCheckedRadioButton = binding.radioGroup.getChildAt(checkedPosition) as? RadioButton
        savedCheckedRadioButton?.isChecked = true
    }

    interface OnActionCompleteListener {
        fun onActionComplete(checkedPosition: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(checkedPosition: Int): SortBottomSheet {
            val fragment = SortBottomSheet()
            val args = Bundle()
            args.putInt(CHECKED_BUNDLE_KEY, checkedPosition)
            fragment.arguments = args
            return fragment
        }
    }
}


package com.example.koder.ui.error

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.koder.R
import com.example.koder.databinding.FragmentErrorBinding
import com.example.koder.ui.IMainActivity

class ErrorFragment : Fragment(R.layout.fragment_error) {
    private val binding by viewBinding(FragmentErrorBinding::bind)
    private lateinit var iMainActivity: IMainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IMainActivity) {
            iMainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAction.setOnClickListener {
            iMainActivity.moveToMainFragment()
        }
    }
}
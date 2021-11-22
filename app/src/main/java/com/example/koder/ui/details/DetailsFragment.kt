package com.example.koder.ui.details

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.koder.R
import com.example.koder.databinding.FragmentDetailsBinding
import com.example.koder.domain.EmployeeDomainModel
import com.example.koder.extensions.callPhone
import com.example.koder.extensions.setCircleImage
import com.example.koder.extensions.toAgeString
import com.example.koder.extensions.toDetailsUiFormat
import com.example.koder.ui.IMainActivity
import java.time.LocalDate

private const val EMPLOYEE_BUNDLE_KEY = "EMPLOYEE_BUNDLE_KEY"

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private lateinit var iMainActivity: IMainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IMainActivity) {
            iMainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeeDomainModel =
            arguments?.getParcelable<EmployeeDomainModel>(EMPLOYEE_BUNDLE_KEY) ?: return

        with(binding) {
            ibBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            ivAvatar.setCircleImage(employeeDomainModel.avatarUrl, R.drawable.ic_goose)
            val title = "${employeeDomainModel.firstName} ${employeeDomainModel.lastName}"
            tvTitle.text = title
            tvUserTag.text = employeeDomainModel.userTag
            tvSubtitle.text = employeeDomainModel.position
            tvBirthday.text = employeeDomainModel.birthdayLocalDate.toDetailsUiFormat()
            val age = LocalDate.now().year - employeeDomainModel.birthdayLocalDate.year
            val ageString = age.toAgeString()
            tvAge.text = ageString
            tvPhone.text = employeeDomainModel.phone

            tvPhone.setOnClickListener {
                requireContext().callPhone(tvPhone.text.toString())
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(employeeDomainModel: EmployeeDomainModel) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EMPLOYEE_BUNDLE_KEY, employeeDomainModel)
                }
            }
    }
}
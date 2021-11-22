package com.example.koder.ui

import com.example.koder.domain.EmployeeDomainModel

interface IMainActivity {
    fun moveToErrorFragment()
    fun moveToDetailsFragment(employeeDomainModel: EmployeeDomainModel)
    fun moveToMainFragment()
}
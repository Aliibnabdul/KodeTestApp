package com.example.koder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.koder.databinding.ActivityMainBinding
import com.example.koder.domain.EmployeeDomainModel
import com.example.koder.ui.IMainActivity
import com.example.koder.ui.details.DetailsFragment
import com.example.koder.ui.error.ErrorFragment
import com.example.koder.ui.main.MainFragment

class MainActivity : AppCompatActivity(), IMainActivity {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            moveToMainFragment()
        }
    }

    override fun moveToMainFragment() {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, MainFragment())
                setReorderingAllowed(true)
                commit()
            }
    }

    override fun moveToDetailsFragment(employeeDomainModel: EmployeeDomainModel) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, DetailsFragment.newInstance(employeeDomainModel))
                setReorderingAllowed(true)
                addToBackStack(null)
                commit()
            }
    }

    override fun moveToErrorFragment() {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, ErrorFragment())
                setReorderingAllowed(true)
                commit()
            }
    }
}
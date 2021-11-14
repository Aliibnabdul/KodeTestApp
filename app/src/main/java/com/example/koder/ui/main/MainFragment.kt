package com.example.koder.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.koder.R
import com.example.koder.databinding.FragmentMainBinding
import com.example.koder.domain.EmployeeUiModel
import com.example.koder.extensions.afterTextChanged
import com.example.koder.ui.IMainActivity
import com.example.koder.ui.sort.SortBottomSheet
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainFragmentVM by viewModel()
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val adapter = MainAdapter(::onAdapterItemClick)
    private lateinit var iMainActivity: IMainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IMainActivity) {
            iMainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefreshLayout()

        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.keys.collectLatest {
                setupTabLayout(it)
            }
        }

        lifecycleScope.launch {
            viewModel.filteredList.collectLatest {
                adapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collectLatest(::showLoading)
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest {
                moveToErrorFragment()
            }
        }

        binding.ivSort.setOnClickListener {
            showSortBottomSheet()
        }

        binding.searchInput.afterTextChanged(viewModel::onNewQuery)
    }

    private fun setupTabLayout(list: List<String>) {
        val tabLayout = binding.tabLayout
        list.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.setSelectedTab(tab?.text.toString())
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

        })
        viewModel.setSelectedTab(list.getOrNull(tabLayout.selectedTabPosition))
    }

    private fun onAdapterItemClick(employeeUiModel: EmployeeUiModel) {
    }

    private fun setupRefreshLayout() {
        binding.srlRefresh.apply {
            setOnRefreshListener { refreshJobItems() }
        }
    }

    private fun refreshJobItems() {
        viewModel.fetchEmployees()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.srlRefresh.isRefreshing = isLoading
    }

    private fun moveToErrorFragment() {
        iMainActivity.moveToErrorFragment()
    }

    private fun showSortBottomSheet(){
        val sortBottomSheet = SortBottomSheet.newInstance(viewModel.sortValue.value)
        val listener = object: SortBottomSheet.OnActionCompleteListener {
            override fun onActionComplete(checkedPosition: Int) {
                viewModel.setSortValue(checkedPosition)
            }
        }
        sortBottomSheet.setOnActionCompleteListener(listener)
        sortBottomSheet.show(parentFragmentManager, sortBottomSheet.tag)
    }
}
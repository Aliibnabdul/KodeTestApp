package com.example.koder.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.koder.R
import com.example.koder.databinding.FragmentMainBinding
import com.example.koder.domain.EmployeeDomainModel
import com.example.koder.extensions.afterTextChanged
import com.example.koder.extensions.getSnackBar
import com.example.koder.extensions.hideKeyboard
import com.example.koder.extensions.showSnackBar
import com.example.koder.ui.IMainActivity
import com.example.koder.ui.sort.SortBottomSheet
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainFragmentVM by viewModel()
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val mainAdapter = MainAdapter(::onAdapterItemClick)
    private lateinit var iMainActivity: IMainActivity
    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IMainActivity) {
            iMainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayout()

        binding.recyclerView.adapter = mainAdapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    recyclerView.hideKeyboard()
                }
            }
        })

        lifecycleScope.launch {
            viewModel.keys.collectLatest {
                setupTabLayout(it)
            }
        }

        lifecycleScope.launch {
            viewModel.filteredUiModel.collectLatest {
                binding.recyclerView.isVisible = it.isNotEmpty()
                mainAdapter.submitList(it)
                binding.layoutEmptyContainer.isVisible =
                    viewModel.isSearchQueryNotEmpty.value && it.isEmpty()
            }
        }

        lifecycleScope.launch {
            viewModel.sortValue.collectLatest {
                mainAdapter.isSortingByBirthday = it == BIRTHDAY_SORT_KEY
                binding.ivSort.isActivated = it == BIRTHDAY_SORT_KEY
            }
        }

        lifecycleScope.launch {
            viewModel.isSearchQueryNotEmpty.collectLatest {
                binding.ivClose.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collectLatest(::showLoading)
        }

        lifecycleScope.launch {
            viewModel.fatalError.collectLatest {
                moveToErrorFragment()
            }
        }

        lifecycleScope.launch {
            viewModel.errorSnackBar.collectLatest {
                requireContext().showSnackBar(binding.root, getString(it.message()))
            }
        }

        binding.ivSort.setOnClickListener {
            showSortBottomSheet()
        }

        binding.ivClose.setOnClickListener {
            binding.searchInput.setText("")
        }

        binding.searchInput.afterTextChanged(viewModel::onNewQuery)
        binding.searchInput.setOnFocusChangeListener { _, hasFocus ->
            binding.buttonCancelSearch.isVisible = hasFocus
        }

        binding.buttonCancelSearch.setOnClickListener {
            binding.searchInput.setText("")
            binding.searchInput.clearFocus()
            it.hideKeyboard()
        }
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

    private fun onAdapterItemClick(employeeDomainModel: EmployeeDomainModel) {
        iMainActivity.moveToDetailsFragment(employeeDomainModel)
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
        if (isLoading) {
            snackBar =
                requireContext().getSnackBar(binding.root, getString(R.string.loadingMessage))
        } else snackBar?.dismiss()
    }

    private fun moveToErrorFragment() {
        iMainActivity.moveToErrorFragment()
    }

    private fun showSortBottomSheet() {
        val sortBottomSheet = SortBottomSheet.newInstance(viewModel.sortValue.value)
        val listener = object : SortBottomSheet.OnActionCompleteListener {
            override fun onActionComplete(checkedPosition: Int) {
                viewModel.setSortValue(checkedPosition)
            }
        }
        sortBottomSheet.setOnActionCompleteListener(listener)
        sortBottomSheet.show(parentFragmentManager, sortBottomSheet.tag)
    }

    override fun onDestroy() {
        super.onDestroy()
        snackBar?.dismiss()
        snackBar = null
    }
}
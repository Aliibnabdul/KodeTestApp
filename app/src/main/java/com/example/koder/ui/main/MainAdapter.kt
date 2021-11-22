package com.example.koder.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koder.R
import com.example.koder.databinding.SeparatorViewItemBinding
import com.example.koder.databinding.ViewHolderItemBinding
import com.example.koder.domain.EmployeeDomainModel
import com.example.koder.domain.UiModel
import com.example.koder.extensions.setCircleImage

class MainAdapter(private val clickListener: (EmployeeDomainModel) -> Unit) :
    ListAdapter<UiModel, RecyclerView.ViewHolder>(DiffCallback()) {

    var isSortingByBirthday = false

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.EmployeeItem -> R.layout.view_holder_item
            is UiModel.SeparatorItem -> R.layout.separator_view_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.view_holder_item) {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewHolderItemBinding.inflate(inflater, parent, false)
            MainViewHolder(binding, clickListener)
        } else {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SeparatorViewItemBinding.inflate(inflater, parent, false)
            SeparatorViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is UiModel.EmployeeItem -> (holder as MainViewHolder).bind(uiModel.employee)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel.description)
            }
        }
    }
}

class MainViewHolder(
    binding: ViewHolderItemBinding,
    clickListener: (EmployeeDomainModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private val ivAvatar = binding.ivAvatar
    private val tvTitle = binding.tvTitle
    private val tvUserTag = binding.tvUserTag
    private val tvSubtitle = binding.tvSubtitle
    private val tvBirthday = binding.tvBirthday

    var item: EmployeeDomainModel? = null

    init {
        binding.root.setOnClickListener {
            item?.let { clickListener(it) }
        }
    }

    fun bind(employeeDomainModel: EmployeeDomainModel) {
        this.item = employeeDomainModel
        val title = "${employeeDomainModel.firstName} ${employeeDomainModel.lastName}"
        tvTitle.text = title
        tvUserTag.text = employeeDomainModel.userTag
        tvSubtitle.text = employeeDomainModel.position
        tvBirthday.text = employeeDomainModel.birthday
        ivAvatar.setCircleImage(employeeDomainModel.avatarUrl, R.drawable.ic_goose)
    }
}

class SeparatorViewHolder(
    binding: SeparatorViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val description: TextView = binding.tvYear

    fun bind(separatorText: String) {
        description.text = separatorText
    }
}

class DiffCallback : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(old: UiModel, new: UiModel): Boolean {
        return (old is UiModel.EmployeeItem && new is UiModel.EmployeeItem &&
                old.employee.id == new.employee.id) ||
                (old is UiModel.SeparatorItem && new is UiModel.SeparatorItem &&
                        old.description == new.description)

    }

    override fun areContentsTheSame(old: UiModel, new: UiModel): Boolean = old == new
}
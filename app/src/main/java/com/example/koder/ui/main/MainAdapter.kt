package com.example.koder.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.koder.R
import com.example.koder.databinding.ViewHolderItemBinding
import com.example.koder.domain.EmployeeUiModel

private const val CROSSFADE_DURATION = 500
private val glideCircleTransformOption = RequestOptions().apply(RequestOptions.circleCropTransform())

class MainAdapter(private val clickListener: (EmployeeUiModel) -> Unit) : ListAdapter<EmployeeUiModel, MainViewHolder>(DiffCallback()){

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = getItem(position)
        holder.item = item
        val title = "${item.firstName} ${item.lastName}"
        holder.tvTitle.text = title
        holder.tvSubtitle.text = item.department

        Glide.with(holder.ivAvatar)
            .load(item.avatarUrl)
            .apply(glideCircleTransformOption)
            .placeholder(R.drawable.ic_goose)
            .transition(DrawableTransitionOptions.withCrossFade(CROSSFADE_DURATION))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ivAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding, clickListener)
    }
}

class MainViewHolder(
    binding: ViewHolderItemBinding,
    clickListener: (EmployeeUiModel) -> Unit
): RecyclerView.ViewHolder(binding.root){
    val ivAvatar = binding.ivAvatar
    val tvTitle = binding.tvTitle
    val tvSubtitle = binding.tvSubtitle
    var item: EmployeeUiModel? = null

    init {
        binding.root.setOnClickListener {
            item?.let { clickListener(it) }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<EmployeeUiModel>() {
    override fun areItemsTheSame(old: EmployeeUiModel, new: EmployeeUiModel): Boolean = old.id == new.id
    override fun areContentsTheSame(old: EmployeeUiModel, new: EmployeeUiModel): Boolean = old == new
}
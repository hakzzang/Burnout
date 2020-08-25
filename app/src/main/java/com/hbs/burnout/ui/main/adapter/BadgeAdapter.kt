package com.hbs.burnout.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hbs.burnout.databinding.ItemBadgeBinding
import com.hbs.burnout.model.Stage
import com.hbs.burnout.model.StageProgress
import com.hbs.burnout.utils.script.MissionHelper


class BadgeAdapter(private val clickCallBack: (Boolean) -> (Unit)) :
    ListAdapter<Stage, BadgeAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<Stage>() {
        override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean =
            oldItem.round == newItem.round

        override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean =
            oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    inner class ViewHolder(private val binding: ItemBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(index: Int, stage: Stage) {
            binding.ivBadge.setImageResource(MissionHelper.getMyBadge(index, stage))
            binding.root.setOnClickListener(makeRootClickListener(stage.progress))
        }

        private fun makeRootClickListener(progress:Int) = View.OnClickListener { clickCallBack(progress != StageProgress.NOT_COMPLETED) }
    }
}
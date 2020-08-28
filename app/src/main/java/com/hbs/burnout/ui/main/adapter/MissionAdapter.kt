package com.hbs.burnout.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hbs.burnout.databinding.ItemMissionBinding
import com.hbs.burnout.model.Stage
import com.hbs.burnout.model.StageProgress
import com.hbs.burnout.utils.script.MissionHelper

class MissionAdapter(private val successCallback: (View, Int) -> (Unit), private val failCallback:(Boolean)->Unit) :
    ListAdapter<Stage, MissionAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Stage>() {
        override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean =
            oldItem.round == newItem.round && oldItem.progress == newItem.progress

    }) {
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).round.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemMissionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMissionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, stage: Stage) {
            binding.tvMissionTitle.text = stage.title
            binding.tvMissionContent.text = stage.content
            binding.ivMissionBadge.setImageResource(MissionHelper.getBadge(stage.round))
            if(position >= 3){
                binding.ivNotCompletedImage.visibility = View.VISIBLE
                binding.root.setOnClickListener(makeSuccessClickListener(position))
                return
            }
            when (stage.progress) {
                StageProgress.PLAYING -> {
                    binding.ivNotCompletedImage.visibility = View.GONE
                    binding.root.setOnClickListener(makeSuccessClickListener(position))
                }
                StageProgress.COMPLETED -> {
                    binding.ivNotCompletedImage.visibility = View.GONE
                    binding.root.setOnClickListener(makeSuccessClickListener(position))
                }
                StageProgress.NOT_COMPLETED -> {
                    binding.ivNotCompletedImage.visibility = View.VISIBLE
                    binding.root.setOnClickListener(makeFailClickListener(stage.progress))
                }
                else -> {
                    binding.ivNotCompletedImage.visibility = View.VISIBLE
                    binding.root.setOnClickListener(makeFailClickListener(stage.progress))
                }
            }
        }

        private fun makeSuccessClickListener(position: Int) = View.OnClickListener { successCallback(it, position) }
        private fun makeFailClickListener(progress:Int) = View.OnClickListener { failCallback(progress != StageProgress.NOT_COMPLETED) }

    }
}
package com.hbs.burnout.ui.share

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hbs.burnout.R
import com.hbs.burnout.databinding.ItemShareProgressBinding
import com.hbs.burnout.model.ShareResult

class ProgressAdapter : ListAdapter<ShareResult.Result, ProgressAdapter.ViewHolder>(object : DiffUtil.ItemCallback<ShareResult.Result>() {

    override fun areItemsTheSame(oldItem: ShareResult.Result, newItem: ShareResult.Result): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: ShareResult.Result, newItem: ShareResult.Result): Boolean = oldItem.title == newItem.title }) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    val colors = listOf(R.color.color_progress_0, R.color.color_progress_1, R.color.color_progress_2)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemShareProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    inner class ViewHolder(private val binding: ItemShareProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.data = getItem(position)

            val color: Int = ContextCompat.getColor(binding.root.context, colors.get(position))

            // ProgressBar 색상 변경
            val drawable: LayerDrawable = binding.progressBar.progressDrawable as LayerDrawable
            val progressDrawable = drawable.getDrawable(1)

            progressDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)

            // Point View 색상 변경
            val pointDrawable: GradientDrawable = binding.pointColor.background as GradientDrawable
            pointDrawable.setColor(color)
        }
    }
}
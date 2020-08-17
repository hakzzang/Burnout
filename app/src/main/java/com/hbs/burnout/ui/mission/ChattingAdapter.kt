package com.hbs.burnout.ui.mission

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hbs.burnout.R
import com.hbs.burnout.databinding.ItemMyChattingBinding
import com.hbs.burnout.databinding.ItemYourChattingBinding
import com.hbs.burnout.model.Chatting

class ChattingAdapter : ListAdapter<Chatting, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Chatting>() {
    override fun areItemsTheSame(oldItem: Chatting, newItem: Chatting): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: Chatting, newItem: Chatting): Boolean = oldItem.user == newItem.user && oldItem.message == newItem.message
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0){
            YourChattingViewHolder(ItemYourChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            MyChattingViewHolder(ItemMyChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is YourChattingViewHolder){
            holder.bind(position)
        }else if(holder is MyChattingViewHolder){
            holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).user
    }

    inner class YourChattingViewHolder(private val binding: ItemYourChattingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.ivProfile.setImageResource(R.drawable.shrimp)
            binding.tvChatting.text = getItem(position).message
        }
    }

    inner class MyChattingViewHolder(private val binding: ItemMyChattingBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.ivProfile.setImageResource(R.drawable.gamjatwigim)
            binding.tvChatting.text = getItem(position).message
        }
    }
}
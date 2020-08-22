package com.hbs.burnout.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hbs.burnout.R
import com.hbs.burnout.databinding.ItemMyChattingBinding
import com.hbs.burnout.databinding.ItemYourChattingBinding
import com.hbs.burnout.model.Script
import com.hbs.burnout.model.EventType

class ChattingAdapter : ListAdapter<Script, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Script>() {
    override fun areItemsTheSame(oldItem: Script, newItem: Script): Boolean = oldItem.message == newItem.message
    override fun areContentsTheSame(oldItem: Script, newItem: Script): Boolean = oldItem.user == newItem.user && oldItem.message == newItem.message
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
            val chatting = getItem(position).parse()
            binding.ivProfile.setImageResource(R.drawable.gamjatwigim)
            if(chatting.eventType == EventType.QUESTION){
                binding.tvChatting.text = ""
                binding.lottieViewWait.visibility = View.VISIBLE
            }
            else if(chatting.eventType == EventType.CHATTING){
                binding.lottieViewWait.visibility = View.GONE
                binding.tvChatting.text = getItem(position).message
            }
        }
    }
}
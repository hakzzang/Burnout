package com.hbs.burnout.ui.chat

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hbs.burnout.R
import com.hbs.burnout.databinding.ItemLastChattingBinding
import com.hbs.burnout.databinding.ItemMyChattingBinding
import com.hbs.burnout.databinding.ItemYourChattingBinding
import com.hbs.burnout.model.Script
import com.hbs.burnout.model.EventType
import com.hbs.burnout.utils.FileUtils

class ChattingAdapter : ListAdapter<Script, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Script>() {
    override fun areItemsTheSame(oldItem: Script, newItem: Script): Boolean = oldItem.message == newItem.message
    override fun areContentsTheSame(oldItem: Script, newItem: Script): Boolean = oldItem.user == newItem.user && oldItem.message == newItem.message
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0){
            YourChattingViewHolder(ItemYourChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else if(viewType == 1){
            MyChattingViewHolder(ItemMyChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            LastChattingViewHolder(ItemLastChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is YourChattingViewHolder -> {
                holder.bind(position)
            }
            is MyChattingViewHolder -> {
                holder.bind(position)
            }
            is LastChattingViewHolder -> {
                holder.bind(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).user
    }

    private inner class LastChattingViewHolder(private val binding: ItemLastChattingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.tvChatting.text = "미션을 클리어했습니다 \uD83C\uDF89"
        }
    }

    private inner class YourChattingViewHolder(private val binding: ItemYourChattingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.ivProfile.setImageResource(R.drawable.shrimp)
            binding.tvChatting.text = getItem(position).message
        }
    }

    private inner class MyChattingViewHolder(private val binding: ItemMyChattingBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val chatting = getItem(position).parse()
            binding.ivProfile.setImageResource(R.drawable.gamjatwigim)
            when (chatting.eventType) {
                EventType.QUESTION -> {
                    binding.tvChatting.text = ""
                    binding.lottieViewWait.visibility = View.VISIBLE
                    binding.ivChattingRecognizeImage.visibility = View.GONE
                }
                EventType.CHATTING -> {
                    binding.lottieViewWait.visibility = View.GONE
                    binding.tvChatting.text = getItem(position).message
                    binding.ivChattingRecognizeImage.visibility = View.GONE
                }
                EventType.CAMERA -> {
                    binding.tvChatting.text = ""
                    binding.lottieViewWait.visibility = View.VISIBLE
                    binding.ivChattingRecognizeImage.visibility = View.GONE
                }
                EventType.CAMERA_RESULT -> {
                    binding.tvChatting.text = getItem(position).message
                    binding.lottieViewWait.visibility = View.GONE
                    binding.ivChattingRecognizeImage.visibility = View.VISIBLE
                    val fileUri = FileUtils.getOrMakeRecognizeFile(binding.root.context).toUri()
                    binding.ivChattingRecognizeImage.setImageURI(fileUri)
                }
                EventType.DRAWING -> {
                    binding.tvChatting.text = ""
                    binding.lottieViewWait.visibility = View.VISIBLE
                    binding.ivChattingRecognizeImage.visibility = View.GONE
                }
                EventType.DRAWING_RESULT -> {
                    binding.tvChatting.text = getItem(position).message
                    binding.lottieViewWait.visibility = View.GONE
                    binding.ivChattingRecognizeImage.visibility = View.VISIBLE
                    val fileUri = FileUtils.getOrMakeRecognizeFile(binding.root.context).toUri()
                    binding.ivChattingRecognizeImage.setImageURI(fileUri)
                }
            }
        }
    }
}
package com.hbs.burnout.ui.chat

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import com.hbs.burnout.R
import com.hbs.burnout.core.BaseFragment
import com.hbs.burnout.databinding.FragmentCompletedStageBinding
import com.hbs.burnout.utils.script.MissionHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedStageFragment : BaseFragment<FragmentCompletedStageBinding>(){
    override fun isUseTransition(): Boolean = true

    override fun bindBinding(): FragmentCompletedStageBinding = FragmentCompletedStageBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var stageNumber = arguments?.getInt("stageNumber")?:0
        if(stageNumber==-1){
            stageNumber = 0
        }
        binding.tvBadgeContent.text = setBadgeContentTextView(stageNumber)
        binding.ivBadge.setImageResource(MissionHelper.getBadge(stageNumber))
    }

    private fun setBadgeContentTextView(completedStage:Int): SpannableString {
        val spannableString = SpannableString("당신은 ${completedStage}번째 미션으로\n아래의 뱃지를 획득했습니다")
        if (completedStage < 10) {
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
                4,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
                4, 6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
    }
}
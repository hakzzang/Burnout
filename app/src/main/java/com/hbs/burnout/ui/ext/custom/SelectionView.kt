package com.hbs.burnout.ui.ext.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.hbs.burnout.databinding.ViewSelectionBinding

class SelectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val viewSelectionBinding = ViewSelectionBinding.inflate(LayoutInflater.from(context), this, true)

    fun setButtonAnswer(answer: HashMap<Int, String>) {
        viewSelectionBinding.buttonAnswer1.text = answer[0]
        viewSelectionBinding.buttonAnswer2.text = answer[1]

        if (answer.size == 3) {
            viewSelectionBinding.buttonAnswer3.visibility = View.VISIBLE
            viewSelectionBinding.buttonAnswer3.text = answer[2]
        }else{
            viewSelectionBinding.buttonAnswer3.visibility = View.GONE
        }
    }

    fun setOnAnswerCallback(answerCallback:(Int)->Unit){
        viewSelectionBinding.buttonAnswer1.setOnClickListener { answerCallback(0) }
        viewSelectionBinding.buttonAnswer2.setOnClickListener { answerCallback(1) }
        viewSelectionBinding.buttonAnswer3.setOnClickListener { answerCallback(2) }
    }
}
package com.chloedewyes.walkmydog.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chloedewyes.walkmydog.R

class CustomBottomButton(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var customText: TextView

    init {
        val v = View.inflate(context, R.layout.custom_bottom_btn, this)
        customText = v.findViewById(R.id.customText)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomBottomButton,
            0,0
        ).apply {
            try {
                setText(getString(R.styleable.CustomBottomButton_customText))
                setTextColor(getColor(R.styleable.CustomBottomButton_customTextColor, 0))
            } finally {
                recycle()
            }

        }
    }

    fun setText(text: String?) {
        customText.text = text
        onRefresh()
    }

    fun setTextColor(color: Int){
        customText.setTextColor(color)
        onRefresh()
    }

    private fun onRefresh() {
        invalidate()
        requestLayout()
    }
}



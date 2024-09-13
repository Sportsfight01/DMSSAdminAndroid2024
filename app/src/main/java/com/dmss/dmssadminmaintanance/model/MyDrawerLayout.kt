package com.dmss.dmssadminmaintanance.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class MyDrawerLayout @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawerLayout(ctx, attrs, defStyleAttr) {
    var isSwipeOpenEnabled: Boolean = true

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isSwipeOpenEnabled && !isDrawerVisible(Gravity.LEFT)) {
            return false
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!isSwipeOpenEnabled && !isDrawerVisible(Gravity.LEFT)) {
            return false
        }
        return super.onTouchEvent(ev)
    }
}
package com.raywenderlich.apps.backgroundprocessing.ui.main

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoSwipeViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    override fun onTouchEvent(ev: MotionEvent?): Boolean = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = false
}
package com.testapp.appfortesting.screens.navview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

class CustomDrawerLayout : DrawerLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.EXACTLY
        )
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            View.MeasureSpec.getSize(heightMeasureSpec), View.MeasureSpec.EXACTLY
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}
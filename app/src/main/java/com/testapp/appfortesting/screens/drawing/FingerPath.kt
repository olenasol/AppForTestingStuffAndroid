package com.testapp.appfortesting.screens.drawing

import android.graphics.Path


data class FingerPath(var color:Int, var isEmboss:Boolean,
                 var isBlur:Boolean,var strokeWidth:Int,var path: Path
)
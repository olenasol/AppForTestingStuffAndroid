package com.testapp.appfortesting

fun ArrayList<String>.swap(index1: Int, index2: Int) {
    if(index1 in 0..(this.size-1) && index2 in 0..(this.size-1)) {
        val tmp = this[index1] // 'this' corresponds to the list
        this[index1] = this[index2]
        this[index2] = tmp
    }
}
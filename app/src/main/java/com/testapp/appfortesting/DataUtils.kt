package com.testapp.appfortesting

class DataUtils(){
    public fun getStringItems():ArrayList<String>{
        return (Array(10,{x->"Item "+x}).toCollection(ArrayList()))
    }
}
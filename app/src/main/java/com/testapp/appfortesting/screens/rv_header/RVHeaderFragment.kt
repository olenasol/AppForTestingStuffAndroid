package com.testapp.appfortesting.screens.rv_header


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_rvheader.*

class RVHeaderFragment : Fragment() {

    companion object{
        fun newInstance() = RVHeaderFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rvheader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        rvEvents.adapter = EventsAdapter(getListOfEvents())
        rvEvents.layoutManager = linearLayoutManager
        rvEvents.addItemDecoration(HeaderItemDecoration(rvEvents, getListOfEvents(), rvEvents.adapter as HeaderItemDecoration.StickyHeaderInterface))
    }

    private fun getListOfEvents()= listOf(
        Event(-1,"Coffee break", "Bofs","No experience level"),
        Event(1,"Coffee break", "Bofs","No experience level"),
        Event(2,"Lecture about bread", "Session","Beginner"),
        Event(-1,"12/03/19", "Session","Excellent"),
        Event(3,"Future of machine learning", "Session","Excellent"),
        Event(4,"Dinner", "Bofs","No experience level"),
        Event(5,"Meet up of programmers", "Social event","No experience level"),
        Event(1,"Reading session", "Bofs","No experience level"),
        Event(-1,"Right now", "Session","Excellent"),
        Event(2,"Lecture about cars", "Session","Beginner"),
        Event(3,"Future of React Native", "Session","Excellent"),
        Event(4,"Breakfast", "Bofs","No experience level"),
        Event(-1,"Passed", "Session","Excellent"),
        Event(5,"Meet up of cooks", "Social event","No experience level")
        )
}

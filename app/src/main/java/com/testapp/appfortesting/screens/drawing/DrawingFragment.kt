package com.testapp.appfortesting.screens.drawing


import android.os.Bundle
import androidx.fragment.app.Fragment

import com.testapp.appfortesting.R
import android.util.DisplayMetrics
import kotlinx.android.synthetic.main.fragment_drawing.*
import android.view.*
import android.view.MenuInflater

class DrawingFragment : Fragment() {

    companion object{
        fun newInstance() = DrawingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawing, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        paintView.init(metrics)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.normal -> {
                paintView.normal()
                return true
            }
            R.id.emboss -> {
                paintView.emboss()
                return true
            }
            R.id.blur -> {
                paintView.blur()
                return true
            }
            R.id.clear -> {
                paintView.clear()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}

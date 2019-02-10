package com.testapp.appfortesting.screens.rvdragdrop.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.testapp.appfortesting.DataUtils
import com.testapp.appfortesting.R
import com.testapp.appfortesting.screens.rvdragdrop.TextItemAdapter
import kotlinx.android.synthetic.main.fragment_drag_swipe.*

class GridDragFragment : Fragment(){
    lateinit var touchHelper : ItemTouchHelper

    companion object {
        fun newInstance(): GridDragFragment {
            return GridDragFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drag_swipe, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRV()
    }

    protected fun initRV(){
        rvTextItems.layoutManager = GridLayoutManager(context,2)
        val textAdapter = TextItemAdapter(DataUtils().getStringItems()) {
            touchHelper.startDrag(it)
        }
        rvTextItems.adapter = textAdapter
        val itemTouchCallback = GridItemTouchHelperCallback(textAdapter)
        touchHelper = ItemTouchHelper(itemTouchCallback)
        touchHelper.attachToRecyclerView(rvTextItems)
    }
}
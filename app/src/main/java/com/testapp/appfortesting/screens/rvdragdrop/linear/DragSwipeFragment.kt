package com.testapp.appfortesting.screens.rvdragdrop.linear


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.testapp.appfortesting.DataUtils
import com.testapp.appfortesting.R
import com.testapp.appfortesting.screens.rvdragdrop.TextItemAdapter
import kotlinx.android.synthetic.main.fragment_drag_swipe.*


class DragSwipeFragment : Fragment() {

    lateinit var touchHelper : ItemTouchHelper

    companion object {
        fun newInstance(): DragSwipeFragment {
            return DragSwipeFragment()
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
        rvTextItems.layoutManager = LinearLayoutManager(context)
        val textAdapter = TextItemAdapter(DataUtils().getStringItems()) {
            touchHelper.startDrag(it)
        }
        rvTextItems.adapter = textAdapter
        val itemTouchCallback = LinearItemTouchHelperCallback(textAdapter)
        touchHelper = ItemTouchHelper(itemTouchCallback)
        touchHelper.attachToRecyclerView(rvTextItems)
        val dividerItemDecoration = DividerItemDecoration(context,
            (rvTextItems.layoutManager as LinearLayoutManager).orientation)
        rvTextItems.addItemDecoration(dividerItemDecoration)
    }
}

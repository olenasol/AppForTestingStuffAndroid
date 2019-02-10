package com.testapp.appfortesting.screens.rvdragdrop

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testapp.appfortesting.R
import com.testapp.appfortesting.swap
import kotlinx.android.synthetic.main.item_text.view.*

class TextItemAdapter(private var items:ArrayList<String>, private val onStartDragListener: (RecyclerView.ViewHolder) -> Unit)
        : RecyclerView.Adapter<TextItemAdapter.TextItemHolder>(),
    ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemHolder {
        return TextItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false))
    }
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: TextItemHolder, position: Int) {
        holder.bindView(items[position],holder)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if(fromPosition < toPosition){
            for (i:Int in fromPosition..toPosition) {
                items.swap(i,i+1)
            }
        } else{
            for (i:Int in fromPosition downTo toPosition) {
                items.swap(i,i-1)
            }
        }
        notifyItemMoved(fromPosition,toPosition)
    }

    override fun onItemDismissed(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class TextItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemTouchHelperViewHolder {
        override fun onItemSelected() {
            itemView.tvItemName.setBackgroundColor(Color.GRAY)
        }

        override fun onItemClear() {
            itemView.tvItemName.setBackgroundColor(Color.LTGRAY)
        }

        fun bindView(name:String, holder: RecyclerView.ViewHolder){
            with(itemView){
                tvItemName.text = name
                ivHandler.setOnTouchListener(object: View.OnTouchListener {
                    @SuppressLint("ClickableViewAccessibility")
                    override fun onTouch(v:View, event:MotionEvent):Boolean {
                        if ((event.actionMasked == MotionEvent.ACTION_DOWN))
                        {
                            onStartDragListener.invoke(holder)
                        }
                        return false
                    }
                })
            }
        }
    }
}
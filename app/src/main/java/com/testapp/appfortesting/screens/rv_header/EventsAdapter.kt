package com.testapp.appfortesting.screens.rv_header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.item_event.view.*



class EventsAdapter(private var items:List<Event>)
    : RecyclerView.Adapter<EventsAdapter.TextItemHolder>(), HeaderItemDecoration.StickyHeaderInterface {

    companion object{
        const val REGULAR_ITEM = 0
        const val HEADER_ITEM = 1
        const val IS_VISIBLE = "is_visible"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemHolder {
        if(viewType == REGULAR_ITEM)
            return TextItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false))
        else
            return TextItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
    }
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: TextItemHolder, position: Int) {
        holder.bindView(items[position],holder)
    }

    override fun onBindViewHolder(holder: TextItemHolder, position: Int, payloads: MutableList<Any>) {
        if ( payloads.size>0 && payloads[0] is Bundle){
            if((payloads[0] as Bundle).getBoolean(IS_VISIBLE,true)){
                holder.itemView.visibility = View.VISIBLE
            } else
                holder.itemView.visibility = View.INVISIBLE
        }
        super.onBindViewHolder(holder, position, payloads)
    }



    override fun getItemViewType(position: Int): Int {
        if (items[position].id == -1){
            return HEADER_ITEM
        } else
            return REGULAR_ITEM
    }
    override fun getHeaderPositionForItem(itemPosition: Int): Int {

        var itemPosition = itemPosition
        var headerPosition = 0
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition
                break
            }
            itemPosition -= 1
        } while (itemPosition >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.item_header
    }

    override fun bindHeaderData(header: View, headerPosition: Int) {
        val bundle = Bundle()
        bundle.putBoolean(EventsAdapter.IS_VISIBLE, true)
        for (i in 0 until items.size){
            if(items[i].id ==-1)
            if (i != headerPosition){
                notifyItemChanged(i, bundle)
            }
        }
        header.findViewById<TextView>(R.id.tvName).text = "Name: ${items[headerPosition].name}"
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return items[itemPosition].id ==-1
    }
    inner class TextItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindView(event: Event, holder: RecyclerView.ViewHolder){
            with(itemView){
                tvName.text = "Name: ${event.name}"
                if (tvExperienceLevel != null)
                    tvExperienceLevel.text = event.experienceLevel
                if (tvTrack != null)
                    tvTrack.text = event.track
            }
        }
    }
}
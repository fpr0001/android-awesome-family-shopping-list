package com.example.awesomefamilyshoppinglist.main

import android.view.View
import android.view.ViewGroup
import android.widget.HeaderViewListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Item
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class MainAdapter(val content: HashMap<Category, Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}



package com.example.awesomefamilyshoppinglist.main

import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.model.Item

interface ViewModel {
    fun layoutId(): Int
}

class ItemViewModel(val item: Item) : ViewModel {

    override fun layoutId(): Int = R.layout.item_view_holder

}
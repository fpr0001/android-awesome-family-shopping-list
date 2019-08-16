package com.example.awesomefamilyshoppinglist.main

import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.databinding.ItemSectionHeaderViewHolderBinding
import com.example.awesomefamilyshoppinglist.databinding.ItemViewHolderBinding
import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Item
import com.example.awesomefamilyshoppinglist.util.DateUtils
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem


class ItemItem(val viewModel: ItemViewModel) : AbstractItem<ItemItem.ViewHolder>() {

    override fun hashCode(): Int {
        return viewModel.model.uid.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is CategoryItem) {
            return viewModel.model.uid == other.viewModel.model.uid
        }
        return super.equals(other)
    }

    override val layoutRes: Int
        get() = R.layout.item_view_holder

    override val type: Int
        get() = R.id.id_list_item

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ItemItem>(view) {
        private val binding: ItemViewHolderBinding = DataBindingUtil.bind(view)!!

        override fun bindView(item: ItemItem, payloads: MutableList<Any>) {
            binding.viewModel = item.viewModel
        }

        override fun unbindView(item: ItemItem) {
            binding.viewModel = null
            binding.imageView.setImageDrawable(null)
            binding.textViewName.text = null
            binding.textViewUserDate.text = null
        }
    }
}

class CategoryItem(val viewModel: SectionHeaderViewModel) : AbstractItem<CategoryItem.ViewHolder>() {

    override fun hashCode(): Int {
        return viewModel.model.name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is CategoryItem) {
            return viewModel.model.name == other.viewModel.model.name
        }
        return super.equals(other)
    }

    override val layoutRes: Int
        get() = R.layout.item_section_header_view_holder

    override val type: Int
        get() = R.id.id_list_header

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<CategoryItem>(view) {
        private val binding: ItemSectionHeaderViewHolderBinding = DataBindingUtil.bind(view)!!

        override fun bindView(item: CategoryItem, payloads: MutableList<Any>) {
            binding.viewModel = item.viewModel
        }

        override fun unbindView(item: CategoryItem) {
            binding.viewModel = null
            binding.textView.text = null
        }
    }
}

class SectionHeaderViewModel(val model: Category)

class ItemViewModel(val model: Item) {
    val userAndDate = "${model.createdBy.name}\n".plus(DateUtils.toText(model.createdAt))
    val checked = model.boughtAt != null
}
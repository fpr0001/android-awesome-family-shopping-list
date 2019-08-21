package com.example.awesomefamilyshoppinglist.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.databinding.ItemCategoryViewHolderBinding
import com.example.awesomefamilyshoppinglist.databinding.ItemViewHolderBinding
import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Item
import com.example.awesomefamilyshoppinglist.util.DateUtils
import java.lang.RuntimeException

class MainAdapter : RecyclerView.Adapter<BaseViewHolder<BaseItemViewModel>>() {

    val list = ArrayList<BaseItemViewModel>()

    fun refreshItems(newItems: List<BaseItemViewModel>) {
        list.clear()
        list.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseItemViewModel> {
        return when (viewType) {
            R.id.id_list_header -> CategoryViewHolder.from(parent) as BaseViewHolder<BaseItemViewModel>
            R.id.id_list_item -> ItemViewHolder.from(parent) as BaseViewHolder<BaseItemViewModel>
            else -> throw RuntimeException("No type defined for $viewType")
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<BaseItemViewModel>, position: Int) {
        holder.bindViewModel(list[position])
    }

    override fun onViewRecycled(holder: BaseViewHolder<BaseItemViewModel>) {
        super.onViewRecycled(holder)
        holder.unbindView()
    }

    override fun getItemViewType(position: Int) = list[position].type
}

class ItemViewHolder(view: View) : BaseViewHolder<ItemViewModel>(view) {
    private val binding: ItemViewHolderBinding = DataBindingUtil.bind(view)!!

    companion object Factory {
        fun from(parent: ViewGroup): ItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_holder, parent, false)
            return ItemViewHolder(view)
        }
    }

    override fun bindViewModel(viewModel: ItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun unbindView() {
        binding.viewModel = null
        binding.imageView.setImageDrawable(null)
        binding.textViewName.text = null
        binding.textViewUserDate.text = null
    }
}

class CategoryViewHolder(view: View) : BaseViewHolder<CategoryViewModel>(view) {
    private val binding: ItemCategoryViewHolderBinding = DataBindingUtil.bind(view)!!

    companion object Factory {
        fun from(parent: ViewGroup): CategoryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_view_holder, parent, false)
            return CategoryViewHolder(view)
        }
    }

    override fun bindViewModel(viewModel: CategoryViewModel) {
        binding.viewModel = viewModel
    }

    override fun unbindView() {
        binding.viewModel = null
        binding.textView.text = null
    }
}

abstract class BaseViewHolder<in T : BaseItemViewModel>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindViewModel(viewModel: T)
    abstract fun unbindView()
}

data class CategoryViewModel(val model: Category) : BaseItemViewModel(R.id.id_list_header) {

    override fun hashCode(): Int {
        return model.uid.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CategoryViewModel
        if (model != other.model) return false
        return true
    }
}

class ItemViewModel(val model: Item) : BaseItemViewModel(R.id.id_list_item) {
    val userAndDate = "${model.createdBy.name}\n".plus(DateUtils.toText(model.createdAt))
    val checked = model.boughtAt != null
}

abstract class BaseItemViewModel(@IdRes val type: Int)
package com.example.awesomefamilyshoppinglist.main

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.databinding.ItemSectionHeaderViewHolderBinding
import com.example.awesomefamilyshoppinglist.databinding.ItemViewHolderBinding
import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Item
import com.example.awesomefamilyshoppinglist.util.DateUtils
import com.mikepenz.fastadapter.items.ModelAbstractItem

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class BaseModelAbstractItem<ViewModel, Item : ModelAbstractItem<ViewModel, Item, *>, VH : BaseViewHolder<ViewModel>>(
    model: ViewModel
) : ModelAbstractItem<ViewModel, Item, VH>(model) {

    fun getViewModel(): ViewModel = model

    override fun bindView(holder: VH, payloads: List<Any>?) {
        super.bindView(holder, payloads)
        holder.bind(getViewModel())
    }

    override fun unbindView(holder: VH) {
        super.unbindView(holder)
        holder.unbind()
    }
}

class SectionHeaderViewModel(val model: Category)

class ItemViewModel(val model: Item) {
    val userAndDate = "${model.createdBy.displayName}\n".plus(DateUtils.toText(model.createdAt))
    val checked = model.boughtAt != 0L
}

class ModelItem(viewModel: ItemViewModel) : BaseModelAbstractItem<ItemViewModel, ModelItem, ItemViewHolder>(viewModel) {

    override fun getType() = layoutRes

    override fun getViewHolder(v: View) = ItemViewHolder(v)

    override fun getLayoutRes() = R.layout.item_view_holder

}

class ModelSectionHeader(category: Category) :
    ModelAbstractItem<Category, ModelSectionHeader, SectionHeaderViewHolder>(category) {

    override fun getType() = layoutRes

    override fun getViewHolder(v: View) = SectionHeaderViewHolder(v)

    override fun getLayoutRes() = R.layout.item_section_header_view_holder

}

abstract class BaseViewHolder<ViewModel>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(viewModel: ViewModel)
    abstract fun unbind()
}

class ItemViewHolder(view: View) : BaseViewHolder<ItemViewModel>(view) {
    private val binding: ItemViewHolderBinding = DataBindingUtil.bind(view)!!

    override fun bind(viewModel: ItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun unbind() {
        binding.imageView.setImageDrawable(null)
        binding.textViewName.text = null
        binding.textViewUserDate.text = null
    }
}

class SectionHeaderViewHolder(view: View) : BaseViewHolder<SectionHeaderViewModel>(view) {
    private val binding: ItemSectionHeaderViewHolderBinding = DataBindingUtil.bind(view)!!

    override fun bind(viewModel: SectionHeaderViewModel) {
        binding.viewModel = viewModel
    }

    override fun unbind() {
        binding.textView.text = null
    }
}
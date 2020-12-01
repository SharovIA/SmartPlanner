package com.ivanasharov.smartplanner.presentation.view.adapters

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class UniversalListAdapter<TBindingType : ViewDataBinding, TModelType>(holderCreator: HolderCreator<TBindingType>,
                                                                       holderBinder: HolderBinder<TModelType, TBindingType>,
                                                                       diffCallback: DiffUtil.ItemCallback<TModelType>) : ListAdapter<TModelType, Holder<TBindingType>>(diffCallback) {

    private val mHolderCreator = holderCreator
    private val mBinder = holderBinder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<TBindingType> {
        return mHolderCreator.create(parent)
    }

    override fun onBindViewHolder(holder: Holder<TBindingType>, position: Int) {
        mBinder.bind(getItem(position), holder)
    }

}
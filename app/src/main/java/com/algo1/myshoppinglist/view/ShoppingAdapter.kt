package com.algo1.myshoppinglist.view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algo1.myshoppinglist.database.ShoppingItem
import com.algo1.myshoppinglist.databinding.ItemShoppingBinding

class ShoppingAdapter(private val onClick: (ShoppingItem) -> Unit) :
    ListAdapter<ShoppingItem, ShoppingAdapter.ShoppingViewHolder>(DiffCallback()) {

    class ShoppingViewHolder(private val binding: ItemShoppingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingItem, onClick: (ShoppingItem) -> Unit) {
            binding.tvItemName.text = item.name
            binding.tvAisleNumber.text = "Aisle - ${item.aisle} "
            binding.checkBox.isChecked = item.isChecked
            binding.checkBox.setOnClickListener {
                onClick(item.copy(isChecked = !item.isChecked))
            }
            binding.tvItemName.paintFlags =
                if (item.isChecked) Paint.STRIKE_THRU_TEXT_FLAG else 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val binding =
            ItemShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem) =
            oldItem == newItem
    }
}

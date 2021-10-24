package com.adhanjadevelopers.girl_rescue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adhanjadevelopers.girl_rescue.database.History
import com.adhanjadevelopers.girl_rescue.databinding.ContactsRowBinding
import com.adhanjadevelopers.girl_rescue.databinding.HistoryRowBinding

class HistoryAdapter():ListAdapter<History,HistoryAdapter.MyViewHolder>(DiffUtilCallBack){
    object DiffUtilCallBack :DiffUtil.ItemCallback<History>(){
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.id == newItem.id
        }

    }
    inner class  MyViewHolder(private val binding: HistoryRowBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History?) {
            binding.guardianName.text = history?.guardianName
            binding.guardianNumber.text = history?.guardianPhone
            binding.date.text = history?.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(HistoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }


}
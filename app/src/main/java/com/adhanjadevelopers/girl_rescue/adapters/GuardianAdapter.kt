package com.adhanjadevelopers.girl_rescue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.database.AddGuardian
import com.adhanjadevelopers.girl_rescue.databinding.ContactsRowBinding
import com.bumptech.glide.Glide

class GuardianAdapter(private val onClickListener: OnClickListener): ListAdapter<AddGuardian, GuardianAdapter.MyViewHolder>(DiffUtilCallback) {

    object DiffUtilCallback : DiffUtil.ItemCallback<AddGuardian>() {
        override fun areItemsTheSame(oldItem: AddGuardian, newItem: AddGuardian): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AddGuardian, newItem: AddGuardian): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class MyViewHolder(private val binding: ContactsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val edit =binding.editPen.setOnClickListener {}
        val delete = binding.delete.setOnClickListener {}

        fun bind(guardian: AddGuardian?) {
            binding.contactRow.text = guardian?.phoneNumber
            binding.nameRow.text = guardian?.name
            Glide.with(binding.imagePhoteRow)
                .load(guardian?.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imagePhoteRow);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ContactsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val guardian = getItem(position)
        holder.bind(guardian)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(guardian)
        }
    }
}
class OnClickListener(val clickListener: (guardian: AddGuardian) -> Unit) {
    fun onClick(guardian: AddGuardian) = clickListener(guardian)
}
package com.argando.habittrainer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.argando.habittrainer.databinding.SingleCardBinding

class HabitsAdapter(private val habits: List<Habit>) :
    RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {
    lateinit var binding: SingleCardBinding

    class HabitViewHolder(itemView: View, val binding: SingleCardBinding) :
        RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        binding = SingleCardBinding.inflate(LayoutInflater.from(parent.context))
        return HabitViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.binding.tvTitle.text = habits[position].title
        holder.binding.tvDescription.text = habits[position].description
        holder.binding.ivIcon.setImageBitmap(habits[position].imageRes)
    }

    override fun getItemCount() = habits.size
}
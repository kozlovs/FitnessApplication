package com.example.fitnessapplication.presentation.feature.workouts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapplication.databinding.ItemWorkoutBinding
import com.example.fitnessapplication.domain.model.workouts.Workout
import com.example.fitnessapplication.presentation.util.getIconRes
import com.example.fitnessapplication.presentation.util.minutesToDuration

class WorkoutsAdapter(
    private val onItemClick: (Workout) -> Unit
) : ListAdapter<Workout, ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding = binding,
            onItemClick = onItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ViewHolder(
    private val binding: ItemWorkoutBinding,
    private val onItemClick: (Workout) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Workout) = with(binding) {
        root.setOnClickListener { onItemClick(item) }
        title.text = item.title
        description.text = item.description
        description.isVisible = item.description != null
        duration.text = item.duration.minutesToDuration(itemView.context)
        typeIcon.setImageResource(item.type.getIconRes())
    }
}

class DiffCallback : DiffUtil.ItemCallback<Workout>() {
    override fun areItemsTheSame(oldItem: Workout, newItem: Workout) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Workout, newItem: Workout) = oldItem == newItem
}
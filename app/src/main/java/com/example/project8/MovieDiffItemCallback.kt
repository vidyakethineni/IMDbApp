package com.example.project8


import androidx.recyclerview.widget.DiffUtil


/**
 * Callback for calculating the difference between two non-null items in a list.
 * Used by the [MoviesAdapter] to efficiently update the RecyclerView.
 */
class MovieDiffItemCallback : DiffUtil.ItemCallback<OmdbMovie>() {
    /**
     * Called to check whether two items represent the same object.
     */
    override fun areItemsTheSame(oldItem: OmdbMovie, newItem: OmdbMovie)
            = (oldItem.Title == newItem.Title)
    override fun areContentsTheSame(oldItem: OmdbMovie, newItem: OmdbMovie) = (oldItem == newItem)
}
package com.example.project8

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.project8.databinding.MovieItemBinding


/**
 * Adapter for the RecyclerView in the MainActivity, responsible for displaying a list of movies.
 */
class MoviesAdapter(val onShareClicked:(OmdbMovie)->Unit,val onImageClicked:(OmdbMovie)->Unit) :
    ListAdapter<OmdbMovie, MoviesAdapter.MovieViewHolder>(MovieDiffItemCallback()) {
    /**
     * View holder class for a movie item in the RecyclerView.
     */
    class MovieViewHolder(val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OmdbMovie?) {
              binding.movie = item
              binding.executePendingBindings()
            // Load movie poster image using Glide with rounded corners.
            Glide.with(binding.root.context)
                .load(item?.Poster)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), RoundedCorners(20)
                    )
                )
                .into(binding.imageViewPoster)
        }
    }
    /**
     * Creates a new instance of [MovieViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding:MovieItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.movie_item,parent,false)
        return  MovieViewHolder(binding)
    }
    /**
     * Binds the data at the specified position to the given [MovieViewHolder].
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
       holder.bind(getItem(position))
        // Set click listeners for share and poster image.
        holder.binding.imageViewShare.setOnClickListener{
            onShareClicked(getItem(holder.adapterPosition))
        }
        holder.binding.imageViewPoster.setOnClickListener{
            onImageClicked(getItem(holder.adapterPosition))
        }
    }
}

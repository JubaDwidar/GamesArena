package com.example.Arena.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Arena.databinding.GenreItemBinding
import com.example.Arena.model.genresmodel.Genres
import com.example.Arena.ui.genres.GenresActivity

/**
 * Genre adapter that takes list of genres and adapt to display in recycler view
 */
class GenreAdapter(var onItemClick: GenresActivity) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var list: List<Genres>? = null

    private val TAG = "GenreAdapter"


    fun setList(genreList: List<Genres>) {

        this.list = genreList
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {

        val binding = GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GenreViewHolder(binding)

    }


    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {

        list?.get(position)?.let { holder.bind(it) }


    }

    override fun getItemCount(): Int {

        return 18
    }


    inner class GenreViewHolder(private val binding: GenreItemBinding)
        : RecyclerView.ViewHolder(binding.root) {


        @OptIn(ExperimentalPagingApi::class)
        @SuppressLint("ShowToast")
        fun bind(genres: Genres) {

            binding.apply {
                Glide.with(itemView)
                        .load(genres.image_background)
                        .centerCrop()
                        .into(genreImageView)

                genreName.text = genres.name
                CountGenres.text = genres.games_count.toString()

            }

            binding.root.setOnClickListener {
                Log.e(TAG, "final bind ${binding.genreName.text}")
                onItemClick.onItemClick(binding.genreName.text.toString())

            }
        }

    }


}

interface OnItemClick {
    fun onItemClick(genreName: String)
}



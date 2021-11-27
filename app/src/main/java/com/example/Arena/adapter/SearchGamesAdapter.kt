package com.example.Arena.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Arena.databinding.SearchItemBinding
import com.example.Arena.db.room.Game
import com.example.Arena.model.gamesmodel.Games

/**
 * Games adapter that takes list of games from local database and adapt to display in recycler view
 */
class SearchGamesAdapter(var searchGame: OnSearchedGameClick) : RecyclerView.Adapter<SearchGamesAdapter.SearchViewHolder>() {

    private var game: Game? = null

    private var listOfSearchedGame: List<Games>? = null

    fun setList( gamesList:List<Games>) {
this.listOfSearchedGame=gamesList

        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val bind = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(bind)


    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        listOfSearchedGame?.get(position)?.let { holder.bind(it) }

      //  game?.let { holder.bind(it) }

    }

    override fun getItemCount(): Int {

        val s=listOfSearchedGame?.indexOf(listOfSearchedGame?.last())
        return s!!

    }


    inner class SearchViewHolder(private val binding: SearchItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Games) {

            binding.apply {
                Glide.with(itemView)
                        .load(game.background_image)
                        .centerCrop()
                        .into(searchGameImageView)

                searchGameName.text = game.name
                searchGameRating.text = game.rating.toString()
                searchGameSlug.text = game.slug

                binding.root.setOnClickListener {

                    searchGame.onSearchClick(binding.searchGameSlug.text.toString())


                }

            }

        }


    }

}

interface OnSearchedGameClick {
    fun onSearchClick(searchGame: String)
}

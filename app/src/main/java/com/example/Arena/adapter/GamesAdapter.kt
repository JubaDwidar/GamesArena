package com.example.Arena.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Arena.databinding.GamesItemBinding
import com.example.Arena.db.room.Game

/**
 * Games adapter that takes paging list of games and adapt to display in recycler view
 */
class GamesAdapter(var onGameClick: OnGameClick) : PagingDataAdapter<Game, GamesAdapter.GameViewHolder>(GamesComparator()) {

    inner class GameViewHolder(private val binding: GamesItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(games: Game) {

            binding.apply {

                Glide.with(itemView)
                        .load(games.background_image)
                        .centerCrop()
                        .into(gameImageView)

                gameSlug.text = games.slug
                gameName.text = games.name
                gameRating.text = games.rating.toString()

            }
            binding.root.setOnClickListener {
                onGameClick.onItemClick(binding.gameSlug.text.toString())


            }


        }


    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = GamesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

}
interface OnGameClick {
    fun onItemClick(gameId: String)
}


class GamesComparator : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game) =
            oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Game, newItem: Game) =
            oldItem == newItem

}


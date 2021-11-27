package com.example.Arena.ui.genres

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Arena.adapter.GenreAdapter
import com.example.Arena.adapter.OnItemClick
import com.example.Arena.databinding.ActivityMapsBinding
import com.example.Arena.model.genresmodel.Genres
import com.example.Arena.ui.games.GamesActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 *Genres activity responsible for showing list of all genres on RecyclerView to select from
 */
@AndroidEntryPoint
class GenresActivity : AppCompatActivity(), OnItemClick {

    private val genreViewModel: GenreViewModel by viewModels()
    private lateinit var genreList: List<Genres>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val genreAdapter = GenreAdapter(this)


        actionBar?.title = "Fav Game"


        binding.apply {
            genreRecycler.apply {
                adapter = genreAdapter
                layoutManager = LinearLayoutManager(this@GenresActivity)

            }
            genreViewModel._genreLiveData.observe(this@GenresActivity) {
                genreList = it.results
                genreAdapter.setList(genreList)
            }
        }


    }


    @ExperimentalPagingApi
    fun moveToGamesActivity(g: String) {
        val intent = Intent(this, GamesActivity::class.java)
        intent.putExtra("genreName", g)
        startActivity(intent)


    }

    @ExperimentalPagingApi
    override fun onItemClick(genreName: String) {
        moveToGamesActivity(genreName)

    }


}
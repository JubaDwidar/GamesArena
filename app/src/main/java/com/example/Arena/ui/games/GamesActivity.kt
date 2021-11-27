package com.example.Arena.ui.games

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Arena.R
import com.example.Arena.adapter.GamesAdapter
import com.example.Arena.adapter.OnGameClick
import com.example.Arena.adapter.OnSearchedGameClick
import com.example.Arena.adapter.SearchGamesAdapter
import com.example.Arena.databinding.ActivityGamesBinding
import com.example.Arena.db.room.Game
import com.example.Arena.ui.details.GamesDetailsActivity
import com.example.Arena.ui.genres.GenresActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates


/**
 *Games activity responsible for showing list of games on RecyclerView based on its genres
 */
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class GamesActivity : AppCompatActivity(), OnGameClick, OnSearchedGameClick {

    private val gamesViewModel: GamesViewModel by viewModels()
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var prefFirst: SharedPreferences
    private var isFirstRun by Delegates.notNull<Boolean>()


    private lateinit var binding: ActivityGamesBinding
    private val TAG = "GamesActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefFirst = getSharedPreferences("isFirst", MODE_PRIVATE)
        val genresName = intent.getStringExtra("genreName")


         val isFirstRun = getSharedPreferences("PREF", MODE_PRIVATE)
                .getBoolean("ilFirst", true)

        if (isFirstRun) {
            //show start activity
            startActivity(Intent(this, GenresActivity::class.java))
            getSharedPreferences("PREF", MODE_PRIVATE).edit()
                .putBoolean("ilFirst", false).apply()

        }
        gamesAdapter = GamesAdapter(this)


        binding.apply {
            gamesRecycler.apply {
                setHasFixedSize(true)
                adapter = gamesAdapter
                layoutManager = LinearLayoutManager(this@GamesActivity)
            }
        }

        lifecycleScope.launch {
            if (genresName != null) {
                try {
                    gamesViewModel.getGamePaginated(genresName.decapitalize()).collect { pagingGameData ->
                        pagingGameData.let {
                            gamesAdapter.submitData(it)
                        }

                    }
                } catch (e: Exception) {
                    Log.e(TAG, "exception $e")
                }
            } else {


                gamesViewModel.getGamePaginated("action").collect { pagingGameData ->
                    pagingGameData.let {
                        gamesAdapter.submitData(it)
                    }
                }

            }


        }


    }

    private fun moveToGameDetailsActivity(g: String) {
        val intent = Intent(this, GamesDetailsActivity::class.java)
        intent.putExtra("gameSlug", g)
        startActivity(intent)

    }

    override fun onItemClick(gameId: String) {
        moveToGameDetailsActivity(gameId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            moveToGenresActivity()
        }

        return super.onOptionsItemSelected(item)

    }

    private fun moveToGenresActivity() {
        startActivity(Intent(this, GenresActivity::class.java))

    }


    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.gallery, menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Search a game"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                lifecycleScope.launch {

                    gamesViewModel.getSearchGameDetail(query.toString(),exact = true,precise = true)

                    gamesViewModel._searcGamesLiveData.observe(this@GamesActivity) {

                        Log.e(TAG, "onQueryTextSubmit: size of result is ${it.results.size} ${it.results[3].name} ", )

                        val searchedGame = it.results[1].let {
                            Log.e(TAG, "remote search ${it.name}",)

                            Game(it.id, it.name, it.slug, it.rating, it.background_image)
                        }


                        val searchGame = SearchGamesAdapter(this@GamesActivity)
                        searchGame.setList(it.results)
                        binding.apply {
                            gamesRecycler.apply {
                                setHasFixedSize(true)
                                adapter = searchGame
                                layoutManager = LinearLayoutManager(this@GamesActivity)
                            }
                        }

                    }


                }


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {



                return false
            }

        })
 return super.onCreateOptionsMenu(menu)
    }

 override fun onResume() {
        super.onResume()

    }

    override fun onSearchClick(searchGame: String) {
        moveToGameDetailsActivity(searchGame)
    }

}



/*


package com.example.Arena.ui.games

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Arena.R
import com.example.Arena.adapter.GamesAdapter
import com.example.Arena.adapter.OnGameClick
import com.example.Arena.adapter.OnSearchedGameClick
import com.example.Arena.adapter.SearchGamesAdapter
import com.example.Arena.databinding.ActivityGamesBinding
import com.example.Arena.db.room.Game
import com.example.Arena.ui.details.GamesDetailsActivity
import com.example.Arena.ui.genres.GenresActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates


/**
 *Games activity responsible for showing list of games on RecyclerView based on its genres
 */
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
abstract class GamesActivity : AppCompatActivity(), OnGameClick, OnSearchedGameClick {

    private val gamesViewModel: GamesViewModel by viewModels()
    private lateinit var gameList: List<Game>
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var prefs: SharedPreferences
    private lateinit var prefFirst: SharedPreferences
    private var isFirstRun by Delegates.notNull<Boolean>()

    private lateinit var binding: ActivityGamesBinding
    private val TAG = "GamesActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE)
        prefFirst = getSharedPreferences("isFirst", MODE_PRIVATE)

        val genresName = intent.getStringExtra("genreName")

        prefs.edit()?.putString("genrename", genresName)?.apply()

        isFirstRun = getSharedPreferences("PREFERENCES", MODE_PRIVATE)
                .getBoolean("FirstRun", true)
        gamesAdapter = GamesAdapter(this)


        binding.apply {
            gamesRecycler.apply {
                setHasFixedSize(true)
                adapter = gamesAdapter
                layoutManager = LinearLayoutManager(this@GamesActivity)
            }
        }

        lifecycleScope.launch {
            if (genresName != null) {
                try {
                    gamesViewModel.getGamePaginated(genresName.decapitalize()).collect { pagingGameData ->
                        pagingGameData.let {
                            gamesAdapter.submitData(it)
                        }

                    }
                } catch (e: Exception) {
                    Log.e(TAG, "exception $e")
                }
            } else {

                Log.e(TAG, "problem in geners")
                val aStringFromShared = prefs.getString("key", "")
                Log.d("TAG", aStringFromShared!!)

                Log.e(TAG, "problem in geners $aStringFromShared")



                gamesViewModel.getGamePaginated(aStringFromShared.decapitalize()).collect { pagingGameData ->
                    pagingGameData.let {
                        Log.e(TAG, "onPag $it ")

                        Log.e(TAG, "on paging ${it.map { it.name }}")
                        gamesAdapter.submitData(it)
                    }
                }

            }


        }


    }

    private fun moveToGameDetailsActivity(g: String) {
        val intent = Intent(this, GamesDetailsActivity::class.java)
        intent.putExtra("gameSlug", g)
        startActivity(intent)

    }

    override fun onItemClick(gameId: String) {
        moveToGameDetailsActivity(gameId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            moveToGenresActivity()
        }

        return super.onOptionsItemSelected(item)

    }

    private fun moveToGenresActivity() {
        startActivity(Intent(this, GenresActivity::class.java))

    }


    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.gallery, menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Search a game"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                lifecycleScope.launch {

                    gamesViewModel.getSearchGameDetail(newText.toString())

                    gamesViewModel._searcGamesLiveData.observe(this@GamesActivity) {

                        val searchedGame = it.results[1].let {
                            Log.e(TAG, "remote search ${it.name}",)

                            Game(it.id, it.name, it.slug, it.rating, it.background_image)
                        }


                        val searchGame = SearchGamesAdapter(this@GamesActivity)
                        searchGame.setList(searchedGame)
                        binding.apply {
                            gamesRecycler.apply {
                                setHasFixedSize(true)
                                adapter = searchGame
                                layoutManager = LinearLayoutManager(this@GamesActivity)
                            }
                        }

                    }


                }


                return true
            }

        })
 return super.onCreateOptionsMenu(menu)
    }

 override fun onResume() {
        super.onResume()
        if (isFirstRun) {
            //show start activity
            startActivity(Intent(this, GenresActivity::class.java))
            getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit()
                .putBoolean("FirstRun", false).apply()


        }
    }

    override fun onSearchClick(searchGame: String) {
        moveToGameDetailsActivity(searchGame)
    }

}




 */
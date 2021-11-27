package com.example.Arena.ui.details

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.Arena.databinding.ActivityGamesDetailsBinding
import com.example.Arena.model.trailermodel.Trailer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 *Games activity responsible for showing details of specific game
 */
@AndroidEntryPoint
class GamesDetailsActivity : AppCompatActivity() {


    private val TAG = "GamesDetailsActivity"
    val gamesDetailsViewModel: GamesDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGamesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameSlug = intent.getStringExtra("gameSlug")
        if (gameSlug != null) {
            lifecycleScope.launch {

                gamesDetailsViewModel.getGameDetail(gameSlug)
                gamesDetailsViewModel.getGameTrailer(gameSlug)


            }
        } else {
            Log.e(TAG, "gameslug is empty ")
        }


        val mController = MediaController(this)
        mController.setAnchorView(binding.videoPlayer)



        gamesDetailsViewModel.gameLiveData.observe(this) {
            binding.apply {
                gameName.text = it.name
                gameDesc.text = it.description.subSequence(3, 330)
                gameDesc.maxLines = 5
                it.description.removeRange(1, 3)
                gameRating.text = "Rate: ${it.rating.toString()}"
                gameRelease.text = "Released: ${it.released}"

                Glide.with(this@GamesDetailsActivity)
                        .load(it.background_image)
                        .centerCrop()
                        .into(gameImage)
            }
        }

        gamesDetailsViewModel.videoGameLiveData.observe(this) {
            if (it != null) {

               val trailo: List<Trailer> = it.results
                  val videoUrl = Uri.parse(trailo.get(0).data._480)

                Log.e(TAG, "trailer ${it.results}" , )

                binding.videoPlayer.apply {
                    setMediaController(mController)
                    setVideoURI(videoUrl)
                    requestFocus()
                    start()
                }

            } else

                Log.e(TAG, "video player Problem")


        }


    }


}
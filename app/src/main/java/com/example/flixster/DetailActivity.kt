package com.example.flixster

import android.R.attr.textColor
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.PaletteAsyncListener
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers


private const val YOUTUBE_API_KEY = "AIzaSyBfUGgKXOHNQH7WIGF67ZF1Y2MW2KUJPc0"
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=74c8086ad605652288071744d8616ec5"

class DetailActivity : YouTubeBaseActivity() {

    private lateinit var detailActivityLayout : ScrollView
    private lateinit var layout : ConstraintLayout
    private lateinit var tvTitle : TextView
    private lateinit var isAdult : TextView
    private lateinit var releaseDate : TextView
    private lateinit var tvOverview : TextView
    private lateinit var ratingBar : RatingBar
    private lateinit var ytPlayerView : YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailActivityLayout = findViewById(R.id.id_detailActivityLayout)
        layout = findViewById(R.id.id_layout)
        tvTitle = findViewById(R.id.id_tvTitle)
        isAdult = findViewById(R.id.id_isAdult)
        releaseDate = findViewById(R.id.id_releaseDate)
        tvOverview = findViewById(R.id.id_tvOverview)
        ratingBar = findViewById(R.id.id_rbVoteAverage)
        ytPlayerView = findViewById(R.id.id_ytPlayer)

        val movie = intent.getParcelableExtra<Movie>("MOVIE_EXTRA") as Movie
        Log.i("RITIKA", "Movie is $movie")

        tvTitle.text = movie.title
        if (movie.isAdult)
            isAdult.text = "R"
        else
            isAdult.text = "PG-13"
        releaseDate.text = movie.releaseDate
        tvOverview.text = movie.overview
        ratingBar.rating = movie.vote_average.toFloat()
        Log.d ("RITIKA", "ratingbar: " + ratingBar.rating
        )
        Glide.with(this).asBitmap().load(movie.posterImageUrl).into(object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                Palette.from(resource).generate(PaletteAsyncListener { p ->
                    val defaultValue = 0xFFFF00
                    detailActivityLayout.setBackgroundColor(p!!.getMutedColor(defaultValue))
                    ratingBar.progressDrawable.setTint(p!!.getLightMutedColor(defaultValue))
                })
            }
            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object : JsonHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e("RITIKA", "TRAILERS_URL onFailure $statusCode")
                Log.e("RITIKA", "TRAILERS_URL: " + TRAILERS_URL.format(movie.movieId))
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i("RITIKA", "TRAILERS_URL onSuccess")
                Log.e("RITIKA", "TRAILERS_URL: " + TRAILERS_URL.format(movie.movieId))
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() == 0) {
                    Log.w("RITIKA", "No movie trailers found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")
                // play youtube video with this trailer
                initializeYoutube(youtubeKey)
            }

        })

    }

    private fun initializeYoutube(youtubeKey: String) {
        ytPlayerView.initialize(YOUTUBE_API_KEY , object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
                Log.i("RITIKA" , "onInitializationSuccess: ")
                player?.cueVideo(youtubeKey)
                if (ratingBar.rating >= 5.0)
                    player?.loadVideo(youtubeKey , 0)
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Log.i("RITIKA" , "onInitializationFailure")
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAfterTransition()
    }

}

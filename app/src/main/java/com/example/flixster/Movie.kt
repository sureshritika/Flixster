package com.example.flixster

import android.os.Parcelable
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import java.net.URI.create

@Parcelize
data class Movie(
    val movieId: Int,
    private val posterPath: String,
    private val backdropPath: String,
    val title: String,
    val overview: String,
    val vote_average: Double,
) : Parcelable {
    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"
    val backdropImageUrl = "https://image.tmdb.org/t/p/w342/$backdropPath"
    companion object {
        fun fromJsonArray(movieJsonArray : JSONArray) : List<Movie> {
            val movies = mutableListOf<Movie>()
            for (i in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add (
                    Movie (
                        movieJson.getInt("id") ,
                        movieJson.getString("poster_path") ,
                        movieJson.getString("backdrop_path") ,
                        movieJson.getString("title") ,
                        movieJson.getString("overview") ,
                        movieJson.getDouble("vote_average")
                    )
                )
            }
            return movies
        }
    }
}
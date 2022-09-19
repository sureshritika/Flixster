package com.example.flixster

import android.content.Context
import android.content.res.Configuration
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class MovieAdapter(private val context : Context, private val movies : List<Movie> , private val orien : Int) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    // expensive
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        //Log.d("RITIKA" , "create")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie , parent , false)
        return ViewHolder(view)
    }

    // cheap
    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        //Log.d("RITIKA" , "bind pos: $position")
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPoster = itemView.findViewById<ImageView>(R.id.id_tvPoster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.id_tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.id_tvOverview)
        private val ratingProgress = itemView.findViewById<ProgressBar>(R.id.id_ratingProgress)
        private val ratingText = itemView.findViewById<TextView>(R.id.id_ratingText)

        fun bind(movie : Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            if (orien == Configuration.ORIENTATION_PORTRAIT) {
                Glide.with(context).load(movie.posterImageUrl).placeholder(R.drawable.loading_img).into(tvPoster)
            }
            else if (orien == Configuration.ORIENTATION_LANDSCAPE) {
                Glide.with(context).load(movie.backdropImageUrl).placeholder(R.drawable.loading_img).into(tvPoster)
            }
            ratingProgress.progress = (movie.vote_average * 10).toInt()
            ratingText.text = (movie.vote_average * 10).toInt().toString() + "%"
        }
    }

}

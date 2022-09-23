package com.example.flixster

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {
        private val tvPosterImg = itemView.findViewById<ImageView>(R.id.id_tvPosterImg)
        private val tvPosterOverlay = itemView.findViewById<ImageView>(R.id.id_tvPosterOverlay)
        private val tvTitle = itemView.findViewById<TextView>(R.id.id_tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.id_tvOverview)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie : Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview

            if (orien == Configuration.ORIENTATION_PORTRAIT) {
                Glide.with(context).load(movie.posterImageUrl).placeholder(R.drawable.loading_img).fitCenter().transform(RoundedCorners(30)).into(tvPosterImg)

            }
            else if (orien == Configuration.ORIENTATION_LANDSCAPE) {
                Glide.with(context).load(movie.backdropImageUrl).placeholder(R.drawable.loading_img).fitCenter().transform(RoundedCorners(30)).into(tvPosterImg)
            }

            if (movie.vote_average >= 5.0)
                Glide.with(context).load(R.drawable.play_overlay).into(tvPosterOverlay)
        }

        override fun onClick(p0: View?) {
            // 1. Get notified of the particular movie which was clicked
            val movie = movies[adapterPosition]
            // 2. Use the intent system to navigate to the new activity
            val intent = Intent(context , DetailActivity::class.java)
            intent.putExtra("MOVIE_EXTRA" , movie)
            val pair1 : androidx.core.util.Pair<View , String> = androidx.core.util.Pair(tvTitle as View , "tvTitle")
            val pair2 : androidx.core.util.Pair<View , String> = androidx.core.util.Pair(tvOverview as View , "tvOverview")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, pair1 , pair2)
            context.startActivity(intent , options.toBundle())
        }
    }

}

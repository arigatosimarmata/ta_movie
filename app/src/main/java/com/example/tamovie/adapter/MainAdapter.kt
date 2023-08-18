package com.example.tamovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tamovie.R
import com.example.tamovie.model.Constant
import com.example.tamovie.model.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_main.view.img_poster
import kotlinx.android.synthetic.main.adapter_main.view.text_title

class MainAdapter(var movies: ArrayList<MovieModel>, var listener: OnAdapterListener): RecyclerView.Adapter<MainAdapter.ViewHolder>(){
    private val TAG:String = "MainActivity"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_main, parent, false)
    )

    override fun getItemCount()= movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        val backdropPath = Constant.BACKDROP_PATH + movie.backdrop_path
        val posterPath = Constant.POSTER_PATH + movie.poster_path
//        Log.d(TAG, "backdrop_url: ${posterPath}")

        Picasso.get()
            .load(posterPath)
            .placeholder(R.drawable.placeholder_portrait)
            .error(R.drawable.placeholder_portrait)
            .into(holder.view.img_poster);

        holder.view.img_poster.setOnClickListener{
            Constant.MOVIE_ID = movie.id!!
            Constant.MOVIE_TITLE = movie.title!!
            listener.onClick(movie)
        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val view = view
        fun bind(movies: MovieModel){
            view.text_title.text = movies.title

        }
    }

    public fun setData(newMovies: List<MovieModel>){
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    public fun setDataNextPage(newMovies: List<MovieModel>){
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(movie: MovieModel)
    }
}
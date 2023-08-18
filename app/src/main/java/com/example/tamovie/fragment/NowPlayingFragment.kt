package com.example.tamovie.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tamovie.R
import com.example.tamovie.activity.DetailActivity
import com.example.tamovie.adapter.MainAdapter
import com.example.tamovie.model.Constant
import com.example.tamovie.model.MovieModel
import com.example.tamovie.model.MovieResponse
import com.example.tamovie.retrofit.ApiService
import kotlinx.android.synthetic.main.fragment_now_playing.view.list_movie
import kotlinx.android.synthetic.main.fragment_now_playing.view.progress_movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NowPlayingFragment : Fragment() {
    lateinit var v:View
    lateinit var mainAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_now_playing, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        getMovieNowPlaying()
    }

    private fun setupRecyclerView(){
        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun onClick(movie: MovieModel) {
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }
        })
        v.list_movie.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    fun getMovieNowPlaying(){
        showLoading(true)
        ApiService().endpoint.getMovieNowPlaying(Constant.API_KEY, 1)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    showLoading(false)
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful){
                        showMovie(response.body()!!)
                    }
                }

            })
    }

    fun showLoading(loading:Boolean){
        when(loading){
            true -> v.progress_movie.visibility = View.VISIBLE
            false -> v.progress_movie.visibility = View.GONE
        }
    }

    fun showMovie(response: MovieResponse){
        mainAdapter.setData(response.results)
    }

}
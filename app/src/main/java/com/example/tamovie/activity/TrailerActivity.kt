package com.example.tamovie.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tamovie.R
import com.example.tamovie.adapter.TrailerAdapter
import com.example.tamovie.model.Constant
import com.example.tamovie.model.TrailerResponse
import com.example.tamovie.retrofit.ApiService
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_trailer.list_video
import kotlinx.android.synthetic.main.activity_trailer.progress_video
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrailerActivity : AppCompatActivity() {
    private val TAG: String = "TrailerActivity"

    lateinit var trailerAdapter: TrailerAdapter
    lateinit var youTubePlayer: YouTubePlayer
    private var youTubeKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        setupView()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        getMovieTrailer()
    }

    private fun setupView(){
        supportActionBar!!.title = Constant.MOVIE_TITLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youTubeKey?.let {
                    youTubePlayer.cueVideo(it,0f)
                }
            }
        })


    }

    private fun setupRecyclerView(){
        trailerAdapter = TrailerAdapter(arrayListOf(), object : TrailerAdapter.OnAdapterListener{
            override fun onLoad(key: String) {
                youTubeKey = key
            }

            override fun onPlay(key: String) {
                youTubePlayer.loadVideo(key, 0f)
            }

        })
        list_video.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trailerAdapter
        }
    }

    private fun getMovieTrailer(){
        showLoading(true)
        ApiService().endpoint.getMovieTrailer(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<TrailerResponse>{
                override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                    showLoading(true)
                }

                override fun onResponse(
                    call: Call<TrailerResponse>,
                    response: Response<TrailerResponse>
                ) {
                    showLoading(false)
                    if(response.isSuccessful){
                        showTrailer(response.body()!!)
                    }

                }



            })
    }

    private fun showLoading(loading:Boolean){
        when(loading){
            true -> {
                progress_video.visibility = View.VISIBLE
            }
            false -> {
                progress_video.visibility = View.GONE
            }
        }
    }

    private fun showTrailer(trailer : TrailerResponse){
        for (res in trailer.results){
            Log.d(TAG, "nameVideo: ${res.name}")
        }
        trailerAdapter.setData(trailer.results)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}
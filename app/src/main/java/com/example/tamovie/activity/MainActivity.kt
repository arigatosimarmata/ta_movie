package com.example.tamovie.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tamovie.R
import com.example.tamovie.adapter.MainAdapter
import com.example.tamovie.model.Constant
import com.example.tamovie.model.MovieModel
import com.example.tamovie.model.MovieResponse
import com.example.tamovie.retrofit.ApiService
import kotlinx.android.synthetic.main.content_main.list_movie
import kotlinx.android.synthetic.main.content_main.progress_movie
import kotlinx.android.synthetic.main.content_main.progress_movie_next_page
import kotlinx.android.synthetic.main.content_main.scrollView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val moviePopular =0
const val movieNowPlaying = 1

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    lateinit var mainAdapter: MainAdapter
    private var movieCategory = 0
    private val api = ApiService().endpoint
    private var isScrolling = false
    private var currentPage = 1
    private var totalPages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupRecyclerView()
        setupListener()

    }

    override fun onStart() {
        super.onStart()
        getMovie()
        showLoadingNextPage(false)
    }

    private fun setupRecyclerView(){
        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun onClick(movie: MovieModel) {
//                showMessage(movie.title!!)
                Constant.MOVIE_ID = movie.id!!
                Constant.MOVIE_TITLE = movie.title!!
                startActivity(Intent(applicationContext, DetailActivity::class.java))
            }

        })
        list_movie.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    private fun setupListener(){
        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if(scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight){
                    if (!isScrolling){
                        if(currentPage <= totalPages){
                            getMovieNextPage()
                        }
                    }
                }
            }

        })
    }

    private fun getMovie(){
        scrollView.scrollTo(0,0)
        currentPage = 1
        showLoading(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory){
            moviePopular -> {
                apiCall = api.getMoviePopular(Constant.API_KEY, 1)
            }
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(Constant.API_KEY, 1)
            }
        }

        apiCall!!
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG, "errorResponse: $t" )
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

    private fun getMovieNextPage(){
        currentPage += 1
        showLoadingNextPage(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory){
            moviePopular -> {
                apiCall = api.getMoviePopular(Constant.API_KEY, currentPage)
            }
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(Constant.API_KEY, currentPage)
            }
        }

        apiCall!!
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG, "errorResponse: $t" )
                    showLoadingNextPage(false)
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoadingNextPage(false)
                    if (response.isSuccessful){
                        showMovieNextPage(response.body()!!)
                    }
                }



            })
    }

    fun showLoading(loading:Boolean){
        when(loading){
            true -> progress_movie.visibility = View.VISIBLE
            false -> progress_movie.visibility = View.GONE
        }
    }

    fun showLoadingNextPage(loading:Boolean){
        when(loading){
            true -> {
                isScrolling = true
                progress_movie_next_page.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                progress_movie_next_page.visibility = View.GONE
            }
        }
    }

    fun showMovie(response: MovieResponse){
        totalPages = response.total_pages!!.toInt()
//        Log.d(TAG, "responseMovie: $response")
//        Log.d(TAG, "total_pages: ${response.total_pages}")
//        for (movie in response.results){
//            Log.d(TAG, "movie_title: ${movie.title}")
//        }
        mainAdapter.setData(response.results)
    }

    fun showMovieNextPage(response: MovieResponse){
        totalPages = response.total_pages!!.toInt()
        mainAdapter.setDataNextPage(response.results)
        showMessage("Page ${currentPage}")
    }

    fun showMessage(msg: String){
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_popular -> {
                showMessage("movie popular selected")
                movieCategory = moviePopular
                getMovie()
                true
            }
            R.id.action_now_playing -> {
                showMessage("movie now playing selected")
                movieCategory = movieNowPlaying
                getMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
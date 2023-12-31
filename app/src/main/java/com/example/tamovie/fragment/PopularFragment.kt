package com.example.tamovie.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tamovie.R
import com.example.tamovie.activity.DetailActivity
import com.example.tamovie.adapter.MainAdapter
import com.example.tamovie.model.Constant
import com.example.tamovie.model.MovieModel
import com.example.tamovie.model.MovieResponse
import com.example.tamovie.retrofit.ApiService
import kotlinx.android.synthetic.main.content_main.view.progress_movie_next_page
import kotlinx.android.synthetic.main.fragment_popular.scrollView
import kotlinx.android.synthetic.main.fragment_popular.view.list_movie
import kotlinx.android.synthetic.main.fragment_popular.view.progress_movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PopularFragment : Fragment() {

    lateinit var v:View
    lateinit var mainAdapter: MainAdapter
    private var isScrolling = false
    private var currentPage = 1
    private var totalPages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_popular, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getMoviePopular()
        showLoadingNextPage(false)
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
                            getMoviePopularNextPage()
                        }
                    }
                }
            }

        })
    }

    fun getMoviePopular(){
        currentPage = 1

        showLoading(true)
        ApiService().endpoint.getMoviePopular(Constant.API_KEY, 1)
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

    fun getMoviePopularNextPage(){
        currentPage += 1
        showLoadingNextPage(true)
        ApiService().endpoint.getMoviePopular(Constant.API_KEY, currentPage)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
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
            true -> v.progress_movie.visibility = View.VISIBLE
            false -> v.progress_movie.visibility = View.GONE
        }
    }

    fun showLoadingNextPage(loading:Boolean){
        when(loading){
            true -> {
                isScrolling = true
                v.progress_movie_next_page.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                v.progress_movie_next_page.visibility = View.GONE
            }
        }
    }

    fun showMovie(response: MovieResponse){
        totalPages = response.total_pages!!
        mainAdapter.setData(response.results)
    }

    fun showMovieNextPage(response: MovieResponse){
        totalPages = response.total_pages!!
        mainAdapter.setDataNextPage(response.results)
        Toast.makeText(requireContext(), "Page $currentPage",Toast.LENGTH_SHORT).show()
    }

}
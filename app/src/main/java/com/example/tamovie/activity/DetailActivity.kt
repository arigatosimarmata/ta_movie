package com.example.tamovie.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tamovie.R
import com.example.tamovie.adapter.UserReviewAdapter
import com.example.tamovie.databinding.ActivityDetailBinding
import com.example.tamovie.model.Constant
import com.example.tamovie.model.DetailResponse
import com.example.tamovie.model.UserReviewModel
import com.example.tamovie.model.UserReviewResponse
import com.example.tamovie.retrofit.ApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.img_poster
import kotlinx.android.synthetic.main.content_detail.fab_play
import kotlinx.android.synthetic.main.content_detail.list_user_review
import kotlinx.android.synthetic.main.content_detail.text_genre
import kotlinx.android.synthetic.main.content_detail.text_overview
import kotlinx.android.synthetic.main.content_detail.text_title
import kotlinx.android.synthetic.main.content_detail.text_vote
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private val TAG: String = "DetailActivity"

    lateinit var userReviewAdapter: UserReviewAdapter
    private lateinit var binding: ActivityDetailBinding
    private val dataListUserReview: MutableList<UserReviewModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupRecyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getMovieDetail()
        getUserReview()
    }

    private fun setupRecyclerView(){
        userReviewAdapter = UserReviewAdapter(dataListUserReview)
        list_user_review.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userReviewAdapter
        }

    }

    private fun setupView(){
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupListener(){
        fab_play.setOnClickListener{
            startActivity(Intent(applicationContext, TrailerActivity::class.java))
        }
    }

    private fun getMovieDetail(){
        ApiService().endpoint.getMovieDetail(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<DetailResponse>{
                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {

                    if(response.isSuccessful){
                        showMovie(response.body()!!)
                    }
                }
            })
    }

    private fun getUserReview(){
        ApiService().endpoint.getUserReview(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<UserReviewResponse>{
                override fun onFailure(call: Call<UserReviewResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

                override fun onResponse(
                    call: Call<UserReviewResponse>,
                    response: Response<UserReviewResponse>
                ) {
                    if(response.isSuccessful){
//                        dataListUserReview.addAll(response.body()?.results!!)
//                        userReviewAdapter.notifyDataSetChanged()
                        showUserReview(response.body()!!)
                    }
                }



            })
    }

    fun showMovie(detail: DetailResponse){
//        Log.d(TAG, "movieDetailResponse: ${detail}" )
        val backdropPath = Constant.BACKDROP_PATH + detail.backdrop_path
        Picasso.get()
            .load(backdropPath)
            .placeholder(R.drawable.placeholder_portrait)
            .error(R.drawable.placeholder_portrait)
            .fit().centerCrop()
            .into(img_poster);

        text_title.text = detail.title
        text_vote.text = detail.vote_average.toString().subSequence(0,3)
        text_overview.text =detail.overview

        var genres = ""
        for (genre in detail.genres!!){
            text_genre.text = "${genre.name} "
        }
    }

    fun showUserReview(user_review: UserReviewResponse){
//        for (ur in user_review.results!!){
//            Log.d(TAG, "user_review_author: ${ur.author}" )
////            text_author.text = ur.author
////            text_content.text = ur.content
//        }
//        userReviewAdapter.setData(user_review.results)

        dataListUserReview.addAll(user_review.results!!)
        userReviewAdapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}
package com.example.tamovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tamovie.R
import com.example.tamovie.model.UserReviewModel
import kotlinx.android.synthetic.main.adapter_user_review.view.text_author
import kotlinx.android.synthetic.main.adapter_user_review.view.text_content

class UserReviewAdapter(var review: MutableList<UserReviewModel>): RecyclerView.Adapter<UserReviewAdapter.ViewHolder>(){
    private val TAG:String = "UserReviewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_user_review, parent, false)
    )

    override fun getItemCount()= review.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usr_review = review[position]

        val reviewAuthor = holder.itemView.text_author
        val reviewContent = holder.itemView.text_content

        reviewAuthor.text = "${usr_review.author}"
        reviewContent.text = "${usr_review.content}"
        holder.bind(usr_review)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val view = view
        fun bind(review: UserReviewModel){
            view.text_author.text = review.author
            view.text_content.text = review.content
        }
    }

    public fun setData(newUserReview: List<UserReviewModel>){
        review.clear()
        review.addAll(newUserReview)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(review: UserReviewModel)
    }
}
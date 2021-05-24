package com.priyanshumaurya8868.realworld.io.conduit.ui.feeds

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.priyanshumaurya8868.realworld.io.api.MyConduitClient
import com.priyanshumaurya8868.realworld.io.api.model.entites.GetArticle
import com.priyanshumaurya8868.realworld.io.conduit.R
import com.priyanshumaurya8868.realworld.io.conduit.databinding.FeedItemBinding
import com.priyanshumaurya8868.realworld.io.conduit.extentions.loadImageInCircleView
import com.priyanshumaurya8868.realworld.io.conduit.extentions.timeStamp


class FeedAdapter(val onArticleClicked: (slug: String) -> Unit,val onLike: (slug : String) -> Unit) :
    ListAdapter<GetArticle, FeedAdapter.FeedViewHolder>(
        object : DiffUtil.ItemCallback<GetArticle>() {
            override fun areItemsTheSame(oldItem: GetArticle, newItem: GetArticle): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GetArticle, newItem: GetArticle): Boolean {
                //since Article is a data class so its toString exists,
                // therefore we can compare its content like this without overriding hashcode/equals
                return oldItem.toString() == newItem.toString()
            }
        }
    ) {

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.feed_item,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        FeedItemBinding.bind(holder.itemView).apply {
            val article = getItem(position)

            feedAvatarImageView.loadImageInCircleView(article.author.image)

            feedItemAuthorTv.text = article.author.username
            feedItemDateTv.timeStamp = article.createdAt
            feedItemTitleTv.text = article.title
            feedItemDescriptionTv.text = article.description
            tvLikecount.text = article.favoritesCount.toString()
            Log.d("FEED"," is Favourited : ${article.favorited}")
           if (article.favorited) {
               favourite(this)
           }
            else unfavourite(this)

//          feedItemBodyTv.text=article.body
            root.setOnClickListener {
                onArticleClicked(article.slug)
            }
            this.likeContainer.setOnClickListener {
                MyConduitClient.authToken?.let{
                    Log.d("FEED", "auth token is not null -> $it")
                    onLike(article.slug)
                    if (!article.favorited) {
                        favourite(this)
                        tvLikecount.text = (article.favoritesCount + 1).toString()
                    } else {
                        unfavourite(this)
                        tvLikecount.text = (article.favoritesCount - 1).toString()
                    }
                }
            }
        }
//       holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_silde_in)

    }

    private fun favourite(binding: FeedItemBinding){
        binding.apply{
            likeContainer.setBackgroundResource(R.drawable.green_strock_green_bc)
            tvLikecount.setTextColor(Color.parseColor("#FFFFFF"))
            ivlikeHeartIcon.imageTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        }
    }

    private fun unfavourite(binding: FeedItemBinding){
        binding.apply{
           likeContainer.setBackgroundResource(R.drawable.green_strock_transparent_bc)
           tvLikecount.setTextColor(Color.parseColor("#5CB85C"))
           ivlikeHeartIcon.imageTintList =
                ColorStateList.valueOf(Color.parseColor("#5CB85C"))
        }
    }
}
package com.priyanshumaurya8868.realworld.io.conduit.ui.article

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.priyanshumaurya8868.realworld.io.api.model.entites.GetComment
import com.priyanshumaurya8868.realworld.io.conduit.R
import com.priyanshumaurya8868.realworld.io.conduit.databinding.CmntItemViewBinding
import com.priyanshumaurya8868.realworld.io.conduit.extentions.loadImageInCircleView
import com.priyanshumaurya8868.realworld.io.conduit.extentions.timeStamp

class CommentsRVAdapter : ListAdapter<GetComment, CommentsRVAdapter.CommentVH>(
    object : DiffUtil.ItemCallback<GetComment>() {
        override fun areItemsTheSame(oldItem: GetComment, newItem: GetComment)
        = oldItem == newItem

        override fun areContentsTheSame(oldItem: GetComment, newItem: GetComment)
        = oldItem.toString() == newItem.toString()
    }
) {

    inner class  CommentVH(val binding : CmntItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH {
        val inflater = parent.context.getSystemService(LayoutInflater::class.java)
       val  binding = CmntItemViewBinding.inflate(inflater)
        return CommentVH(binding)
    }

    override fun onBindViewHolder(holder: CommentVH, position: Int) {
      val  comment = getItem(position)
        holder.binding.apply {
            tvComment.text = comment.body
            ivUserPfp.loadImageInCircleView(comment.author.image)
            tvNameCmntater.text = comment.author.username
            tvDate.timeStamp= comment.createdAt
        }
    }
}
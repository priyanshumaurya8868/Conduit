package com.priyanshumaurya8868.realworld.io.conduit.ui.article

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.priyanshumaurya8868.realworld.io.conduit.R
import com.priyanshumaurya8868.realworld.io.conduit.databinding.TagsItemViewBinding

class TagListAdapter(val updateTagList: (delIndex : Int) -> Unit) :ListAdapter<String, TagListAdapter.MyTagVH>(object : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
      return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem==newItem
    }

}){
    inner class MyTagVH(val binding : TagsItemViewBinding)
        :RecyclerView.ViewHolder(binding.root)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTagVH {
      val  inflater = parent.context.getSystemService(LayoutInflater::class.java)
        val binding = TagsItemViewBinding.inflate(inflater,parent,false)
        return MyTagVH(binding)
    }

    override fun onBindViewHolder(holder: MyTagVH, position: Int) {
     val tag = getItem(position)
        holder.binding.tvTagname.text = tag
        holder.binding.root.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_silde_in)
        holder.binding.deleteTagBtn.setOnClickListener {
           updateTagList(position)
            notifyDataSetChanged()
        }
    }


}
package com.priyanshumaurya8868.realworld.io.conduit.ui.feeds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priyanshumaurya8868.realworld.io.conduit.R
import com.priyanshumaurya8868.realworld.io.conduit.databinding.GlobalFeedFragmentBinding
import com.priyanshumaurya8868.realworld.io.conduit.ui.article.ArticleViewModel
import kotlinx.coroutines.launch

class GlobalFeed : Fragment() {

   
    private var binding: GlobalFeedFragmentBinding? = null
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var feedAdapter: FeedAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        feedViewModel.fetchGlobalFeed()
        feedAdapter =
            FeedAdapter(
                { openArticle(it) },
                { favouriteArticle(it) }) //passing function as argument this called "lambda passing
        feedViewModel.feed.observe({ lifecycle }) {
            feedAdapter.submitList(it)
        }
        binding = GlobalFeedFragmentBinding.inflate(layoutInflater, container, false)
        binding?.feedRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = feedAdapter
            addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        binding!!.writeArticleFabGlobalFeed.shrink()
                    } else {
                        binding!!.writeArticleFabGlobalFeed.extend()
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }

        binding?.writeArticleFabGlobalFeed?.setOnClickListener {
            findNavController().navigate(R.id.action_global_feed_to_nav_write_article)
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun openArticle(articleID: String) {
        findNavController().navigate(R.id.action_global_feed_to_nav_article,
            Bundle().apply
            {
                putString(resources.getString(R.string.arg_article_id), articleID)
            })
    }

    private fun favouriteArticle(slug: String) {
        lifecycleScope.launch {
           articleViewModel.getArticleBySlug(slug).await()
            val article = articleViewModel.getArticle.value
            Log.d("FEED", "At server fav = ${article?.favorited} ..")
            if (article?.favorited == false) {
                Log.d("FEED", " ${article.favoritesCount} -> fav func ")
                feedViewModel.favouriteArticle(slug)
            } else {
                Log.d("FEED", " ${article?.favoritesCount} -> unfav func ")
                feedViewModel.unfavouriteArticle(slug)
            }
          feedViewModel.fetchGlobalFeed()
        }
    }
}
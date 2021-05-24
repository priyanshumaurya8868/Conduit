package com.priyanshumaurya8868.realworld.io.conduit.ui.profile

import android.content.res.ColorStateList
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
import com.priyanshumaurya8868.realworld.io.api.MyConduitClient
import com.priyanshumaurya8868.realworld.io.api.model.entites.Profile
import com.priyanshumaurya8868.realworld.io.conduit.R
import com.priyanshumaurya8868.realworld.io.conduit.databinding.ProfileFragmentBinding
import com.priyanshumaurya8868.realworld.io.conduit.extentions.loadImageInCircleView
import com.priyanshumaurya8868.realworld.io.conduit.ui.article.ArticleViewModel
import com.priyanshumaurya8868.realworld.io.conduit.ui.feeds.FeedAdapter
import com.priyanshumaurya8868.realworld.io.conduit.ui.feeds.FeedViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var isfollowing = false
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: ProfileFragmentBinding? = null
    private var feedAdapter: FeedAdapter? = null
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var articleViewModel: ArticleViewModel
    private var _username: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        feedAdapter =
            FeedAdapter(
                { openArticle(it) },
                { favouriteArticle(it) })
        arguments?.let {
            _username = it.getString(resources.getString(R.string.arg_username))
        }
        _username?.let { profileViewModel.getUser(it) } ?: Log.d("profile", "username is null.!")
        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.user.observe({ lifecycle }) {
            _binding?.apply {
             intializeChildViews(it)
               setUpActionListner(it)
                profileViewModel.getMyArticles(it.username)
                profileViewModel.getFavArticles(it.username)
        }
        setupRv()
        setupBottomNav()
        }
    }

    private fun setUpActionListner(profile: Profile) {
        _binding?.apply {
            followContainer.setOnClickListener { view ->
                if (isfollowing){
                    profileViewModel.unfollowProfile(profile.username)
                    setUpFollowBtnAppearance(!isfollowing)
                }else{
                    profileViewModel.followProfile(profile.username)
                    setUpFollowBtnAppearance(!isfollowing)
                }
            }
        }
    }

    private fun intializeChildViews(profile: Profile) {
 _binding?.apply {
                ivPfp.loadImageInCircleView(profile.image)
                tvAutherName.text = profile.username

                if (!profile.bio.isNullOrBlank() ) {
                    tvBio.visibility = View.VISIBLE
                    tvBio.text = profile.bio
                } else tvBio.visibility = View.GONE

                if (!MyConduitClient.authToken.isNullOrBlank())
                    _binding?.followContainer?.visibility = View.GONE
                else {
                    setUpFollowBtnAppearance(profile.following)
                }
     profileViewModel.myProfile.observe({lifecycle}){
         if (it!=null && it.username == profile.username){
             followContainer.visibility = View.GONE
             updateProfileContainer.visibility = View.VISIBLE
         } else{
             updateProfileContainer.visibility = View.GONE
             followContainer.visibility  = View.VISIBLE
           }

         }
      }
    }


    private fun setupBottomNav() {
        _binding?.bottomNav?.setItemSelected(R.id.menu_my_article, true)
        profileViewModel.authorArticles.observe({ lifecycle }) {
            feedAdapter?.submitList(it)
        }
        _binding?.bottomNav?.setOnItemSelectedListener { id ->

            Log.d(
                "profile",
                "got -> $id , ma -> ${R.id.menu_my_article}  , fa -> ${R.id.menu_fav_article}"
            )
            when (id) {
                R.id.menu_my_article -> {
                    profileViewModel.authorArticles.observe({ lifecycle }) {
                        feedAdapter?.submitList(it)
                    }
                }
                R.id.menu_fav_article -> {
                    profileViewModel.favArticles.observe({ lifecycle }) {
                        feedAdapter?.submitList(it)
                    }
                    Log.d("profile", "Fav  entered ")
                }
            }
        }
    }

    private fun setupRv() {
        _binding?.rvMyArticles?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = feedAdapter
        }
    }

    fun setUpFollowBtnAppearance(following: Boolean = isfollowing) {
        isfollowing = following
        _binding?.apply {
            if (following) {
                followContainer.setBackgroundResource(R.drawable.green_strock_transparent_bc)
                followTv.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.conduit_green)))
                followIcon.imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.conduit_green))
                followTv.text = resources.getString(R.string.unfollow)
            } else {
                followContainer.setBackgroundResource(R.drawable.green_strock_green_bc)
                followTv.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.white)))
                followIcon.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
                followTv.text = resources.getString(R.string.follow)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()

    }

    private fun openArticle(articleID: String) {
        findNavController().navigate(R.id.action_nav_profile_to_nav_article,
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
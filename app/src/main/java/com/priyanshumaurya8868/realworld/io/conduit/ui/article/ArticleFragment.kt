package com.priyanshumaurya8868.realworld.io.conduit.ui.article

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.priyanshumaurya8868.realworld.io.api.MyConduitClient
import com.priyanshumaurya8868.realworld.io.api.model.entites.Comment
import com.priyanshumaurya8868.realworld.io.api.model.entites.GetArticle
import com.priyanshumaurya8868.realworld.io.conduit.MainActivity
import com.priyanshumaurya8868.realworld.io.conduit.R
import com.priyanshumaurya8868.realworld.io.conduit.databinding.ArticleFragmentBinding
import com.priyanshumaurya8868.realworld.io.conduit.extentions.loadImageInCircleView
import com.priyanshumaurya8868.realworld.io.conduit.extentions.timeStamp
import com.priyanshumaurya8868.realworld.io.conduit.ui.profile.ProfileViewModel

class ArticleFragment : Fragment() {
    private var isArticleFavourited = false
    private var isFollowing = false
    private var _binding: ArticleFragmentBinding? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var articleViewModel: ArticleViewModel
    private var commentsAdapter: CommentsRVAdapter? = null
    private val tagListAdapter = TagListAdapter()
    private var articleID: String? = null
    private var slug: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            articleID = it.getString(getString(R.string.arg_article_id))
        }
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        _binding = ArticleFragmentBinding.inflate(inflater, container, false)
        commentsAdapter = CommentsRVAdapter()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleID?.let {
            articleViewModel.getArticleBySlug(it)
        }
        articleViewModel.getArticle.observe({ lifecycle }) {
            it?.let { article ->
                _binding?.apply {
                    slug = article.slug
                    setupComntRV(article)
                    if(article.tagList.isNotEmpty()) setUpTagRv(article)
                    else tagContainer.visibility = View.GONE
                    initializeChildViews(article)
                    setUpbuttonsAppearance(article.author.following, article.favorited)
                    setUpActionListner(article)
                }
            }
        }
    }

    private fun btnAbstraction(article: GetArticle) {
        (activity as MainActivity).authViewModel.user.observe({ lifecycle }){
            if (it != null &&
                article.author.username == it.username){
               _binding?.updateContainer?.visibility = View.VISIBLE
                _binding?.followContainer?.visibility = View.GONE
                _binding?.deleteContainer?.visibility = View.VISIBLE

            }else {
               _binding?.updateContainer?.visibility = View.GONE
                _binding?.followContainer?.visibility = View.VISIBLE
                _binding?.deleteContainer?.visibility = View.GONE
            }
        }
    }

    private fun setUpActionListner(article: GetArticle) {
        btnAbstraction(article)
        _binding?.apply {
            deleteContainer.setOnClickListener {
                articleViewModel.deleteArticle(article.slug)
                findNavController().popBackStack()
            }
            MyConduitClient.authToken?.let{
                followContainer.setOnClickListener {
                    if (isFollowing) {
                        profileViewModel.unfollowProfile(username = article.author.username)
                        setUpbuttonsAppearance(following = !isFollowing)
                    } else {
                        profileViewModel.followProfile(username = article.author.username)
                        setUpbuttonsAppearance(following = !isFollowing)
                    }
                }
                favContainer.setOnClickListener {
                    if (isArticleFavourited) {
                        articleViewModel.unfavouriteArticle(article.slug)
                        setUpbuttonsAppearance(favorited = !isArticleFavourited)
                    } else {
                        articleViewModel.favouriteArticle(article.slug)
                        setUpbuttonsAppearance(favorited = !isArticleFavourited)
                    }
                }
            }

            pfpArticlePreview.setOnClickListener { view ->
                openProfile(article.author.username)

            }
            authorArticlePreviewTv.setOnClickListener { view ->
                openProfile(article.author.username)
            }
            btnPostCmnt.setOnClickListener {
                postComment()
            }

            updateContainer.setOnClickListener{
                val  bundle = bundleOf(resources.getString(R.string.arg_article_key) to article)
                findNavController().navigate(R.id.action_nav_article_to_nav_write_article,bundle)
            }
        }
    }



    private fun initializeChildViews(article: GetArticle) {
        _binding?.apply {
            titleArticlePreviewTv.text = article.title
            authorArticlePreviewTv.text = article.author.username
            dateArticlePreviewTv.timeStamp = article.createdAt
            bodyArticlePreviewTv.text = article.body
            pfpArticlePreview.loadImageInCircleView(article.author.image)
        }
    }

    private fun setUpbuttonsAppearance(
        following: Boolean = isFollowing,
        favorited: Boolean = isArticleFavourited
    ) {
        _binding?.apply {
            isArticleFavourited = favorited
            isFollowing = following
            if (following) {
                followContainer.setBackgroundResource(R.drawable.grey_stroc_grey_bc)
                followTv.text = resources.getString(R.string.unfollow)
            } else {
                followContainer.setBackgroundResource(R.drawable.grey_stroc_trans_bc)
                followTv.text = resources.getString(R.string.follow)
            }
            if (favorited) {
                favContainer.setBackgroundResource(R.drawable.green_strock_green_bc)
                favTv.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.white)))
                favIcon.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
            } else {
                _binding?.favContainer?.setBackgroundResource(R.drawable.green_strock_transparent_bc)
                favTv.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.conduit_green)))
                favIcon.imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.conduit_green))
            }
        }
    }

    private fun setupComntRV(article: GetArticle) {
        Log.d("comment", "rv setup")
        articleViewModel.getComments(article.slug)
        articleViewModel.cmnts.observe({ lifecycle }) { commentsAdapter?.submitList(it) }
        _binding?.rvComments?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentsAdapter?.also { it ->
                it.deleteComment{ id->
                    articleViewModel.deleteComment(article.slug,id)
                    articleViewModel.getComments(slug!!)
                }
            }
            (activity as MainActivity).authViewModel.user.observe({ lifecycle }){
                commentsAdapter?.authorName = it?.username
            }
        }
    }


    private fun postComment() {
        slug?.let {
            if (!_binding?.etComment?.text.isNullOrBlank()) {
                articleViewModel.postComment(
                    slug = it,
                    comment = Comment(_binding?.etComment?.text.toString())
                )
                articleViewModel.getComments(it)
            }
            _binding?.etComment?.setText("")
        }
    }

    private fun openProfile(username: String) {
        findNavController().navigate(R.id.action_nav_article_to_nav_profile,
            Bundle().apply
            {
                putString(resources.getString(R.string.arg_username), username)
            })
    }

    private fun setUpTagRv(article : GetArticle){
        _binding?.apply {
            tagContainer.visibility  = View.VISIBLE
            rvTag.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            rvTag.adapter = tagListAdapter
            tagListAdapter.submitList(article.tagList)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

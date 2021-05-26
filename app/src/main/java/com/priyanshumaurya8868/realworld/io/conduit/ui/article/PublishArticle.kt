package com.priyanshumaurya8868.realworld.io.conduit.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.priyanshumaurya8868.realworld.io.api.model.entites.Article
import com.priyanshumaurya8868.realworld.io.api.model.entites.GetArticle
import com.priyanshumaurya8868.realworld.io.conduit.MainActivity
import com.priyanshumaurya8868.realworld.io.conduit.R
import com.priyanshumaurya8868.realworld.io.conduit.databinding.WriteArticleBinding
import com.priyanshumaurya8868.realworld.io.conduit.ui.feeds.FeedViewModel
import kotlinx.coroutines.launch

class PublishArticle : Fragment() {
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var feedViewModel: FeedViewModel
    private var _binding: WriteArticleBinding? = null
    private var tagList = ArrayList<String>()
    private lateinit var adapter: TagListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        _binding = WriteArticleBinding.inflate(inflater, container, false)
        adapter = TagListAdapter()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments?.get(resources.getString(R.string.arg_article_key)) as GetArticle?
        (activity as MainActivity).authViewModel.user.observe({ lifecycle }) { user ->
            if (user != null) {
                arg?.let { article ->
                    _binding?.apply {
                        etArticleTitle.setText(article.title)
                        etArticleBody.setText(article.body)
                        etArticleDes.setText(article.description)
                        tagList.addAll(article.tagList)
                        adapter.submitList(tagList as List<String>)
                        Log.d("article", "Kam ho gaya...!")
                    }
                    setUpUpdateArticleBtn(article.slug)
                } ?: Log.d("article", "args is  null")
            } else Log.d("article", "user is  null")
        }


        _binding?.addTagsBtn?.setOnClickListener {

            _binding!!.etArticleTaglist.text.toString().apply {
                if (isNotBlank()) {
                    tagList.add(this.trim())
                    adapter.submitList(tagList)
                    adapter.notifyItemInserted(adapter.currentList.size -1)
                    _binding!!.etArticleTaglist.setText("")
                } else
                    Toast.makeText(context, "field is empty...!", Toast.LENGTH_SHORT).show()
            }

        }
        _binding?.articleSubmitBtn?.setOnClickListener {
            publishArticle()
        }
        setupRV()
    }

    private fun setUpUpdateArticleBtn(slug: String) {
        _binding?.apply {
            articleSubmitBtn.visibility = View.GONE
            articleUpdateBtn.visibility = View.VISIBLE
            articleUpdateBtn.setOnClickListener {
             extractData()?.let { updatedArticle ->
              articleViewModel.updateArticle(
                        slug,
                        updatedArticle
                    )
                }
                Toast.makeText(requireContext(), "Article Updated....!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

    }


    private fun publishArticle() {
        lifecycleScope.launch {
            Log.d("omega", "publishing article...")
            val res = extractData()?.let { articleViewModel.publishedArticle(it) }
            Log.d("omega", "publishing article...$res")
            if (res == 200 || res == 201) {
                Toast.makeText(context, "Article Published", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()

            } else
                Toast.makeText(context, "ERROR $res found...!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun extractData(): Article? {
        if (validateInputs()) {
            Log.d("article","submited tagList -> $tagList")
            return Article(
                title = _binding?.etArticleTitle?.text.toString().trim(),
                description = _binding?.etArticleDes?.text.toString().trim(),
                body = _binding?.etArticleBody?.text.toString().trim(),
                tagList = tagList
            )

        }
        Toast.makeText(context, "Required fields are empty...!", Toast.LENGTH_SHORT).show()
        return null
    }

    private fun validateInputs(): Boolean {

        if (_binding?.etArticleTitle?.text?.isBlank() == true) {
            _binding?.etArticleTitle?.error = "Title is Required!"
            _binding?.etArticleTitle?.requestFocus()
            return false
        }
        if (_binding?.etArticleDes?.text?.isBlank() == true) {
            _binding?.etArticleDes?.error = "Description is Required!"
            _binding?.etArticleDes?.requestFocus()
            return false
        }
        if (_binding?.etArticleBody?.text?.isBlank() == true) {
            _binding?.etArticleBody?.error = "body is required!"
            _binding?.etArticleBody?.requestFocus()
            return false
        }
        return true
    }

    private fun setupRV() {
        _binding?.rvtags?.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        _binding?.rvtags?.adapter = adapter
        adapter.submitList(tagList as List<String>)
        adapter.removeTag {
            removeTag(it)
        }
    }

    private fun removeTag(pos: Int) {
        tagList.removeAt(pos)
        adapter.submitList(tagList as List<String>)
        adapter.notifyItemRemoved(pos)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

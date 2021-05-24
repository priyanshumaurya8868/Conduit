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
import com.priyanshumaurya8868.realworld.io.conduit.databinding.WriteArticleBinding
import com.priyanshumaurya8868.realworld.io.conduit.ui.feeds.FeedViewModel
import kotlinx.coroutines.launch

class PublishArticle : Fragment() {
    private lateinit var articleViewModel  : ArticleViewModel
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
        adapter = TagListAdapter { updateList(it) }
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding?.addTagsBtn?.setOnClickListener {
            _binding!!.etArticleTaglist.text.toString().apply{
                if (isNotBlank()) {
                    tagList.add(this.trim())
                    adapter.submitList(tagList)
                    _binding!!.etArticleTaglist.setText("")
                } else
                    Toast.makeText(context, "field is empty...!", Toast.LENGTH_SHORT).show()
            }

        }
        _binding?.articleSubmitBtn?.setOnClickListener {
            publishArticle()
        }
        context?.let { setupRV() }

    }


    private fun publishArticle() {
        if (validateInputs()) {
            val article = Article(
                title = _binding?.etArticleTitle?.text.toString().trim(),
                description = _binding?.etArticleDes?.text.toString().trim(),
                body = _binding?.etArticleBody?.text.toString().trim(),
                tagList = tagList
            )
            lifecycleScope.launch{
                Log.d("omega", "publishing article...")
                val res = articleViewModel.publishedArticle(article)
                Log.d("omega", "publishing article...$res")
                if (res == 200 || res == 201) {
                    Toast.makeText(context, "Article Published", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()

                } else
                    Toast.makeText(context, "ERROR $res found...!", Toast.LENGTH_SHORT).show()
            }
        }
        else
            Toast.makeText(context, "Required fields are empty...!", Toast.LENGTH_SHORT).show()
//

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
                adapter.submitList(tagList)
            }

            private fun updateList(pos: Int) {
                tagList.removeAt(pos)
                adapter.submitList(tagList)
            }

            override fun onDestroy() {
                super.onDestroy()
                _binding = null
            }
        }

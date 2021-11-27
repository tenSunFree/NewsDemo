package com.example.newsdemo.square

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdemo.common.local.Constants
import com.example.newsdemo.common.local.Constants.Companion.QUERY_PER_PAGE
import com.example.newsdemo.common.base.BaseFragment
import com.example.newsdemo.common.remote.NetworkState
import com.example.newsdemo.common.util.EndlessRecyclerOnScrollListener
import com.example.newsdemo.databinding.FragmentSquareBinding
import com.example.newsdemo.main.MainActivity
import com.example.newsdemo.main.MainViewModel

class SquareFragment : BaseFragment<FragmentSquareBinding>() {

    override fun setBinding(): FragmentSquareBinding =
        FragmentSquareBinding.inflate(layoutInflater)

    private lateinit var onScrollListener: EndlessRecyclerOnScrollListener
    lateinit var viewModel: MainViewModel
    lateinit var squareAdapter: SquareAdapter
    var articlesSize = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        squareAdapter = SquareAdapter()
        onScrollListener = object : EndlessRecyclerOnScrollListener(QUERY_PER_PAGE) {
            override fun onLoadMore() {
                viewModel.fetchNews(Constants.CountryCode)
            }
        }
        binding.recyclerView.apply {
            adapter = squareAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(onScrollListener)
        }
    }

    private fun initViewModel() {
        viewModel = (activity as MainActivity).mainViewModel
        viewModel.responseNetworkState.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkState.SuccessNetworkState -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newResponse ->
                        val list = newResponse.articles.toList()
                        val isLastPage = articlesSize >= list.size
                        articlesSize = list.size
                        squareAdapter.differ.submitList(list)
                        viewModel.insertMainArticleEntityList(newResponse.articles.toList())
                        onScrollListener.isLastPage = isLastPage
                        if (onScrollListener.isLastPage) {
                            binding.recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is NetworkState.LoadingNetworkState -> {
                    showProgressBar()
                }
                is NetworkState.ErrorNetworkState -> {
                    hideProgressBar()
                    response.message?.let {
                        showErrorMessage(response.message)
                    }
                }
            }
        })
        viewModel.errorToast.observe(viewLifecycleOwner, { value ->
            if (value.isNotEmpty()) {
                Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
            } else {
                viewModel.hideErrorToast()
            }
        })
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showErrorMessage(message: String) {
        binding.includeItemErrorMessage.errorCard.visibility = View.VISIBLE
        binding.includeItemErrorMessage.tvErrorMessage.text = message
        onScrollListener.isError = true
    }

    private fun hideErrorMessage() {
        binding.includeItemErrorMessage.errorCard.visibility = View.GONE
        onScrollListener.isError = false
    }
}
package com.example.newsdemo.news

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdemo.common.base.BaseFragment
import com.example.newsdemo.databinding.FragmentNewsBinding
import com.example.newsdemo.main.MainActivity
import com.example.newsdemo.main.MainViewModel

class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    override fun setBinding(): FragmentNewsBinding =
        FragmentNewsBinding.inflate(layoutInflater)

    lateinit var viewModel: MainViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        newsAdapter = NewsAdapter()
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initViewModel() {
        viewModel = (activity as MainActivity).mainViewModel
        viewModel.getMainArticleEntityList().observe(
            viewLifecycleOwner, { news ->
                newsAdapter.differ.currentList.clear()
                newsAdapter.differ.submitList(news)
                binding.recyclerView.setPadding(0, 0, 0, 0)
            })
        viewModel.errorToast.observe(viewLifecycleOwner, { value ->
            if (value.isNotEmpty()) {
                Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
            } else {
                viewModel.hideErrorToast()
            }
        })
    }
}
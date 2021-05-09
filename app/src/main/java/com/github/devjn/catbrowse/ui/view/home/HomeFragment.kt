package com.github.devjn.catbrowse.ui.view.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.github.devjn.catbrowse.databinding.FragmentHomeBinding
import com.github.devjn.catbrowse.ui.adapters.CatAdapter
import com.github.devjn.catbrowse.ui.helpers.OnLoadMoreListener
import com.github.devjn.catbrowse.ui.helpers.RecyclerViewLoadMoreScroll


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val listAdapter = CatAdapter(
            viewModel.allCats,
            onItemClicked = {
                val direction = HomeFragmentDirections.actionNavigateToDetail(it)
                findNavController().navigate(direction)
            },
            onFavoriteClicked = {
                viewModel.onFavoriteClick(it)
            }
        )

        binding.list.apply {
            layoutManager = GridLayoutManager(context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) =
                        when (listAdapter.getItemViewType(position)) {
                            CatAdapter.VIEW_TYPE_ITEM -> 1
                            CatAdapter.VIEW_TYPE_LOADING -> 2
                            else -> -1
                        }
                }
            }
            setHasFixedSize(true)
            adapter = listAdapter
            itemAnimator = DefaultItemAnimator()

            scrollListener = RecyclerViewLoadMoreScroll(layoutManager as GridLayoutManager).apply {
                setOnLoadMoreListener(object : OnLoadMoreListener {
                    override fun onLoadMore() {
                        loadMoreData(listAdapter)
                    }
                })
            }
            addOnScrollListener(scrollListener)
        }
        viewModel.newCats.subscribe {
            handler.post {
                if (listAdapter.notifyNewData(it)) {
                    scrollListener.setLoaded()
                }
            }
        }

        return binding.root
    }

    private fun loadMoreData(adapter: CatAdapter) {
        handler.post {
            adapter.showProgress = true
            viewModel.loadMore()
        }
    }

}

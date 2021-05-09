package com.github.devjn.catbrowse.ui.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.devjn.catbrowse.AppData
import com.github.devjn.catbrowse.databinding.FragmentHomeBinding
import com.github.devjn.catbrowse.ui.adapters.CatAdapter

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val listAdapter = CatAdapter(AppData.getAllFavoriteCats(),
            onItemClicked = {
                val direction = FavoritesFragmentDirections.actionNavigateToDetail(it)
                findNavController().navigate(direction)
            },
            onFavoriteClicked = {
                viewModel.onFavoriteClick(it)
            })

        binding.list.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
        }
        viewModel.cats.observe(viewLifecycleOwner, { listAdapter.updateData(it) })

        return binding.root
    }
}
package com.github.devjn.catbrowse.ui.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.github.devjn.catbrowse.R
import com.github.devjn.catbrowse.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = DetailFragmentBinding.inflate(inflater, container, false)
        val cat = args.cat

        with(binding) {
            Glide.with(this@DetailFragment).load(cat.url).into(image)
            textId.text = getString(R.string.idis, cat.id)
            btnFavorite.apply {
                isSelected = cat.isFavorite
                setOnClickListener {
                    viewModel.onFavoriteClicked(cat)
                    btnFavorite.isSelected = cat.isFavorite
                }
            }

            val description = StringBuilder().apply {
                cat.breeds.forEach {
                    append(it.name)
                    appendLine()
                    appendLine()
                    append(it.description)
                    appendLine()
                }
            }

            binding.textBreeds.text = description.toString()
        }

        return binding.root
    }

}
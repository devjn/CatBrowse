package com.github.devjn.catbrowse.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.devjn.catbrowse.R
import com.github.devjn.catbrowse.data.Cat
import com.github.devjn.catbrowse.databinding.GridItemBinding

class CatAdapter(
    private var items: List<Cat>,
    private val onItemClicked: (Cat) -> Unit,
    private val onFavoriteClicked: (Cat) -> Unit
) : RecyclerView.Adapter<CatAdapter.BindViewHolder>() {
    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BindViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = GridItemBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
            ViewHolder(
                binding,
                { items[it].let(onItemClicked) },
                { items[it].let(onFavoriteClicked) })
        } else {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.progress_loading, viewGroup, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        if (position == items.size) {
            return
        }
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount() = if (showProgress) items.size + 1 else items.size

    var showProgress = false
        set(value) {
            field = value
            if (value) {
                notifyItemInserted(items.size)
            } else {
                notifyItemRemoved(items.size)
            }
        }

    /**
     * This method is used to notify adapter that new data was added to the list of items
     * @return true if data is not empty
     */
    fun notifyNewData(data: List<Cat>): Boolean {
        if (data.isEmpty()) {
            return false
        }
        showProgress = false
        val position = items.size
        notifyItemRangeInserted(position, data.size)
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Cat>) {
        items = data
        notifyDataSetChanged() // Better use DiffUtil, but it is too much overhead for this case
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    abstract class BindViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(cat: Cat)
    }

    class LoadingViewHolder(itemView: View) : BindViewHolder(itemView) {
        override fun bind(cat: Cat) {}
    }

    inner class ViewHolder(
        val itemBinding: GridItemBinding,
        onItemClicked: (Int) -> Unit,
        onFavoriteClicked: (Int) -> Unit
    ) : BindViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener { onItemClicked(adapterPosition) }
            itemBinding.btnFavorite.setOnClickListener {
                onFavoriteClicked(adapterPosition)
                notifyItemChanged(adapterPosition)
            }

        }

        override fun bind(cat: Cat) {
            Glide.with(itemView).load(cat.url).into(itemBinding.image)
            itemBinding.title.text = cat.breeds.getOrNull(0)?.name ?: cat.id
            itemBinding.btnFavorite.isSelected = cat.isFavorite
        }
    }

}

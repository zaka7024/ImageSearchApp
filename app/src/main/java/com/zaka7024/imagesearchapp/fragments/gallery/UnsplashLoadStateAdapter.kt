package com.zaka7024.imagesearchapp.fragments.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zaka7024.imagesearchapp.databinding.UnsplashPhotoLoadStateFooterBinding

class UnsplashLoadStateAdapter(private val retryAction: () -> Unit) :
    LoadStateAdapter<UnsplashLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(
        private val unsplashPhotoLoadStateFooterBinding: UnsplashPhotoLoadStateFooterBinding,
        val retryAction: () -> Unit
    ) :
        RecyclerView.ViewHolder(unsplashPhotoLoadStateFooterBinding.root) {

        init {
            unsplashPhotoLoadStateFooterBinding.retryButton.setOnClickListener {
                retryAction()
            }
        }

        fun bind(loadState: LoadState) {
            unsplashPhotoLoadStateFooterBinding.progressBar.isVisible =
                loadState == LoadState.Loading
            unsplashPhotoLoadStateFooterBinding.retryButton.isVisible =
                loadState != LoadState.Loading
            unsplashPhotoLoadStateFooterBinding.errorTextView.isVisible =
                loadState != LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = UnsplashPhotoLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return LoadStateViewHolder(binding, retryAction)
    }
}
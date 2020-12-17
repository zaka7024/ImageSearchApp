package com.zaka7024.imagesearchapp.fragments.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.zaka7024.imagesearchapp.R
import com.zaka7024.imagesearchapp.data.UnsplashPhoto
import com.zaka7024.imagesearchapp.databinding.UnsplashImageItemBinding

class UnsplashPhotoAdapter(private val onSplashPhotoItemClickListener: OnSplashPhotoItemClickListener) :
    PagingDataAdapter<UnsplashPhoto, UnsplashPhotoAdapter.UnsplashPhotoViewHolder>(
        UNSPLASH_PHOTO_COMPARATOR
    ) {

    inner class UnsplashPhotoViewHolder(private val binding: UnsplashImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if(item != null) {
                        onSplashPhotoItemClickListener.onItemClicked(item)
                    }
                }
            }
        }

        fun bind(unsplashPhoto: UnsplashPhoto) {
            binding.apply {
                Glide.with(itemView)
                    .load(unsplashPhoto.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(unsplashImageView)

                unsplashUserName.text = unsplashPhoto.user.username
            }
        }
    }

    interface OnSplashPhotoItemClickListener {
        fun onItemClicked(unsplashPhoto: UnsplashPhoto)
    }

    override fun onBindViewHolder(holder: UnsplashPhotoViewHolder, position: Int) {
        val unsplashPhoto = getItem(position)
        if (unsplashPhoto != null) {
            holder.bind(unsplashPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsplashPhotoViewHolder {
        val binding =
            UnsplashImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnsplashPhotoViewHolder(binding)
    }

    companion object {
        private val UNSPLASH_PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

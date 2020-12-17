package com.zaka7024.imagesearchapp.fragments.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zaka7024.imagesearchapp.R
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.zaka7024.imagesearchapp.databinding.DetailsFragmentBinding


class DetailsFragment : Fragment(R.layout.details_fragment) {
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DetailsFragmentBinding.bind(view)
        binding.apply {
            val photo = args.unsplashPhoto
            // Load the image
            Glide.with(this@DetailsFragment)
                .load(photo.urls.regular)
                .centerCrop()
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageSrc.isVisible = true
                        imageDescription.isVisible = photo.description != null
                        progressBar.isVisible = false
                        return false
                    }
                })
                .into(imageView)

            imageDescription.text = photo.description
            imageSrc.apply {
                text = "Photo by ${photo.user.name}"
                setOnClickListener {
                    val uri = Uri.parse(photo.user.attributionUrl)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }
                paint.isUnderlineText = true
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.download_menu_item -> {
                downloadTheImage()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun downloadTheImage() {
        val imageUrl = args.unsplashPhoto.urls.regular
        val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        val fileName = System.currentTimeMillis().toString() + ".jpg"

        PRDownloader.download(imageUrl, dirPath, fileName)
            .build()
            .setOnProgressListener {

            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    if(context != null) {
                        Toast.makeText(requireContext(), "Download Complete", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onError(error: com.downloader.Error?) {
                }
            })
    }
}
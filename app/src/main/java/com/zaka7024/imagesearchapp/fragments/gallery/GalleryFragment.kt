package com.zaka7024.imagesearchapp.fragments.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.zaka7024.imagesearchapp.R
import com.zaka7024.imagesearchapp.data.UnsplashPhoto
import com.zaka7024.imagesearchapp.databinding.GalleryFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.gallery_fragment), UnsplashPhotoAdapter.OnSplashPhotoItemClickListener {

    private val galleryViewModel by viewModels<GalleryViewModel>()

    private var _binding: GalleryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = GalleryFragmentBinding.bind(view)

        val adapter = UnsplashPhotoAdapter(this)
        binding.apply {
            recyclerView.hasFixedSize()
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashLoadStateAdapter(retryAction = { adapter.retry() }),
                footer = UnsplashLoadStateAdapter(retryAction = { adapter.retry() })
            )
            retryButton.setOnClickListener { adapter.retry() }
        }

        galleryViewModel.photos.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                errorTextView.isVisible = loadState.source.refresh is LoadState.Error

                // No items
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    emptyResultTextView.isVisible = true
                } else {
                    emptyResultTextView.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.gallery_menu, menu)
        val searchItem = menu.findItem(R.id.search_item)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    galleryViewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // To avoid memory leak
        _binding = null
    }

    override fun onItemClicked(unsplashPhoto: UnsplashPhoto) {
        val navController = findNavController()
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(unsplashPhoto)
        navController.navigate(action)
    }
}
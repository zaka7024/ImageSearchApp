package com.zaka7024.imagesearchapp.data

import android.util.Log
import androidx.paging.PagingSource
import com.zaka7024.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

const val UNSPLASH_PAGE_START_INDEX = 1

class UnsplashPagingSource(private val unsplashApi: UnsplashApi, private val query: String) :
    PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSPLASH_PAGE_START_INDEX
        return try {
            val response =
                unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results
            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_PAGE_START_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            Log.e("UnsplashPagingSource", e.toString())
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("UnsplashPagingSource", e.toString())
            LoadResult.Error(e)
        }
    }
}
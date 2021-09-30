package com.example.pichurchyk_p3.ui.favoritesFragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.adapter.FavoriteImagesAdapter
import com.example.pichurchyk_p3.base.baseFragment.BaseFragment
import com.example.pichurchyk_p3.databinding.FragmentFavoritesImagesBinding
import com.example.pichurchyk_p3.room.Photo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesImagesFragment : BaseFragment<FragmentFavoritesImagesBinding>(),
    FavoriteImagesAdapter.OnItemClick, FavoriteImagesAdapter.OnDeleteClick {

    private val viewModel by viewModels<FavoriteImagesViewModel>()
    private lateinit var adapter: FavoriteImagesAdapter

    private var isLandscape: Boolean = false

    override fun getFragmentView(): Int {
        return R.layout.fragment_favorites_images
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuVisibility(true)

        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            viewModel.allFavoritePhotos
                .collect { adapter.submitList(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentOrientation = resources.configuration.orientation
        isLandscape = currentOrientation == Configuration.ORIENTATION_LANDSCAPE
    }

    private fun setupRecyclerView() {
        adapter = FavoriteImagesAdapter(this, this, isLandscape)
        val recyclerView = binding.favoritesImagesRecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(photo: Any) {
        GlobalScope.launch {
            viewModel.getPhotoById(adapter.clickedItemId)
            val action =
                FavoritesFragmentDirections.actionFavoritesFragmentToImageFullScreenFragment(
                    viewModel.photo[0], ""
                )
            findNavController().navigate(action)
        }
    }

    override fun onDeleteClick(photo: Photo) {
        GlobalScope.launch {
            viewModel.deleteFromFavorites(photo)
        }
    }
}
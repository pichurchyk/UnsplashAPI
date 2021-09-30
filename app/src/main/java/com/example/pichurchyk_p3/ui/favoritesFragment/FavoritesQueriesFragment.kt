package com.example.pichurchyk_p3.ui.favoritesFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.adapter.FavoriteQueriesAdapter
import com.example.pichurchyk_p3.base.baseFragment.BaseFragment
import com.example.pichurchyk_p3.databinding.FragmentFavoritesQueriesBinding
import com.example.pichurchyk_p3.room.Query
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesQueriesFragment : BaseFragment<FragmentFavoritesQueriesBinding>(),
    FavoriteQueriesAdapter.ChangeQueryState, FavoriteQueriesAdapter.OnItemClick {

    private lateinit var adapter: FavoriteQueriesAdapter
    private val viewModel by viewModels<FavoriteQueriesViewModel>()

    override fun getFragmentView(): Int {
        return R.layout.fragment_favorites_queries
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            viewModel.allFavoriteQueries
                .collect {
                    adapter.submitList(it)
                }
        }
    }

    private fun setupRecyclerView() {
        adapter = FavoriteQueriesAdapter(
            this,
            this,
            fun(date: String): String { return viewModel.compareDates(date) })
        val recyclerView = binding.favoritesQueriesRecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun changeQueryState(query: Query) {
        GlobalScope.launch {
            if (query.isLiked) {
                viewModel.changeQueryLikeState(
                    Query(
                        query.queryText,
                        false,
                        query.total,
                        query.date
                    )
                )
            } else {
                viewModel.changeQueryLikeState(
                    Query(
                        query.queryText,
                        true,
                        query.total,
                        query.date
                    )
                )
            }
        }
    }

    override fun openSearchPage(query: String) {
        val action =
            FavoritesFragmentDirections.actionFavoritesFragmentToWallpapersFeedFragment(query)
        findNavController().navigate(action)
    }
}
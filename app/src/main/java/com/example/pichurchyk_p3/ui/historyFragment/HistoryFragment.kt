package com.example.pichurchyk_p3.ui.historyFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.adapter.HistoryFragmentAdapter
import com.example.pichurchyk_p3.base.baseFragment.BaseFragment
import com.example.pichurchyk_p3.databinding.FragmentHistoryBinding
import com.example.pichurchyk_p3.room.Query
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(),
    HistoryFragmentAdapter.ChangeQueryState, HistoryFragmentAdapter.OnItemClick {

    private lateinit var adapter: HistoryFragmentAdapter
    private val viewModel by viewModels<HistoryViewModel>()

    override fun getFragmentView(): Int {
        return R.layout.fragment_history
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.allQueries
                .collect { adapter.submitList(it) }
        }

        binding.clearHistory.setOnClickListener {
            viewModel.deleteQueries()
            snackBar("History is purified")
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = HistoryFragmentAdapter(
            this,
            this,
            fun(date: String): String { return viewModel.compareDates(date) })
        val recyclerView = binding.historyRecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun changeQueryState(query: Query) {
        lifecycleScope.launch {
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
            HistoryFragmentDirections.actionHistoryFragmentToWallpapersFeedFragment(query)
        findNavController().navigate(action)
    }
}
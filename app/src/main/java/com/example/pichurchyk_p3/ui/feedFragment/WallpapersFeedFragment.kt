package com.example.pichurchyk_p3.ui.feedFragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.l2_t1_p.sharedPref.SharedPrefConsts
import com.example.pichurchyk_p3.common.sharedPref.SharedPrefInterface
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.adapter.RecyclerViewStateLoadAdapter
import com.example.pichurchyk_p3.adapter.SearchFragmentAdapter
import com.example.pichurchyk_p3.base.baseFragment.BaseFragment
import com.example.pichurchyk_p3.databinding.FragmentWallpappersFeedBinding
import com.example.pichurchyk_p3.model.UnsplashPhoto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class WallpapersFeedFragment : BaseFragment<FragmentWallpappersFeedBinding>(),
    SearchFragmentAdapter.OnItemClick{

    private lateinit var displayType: ImageButton
    private lateinit var searchLine: EditText
    private lateinit var sharedPref: SharedPrefInterface
    private var timer: Timer? = null

    private var displayColumns = 2

    private val viewModel by viewModels<FeedViewModel>()

    private lateinit var adapter: SearchFragmentAdapter

    private val args by navArgs<WallpapersFeedFragmentArgs>()

    private var isLandscape : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPref = SharedPrefInterface(requireContext())
        if (args.query.isEmpty()) {
            if (sharedPref.getPrefString(SharedPrefConsts.LAST_QUERY) != null) {
                binding.searchRecyclerView.scrollToPosition(0)
                viewModel.searchPhotos(
                    sharedPref.getPrefString(SharedPrefConsts.LAST_QUERY).toString()
                )
            } else {
                viewModel.searchPhotos("Cars")
            }
        } else {
            viewModel.searchPhotos(args.query)
        }

        menuVisibility(true)
        setupRecyclerView()

        displayType = binding.displayStyle

        searchLine = binding.searchField
        searchLine.addTextChangedListener(searchTextWatcher)

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        displayType.setOnClickListener {
            when (displayColumns) {
                2 -> {
                    binding.displayStyle.setImageResource(R.drawable.ic_2_columns)
                    displayColumns = 3
                    (binding.searchRecyclerView.layoutManager as GridLayoutManager).spanCount = 3
                }
                3 -> {
                    binding.displayStyle.setImageResource(R.drawable.ic_3_columns)
                    displayColumns = 2
                    (binding.searchRecyclerView.layoutManager as GridLayoutManager).spanCount = 2
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentOrientation = resources.configuration.orientation
        isLandscape = currentOrientation == Configuration.ORIENTATION_LANDSCAPE
    }

    private val searchTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(arg0: Editable) {
            if (!searchLine.text.isNullOrEmpty()) {
                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        Handler(Looper.getMainLooper()).postDelayed({
                            searchPhoto(searchLine.text.toString())
                            hideKeyboard()
                            requireActivity().window.decorView.clearFocus()
                        }, 0)
                    }
                }, 2000)
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (timer != null) {
                timer?.cancel()
            }
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_wallpappers_feed
    }

    private fun setupRecyclerView() {
        adapter = SearchFragmentAdapter(this, isLandscape)
        adapter.addLoadStateListener { loadState ->
            binding.loadState.progress.isVisible = loadState.source.refresh is LoadState.Loading
            binding.searchRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.loadState.loadingErrorButton.isVisible =
                loadState.source.refresh is LoadState.Error
            binding.loadState.loadingErrorTitle.isVisible =
                loadState.source.refresh is LoadState.Error

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                adapter.itemCount < 1
            ) {
                binding.searchRecyclerView.visibility = View.GONE
            }
        }

        val footerAdapter = RecyclerViewStateLoadAdapter { adapter.retry() }
        val recyclerView = binding.searchRecyclerView
        val layoutManager = GridLayoutManager(requireContext(), displayColumns)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter.withLoadStateFooter(
            footer = footerAdapter
        )
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        binding.loadState.loadingErrorButton.setOnClickListener {
            adapter.retry()
        }
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val action =
            WallpapersFeedFragmentDirections.actionWallpapersFeedFragmentToImageFullScreenFragment(
                photo, viewModel.getCurrentQuery()
            )
        findNavController().navigate(action)
    }

    private fun searchPhoto(query: String) {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding.searchRecyclerView.scrollToPosition(0)
        viewModel.searchPhotos(query)
        searchLine.text = null
        viewModel.addQuery()
        sharedPref.setPref(SharedPrefConsts.LAST_QUERY, query)
    }
}
package com.example.pichurchyk_p3.ui.fullScreenImageFragment

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.base.baseFragment.BaseFragment
import com.example.pichurchyk_p3.databinding.BottomPopupMenuBinding
import com.example.pichurchyk_p3.databinding.FragmentImageFullScreenBinding
import com.example.pichurchyk_p3.databinding.ImgInfoPopupBinding
import com.example.pichurchyk_p3.databinding.RecyclerAdapterLoaderBinding
import com.example.pichurchyk_p3.model.UnsplashPhoto
import com.example.pichurchyk_p3.room.Photo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_image_full_screen.view.*
import kotlinx.android.synthetic.main.img_info_popup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


@SuppressLint("SetJavaScriptEnabled")
@AndroidEntryPoint
class ImageFullScreenFragment : BaseFragment<FragmentImageFullScreenBinding>() {
    override fun getFragmentView(): Int {
        return R.layout.fragment_image_full_screen
    }

    private var isPhotoExist = false

    private lateinit var toolbar: Toolbar
    private lateinit var loadState: RecyclerAdapterLoaderBinding
    private lateinit var btnFullScreen: FloatingActionButton
    private lateinit var btnHideInfo: FloatingActionButton
    private lateinit var btnShare: ImageButton
    private lateinit var btnInfo: ImageButton
    private lateinit var btnOptions: ImageButton
    private lateinit var settingsBar: LinearLayout

    private var isSettingsVisible = true
    private var popupWindow: PopupWindow? = null

    private lateinit var photo: UnsplashPhoto
    private val args by navArgs<ImageFullScreenFragmentArgs>()
    private val viewModel by viewModels<ImageFullScreenViewModel>()

    private lateinit var res: Resources

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        res = binding.root.resources
        binding.blackout.animate().alpha(0.0f)

        viewModel.setQuery(args.queryName)
        viewModel.setPhoto(args.photo)
        photo = viewModel.getPhoto()
        val query = viewModel.validateQueryLength()

        toolbar = binding.toolbar
        loadState = binding.loadState
        btnFullScreen = binding.buttonFullScreen
        btnHideInfo = binding.buttonHideInfo
        btnShare = binding.shareBtn
        btnInfo = binding.infoBtn
        btnOptions = binding.optionsBtn
        settingsBar = binding.settingsBar

        toolbar.toolbar_title.text = query
        toolbar.toolbar_btn_back.setOnClickListener {
            if (popupWindow != null) {
                hideInfoWindow()
                hideOptionsWindow()
            } else {
                moveBackToFeed()
            }
        }

        loadState(isVisible = true, isError = false)
        loadImage()

        binding.loadState.loadingErrorButton.setOnClickListener {
            loadState(isVisible = true, isError = true)
            loadImage()
        }

        btnFullScreen.setOnClickListener {
            imgFullScreen()
        }

        btnShare.setOnClickListener {
            sharePhoto(photo.urls.regular)
        }

        btnOptions.setOnClickListener {
            btnFullScreen.visibility = View.INVISIBLE
            binding.blackout.animate().alpha(1.0f).duration = 400
            showOptionsMenu()
        }

        btnInfo.setOnClickListener {
            btnFullScreen.visibility = View.INVISIBLE
            binding.blackout.animate().alpha(1.0f).duration = 400
            toolbar.visibility = View.GONE
            showImgInfo()
        }

        GlobalScope.launch(Dispatchers.Default) {
            if (viewModel.isPhotoExists(photo.id)) {
                isPhotoExist = true
            }
        }

        menuVisibility(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (popupWindow != null) {
                    hideOptionsWindow()
                    hideInfoWindow()
                } else {
                    moveBackToFeed()
                }
            }
        })
    }

    private fun imgFullScreen() {
        if (isSettingsVisible) {
            toolbar.animate().translationY(-300f).duration = 300
            toolbar.visibility = View.GONE
            btnFullScreen.setImageResource(R.drawable.ic_full_screen_exit)
            isSettingsVisible = false
        } else {
            toolbar.animate().translationY(0f).duration = 300
            toolbar.visibility = View.VISIBLE
            btnFullScreen.setImageResource(R.drawable.ic_full_screen)
            isSettingsVisible = true
        }
        changeSettingsBarVisibility()
    }

    private fun changeSettingsBarVisibility() {
        if (isSettingsVisible) {
            settingsBar.animate().translationY(0f).duration = 300
        } else {
            settingsBar.animate().translationY(400f).duration = 300
        }
    }

    private fun loadImage() {
        Glide.with(this@ImageFullScreenFragment)
            .load(photo.urls.full)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    loadState(isVisible = true, isError = true)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    btnFullScreen.visibility = View.VISIBLE
                    settingsBar.visibility = View.VISIBLE
                    btnFullScreen.visibility = View.VISIBLE
                    loadState(isVisible = false, isError = false)
                    return false
                }

            })
            .into(binding.imageView)
    }

    private fun sharePhoto(imgUrl: String) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, imgUrl)
            type = "text/*"
        }
        startActivity(Intent.createChooser(shareIntent, "Share to"))
    }

    private fun showOptionsMenu() {
        val inflater: LayoutInflater =
            binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.bottom_popup_menu, null)

        val popupWidth = LinearLayout.LayoutParams.MATCH_PARENT
        val popupHeight = LinearLayout.LayoutParams.WRAP_CONTENT
        popupWindow = PopupWindow(popupView, popupWidth, popupHeight)
        popupWindow?.animationStyle = R.style.popup_animation
        popupWindow?.showAtLocation(view, Gravity.BOTTOM, 0, 0)

        val popupBinding = BottomPopupMenuBinding.bind(popupView)

        if (isPhotoExist) {
            changeIconColor(popupBinding.likeIcon.drawable, R.color.blue)
        }

        popupBinding.setImgHomeScreen.setOnClickListener {
            setHomeScreenWallpaper()
        }
        popupBinding.setImgLockScreen.setOnClickListener {
            setLockScreenWallpaper()
        }
        popupBinding.likeImg.setOnClickListener {
            if (isPhotoExist) {
                snackBar(res.getString(R.string.favorite_photo_exists))
            } else {
                isPhotoExist = true
                viewModel.likePhoto(Photo(0, photo.id, photo.urls.full))
                snackBar(res.getString(R.string.favorite_photo_success))
                changeIconColor(popupBinding.likeIcon.drawable, R.color.blue)
            }
            hidePopups()
        }

        binding.fullScreenFragment.setOnClickListener {
            hideOptionsWindow()
        }
    }

    private fun hidePopups() {
        binding.blackout.animate().alpha(0f).duration = 400
        btnFullScreen.visibility = View.VISIBLE
        popupWindow?.dismiss()
        popupWindow = null
    }

    private fun moveBackToFeed() {
        Navigation.findNavController(requireView()).popBackStack()
    }

    private fun setHomeScreenWallpaper() {
        val imgBitmap = (binding.imageView.drawable as BitmapDrawable).bitmap
        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(requireContext())
        wallpaperManager.setBitmap(imgBitmap)
        snackBar(res.getString(R.string.wallpapers_home_success))
        hideOptionsWindow()
    }

    private fun setLockScreenWallpaper() {
        val imgBitmap = (binding.imageView.drawable as BitmapDrawable).bitmap
        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(requireContext())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wallpaperManager.setBitmap(imgBitmap, null, true, WallpaperManager.FLAG_LOCK)
            snackBar(res.getString(R.string.wallpapers_lock_success))
        } else {
            dialog(
                res.getString(R.string.wallpapers_alert_title),
                res.getString(R.string.wallpapers_alert_text),
                binding.root.context
            )
        }
        hideOptionsWindow()
    }

    private fun showImgInfo() {
        val inflater: LayoutInflater =
            binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.img_info_popup, null)
        val popupWidth = LinearLayout.LayoutParams.MATCH_PARENT
        val popupHeight = LinearLayout.LayoutParams.WRAP_CONTENT
        popupWindow = PopupWindow(popupView, popupWidth, popupHeight)

        val popupBinding = ImgInfoPopupBinding.bind(popupView)
        popupBinding.photo = photo
        popupBinding.user = photo.user

        popupWindow?.animationStyle = R.style.popup_animation
        popupWindow?.showAtLocation(view, Gravity.BOTTOM, 0, 0)

        popupBinding.imgDate.text = viewModel.getPhotoValidatedDate()

        if (!photo.user.instagram_username.isNullOrEmpty()) {
            popupBinding.userInsta.text = photo.user.instagram_username
        }

        if (!photo.user.twitter_username.isNullOrEmpty()) {
            popupBinding.userTwitter.text = photo.user.twitter_username
        }

        if (!photo.description.isNullOrEmpty()) {
            if (photo.description?.length!! > 30) {
                popupBinding.photoTitle.text = String.format(res.getString(R.string.photo_title), photo.description!!.substring(0, 30))
                popupBinding.photoTitle.setOnClickListener {
                    popupBinding.photoTitle.text = photo.description
                }
            } else {
                popupBinding.photoTitle.text = photo.description
            }
        } else {
            popupBinding.photoTitle.visibility = View.GONE
        }

        if (!photo.user.instagram_username.isNullOrEmpty()) {
            popupBinding.userInsta.setOnClickListener {
                openBrowserLink("https://www.instagram.com/${photo.user.instagram_username}/")
                hidePopups()
            }
        } else {
            popupBinding.userInsta.visibility = View.GONE
        }

        if (!photo.user.twitter_username.isNullOrEmpty()) {
            popupBinding.userTwitter.setOnClickListener {
                openBrowserLink("https://twitter.com/${photo.user.twitter_username}")
                hidePopups()
            }
        } else {
            popupBinding.userTwitter.visibility = View.GONE
        }

        if (photo.user.profile_image != null) {
            Glide.with(this@ImageFullScreenFragment)
                .load(photo.user.profile_image?.large)
                .centerCrop()
                .into(popupBinding.userImg)
        }

        popupBinding.btnUserPortfolio.setOnClickListener {
            if (!photo.user.portfolio_url.isNullOrEmpty()) {
                openBrowserLink(photo.user.portfolio_url!!)
                hidePopups()
            } else snackBar(res.getString(R.string.user_no_portfolio))
        }

        binding.fullScreenFragment.setOnClickListener {
            hideInfoWindow()
        }
        toolbar.visibility = View.GONE
        btnHideInfo.visibility = View.VISIBLE
        btnHideInfo.setOnClickListener {
            hideInfoWindow()
            moveBackToFeed()
        }
    }

    private fun openBrowserLink(link: String) {
        val action =
            ImageFullScreenFragmentDirections.actionImageFullScreenFragmentToBrowserFragment(
                link
            )
        findNavController().navigate(action)
    }

    private fun hideInfoWindow() {
        hidePopups()
        toolbar.visibility = View.VISIBLE
        btnHideInfo.visibility = View.GONE
        btnFullScreen.visibility = View.VISIBLE
    }

    private fun hideOptionsWindow() {
        hidePopups()
        binding.blackout.animate().alpha(0.0f).duration = 400
        btnFullScreen.visibility = View.VISIBLE
    }

    private fun loadState(isVisible: Boolean, isError: Boolean) {
        if (isVisible) {
            loadState.progress.visibility = View.VISIBLE
            loadState.loadingErrorButton.visibility = View.GONE
            loadState.loadingErrorTitle.visibility = View.GONE
            if (isError) {
                loadState.progress.visibility = View.GONE
                loadState.loadingErrorButton.visibility = View.VISIBLE
                loadState.loadingErrorTitle.visibility = View.VISIBLE
            }
        } else {
            loadState.progress.visibility = View.GONE
            loadState.loadingErrorButton.visibility = View.GONE
            loadState.loadingErrorTitle.visibility = View.GONE
        }
    }
}
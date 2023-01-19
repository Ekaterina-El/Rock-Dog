package el.ka.rockdog.view.ui.fullScreenViewer

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import el.ka.rockdog.R
import el.ka.rockdog.databinding.FullScreenViewerBinding
import el.ka.rockdog.view.adapter.lists.photos.FullScreenPhotoViewHolder
import el.ka.rockdog.view.adapter.lists.photos.FullScreenPhotosAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ArtistViewModel
import el.ka.rockdog.viewModel.FullScreenViewerViewModel
import java.util.Objects

class FullScreenViewerFragment : BaseFragment() {
  private lateinit var binding: FullScreenViewerBinding
  private lateinit var viewModel: FullScreenViewerViewModel
  private val artistViewModel by activityViewModels<ArtistViewModel>()


  private lateinit var photosAdapter: FullScreenPhotosAdapter

  private val photosObserver = Observer<Array<String>> {
    if (it.isEmpty()) goBack()
    else photosAdapter.setItems(it.toList())
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[FullScreenViewerViewModel::class.java]
    photosAdapter = FullScreenPhotosAdapter()

    binding = FullScreenViewerBinding.inflate(layoutInflater)
    binding.apply {
      viewModel = this@FullScreenViewerFragment.viewModel
      master = this@FullScreenViewerFragment
      photosAdapter = this@FullScreenViewerFragment.photosAdapter
      lifecycleOwner = viewLifecycleOwner
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    getPhotosFromArguments()
    initPopupMenu()

    binding.viewPager.registerOnPageChangeCallback(viewPagerCallback)
  }

  private val viewPagerCallback by lazy {
    object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        if (viewModel.isStart && photosAdapter.itemCount != 0) {
          binding.viewPager.currentItem = viewModel.currentPos.value!!
          viewModel.isStart = false
        } else {
          viewModel.updateCurrentPos(position)
        }

        super.onPageSelected(position)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    binding.viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
  }

  private fun getPhotosFromArguments() {
    val bundle = FullScreenViewerFragmentArgs.fromBundle(requireArguments())
    viewModel.setPhotos(bundle.artistId, bundle.photos, bundle.currentPos)
  }

  override fun onResume() {
    super.onResume()
    viewModel.photos.observe(viewLifecycleOwner, photosObserver)
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.error.observe(viewLifecycleOwner, errorObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.photos.removeObserver(photosObserver)
    viewModel.work.removeObserver(workObserver)
    viewModel.error.removeObserver(errorObserver)
  }

  private lateinit var popupMenu: PopupMenu

  private fun initPopupMenu() {
    popupMenu = PopupMenu(context, binding.verticalMenu)

    popupMenu.menu.add(0, SAVE_PHOTO, Menu.NONE, requireContext().getString(R.string.download))
    popupMenu.menu.add(0, DELETE_PHOTO, Menu.NONE, requireContext().getString(R.string.delete))

    popupMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        SAVE_PHOTO -> downloadPhoto()
        DELETE_PHOTO -> deletePhoto()
        else -> {}
      }
      return@setOnMenuItemClickListener true
    }
  }

  fun openPopupMenu() {
    popupMenu.show()
  }

  private fun deletePhoto() {
    val a = when (val current = viewModel.currentPos.value!!) {
      0 -> 1
      1 -> 0
      else -> current - 1
    }
    if (viewModel.photos.value!!.size == 1) binding.photosAdapter = null
    viewModel.deleteCurrentPhoto(a) {
      artistViewModel.setPhotos(it)
    }
  }

  private fun downloadPhoto() {
    val pos = viewModel.currentPos.value!!
    val current = (binding.viewPager[0] as RecyclerView).findViewHolderForAdapterPosition(pos) ?: return
    val bitmap = (current as FullScreenPhotoViewHolder).binding.image.drawable.toBitmap()

    val images = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    val cv = ContentValues()
    val cr = requireContext().contentResolver
    val time = System.currentTimeMillis()
    cv.put(MediaStore.Images.Media.DISPLAY_NAME, "$time.jpg")
    cv.put(MediaStore.Images.Media.MIME_TYPE, "images/*")
    val uri = cr.insert(images, cv)!!

    try {
      val os = cr.openOutputStream(Objects.requireNonNull(uri))
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
      Objects.requireNonNull(os)

      Toast.makeText(requireContext(), "Image saved", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
      Toast.makeText(requireContext(), "Image saved", Toast.LENGTH_SHORT).show()
      e.printStackTrace()
    }
  }

  companion object {
    const val DELETE_PHOTO = 1
    const val SAVE_PHOTO = 2
  }
}
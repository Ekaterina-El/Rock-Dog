package el.ka.rockdog.view.ui.fullScreenViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import el.ka.rockdog.R
import el.ka.rockdog.databinding.FullScreenViewerBinding
import el.ka.rockdog.view.adapter.lists.photos.FullScreenPhotosAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ArtistViewModel
import el.ka.rockdog.viewModel.FullScreenViewerViewModel

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
    popupMenu.menu.add(0, DELETE_PHOTO, Menu.NONE, requireContext().getString(R.string.delete))
    popupMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        DELETE_PHOTO -> deletePhoto()
        else -> {}
      }
      return@setOnMenuItemClickListener true
    }
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

  fun openPopupMenu() {
    popupMenu.show()
  }


  companion object {
    const val DELETE_PHOTO = 1
  }
}
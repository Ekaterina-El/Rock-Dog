package el.ka.rockdog.view.ui.fullScreenViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import el.ka.rockdog.databinding.FullScreenViewerBinding
import el.ka.rockdog.view.adapter.lists.photos.FullScreenPhotosAdapter
import el.ka.rockdog.view.adapter.lists.photos.PhotosAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.FullScreenViewerViewModel

class FullScreenViewerFragment: BaseFragment() {
  private lateinit var binding: FullScreenViewerBinding
  private lateinit var viewModel: FullScreenViewerViewModel

  private lateinit var photosAdapter: FullScreenPhotosAdapter

  private val photosObserver = Observer<Array<String>> {
    if (it.isEmpty()) goBack()
    photosAdapter.setItems(it.toList())
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

    binding.viewPager.registerOnPageChangeCallback(viewPagerCallback)
  }

  private val viewPagerCallback by lazy {
    object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        viewModel.updateCurrentPos(position)
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
    viewModel.setPhotos(bundle.photos, bundle.currentPos)
  }

  override fun onResume() {
    super.onResume()
    viewModel.photos.observe(viewLifecycleOwner, photosObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.photos.removeObserver(photosObserver)
  }
}
package el.ka.rockdog.view.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageOptions
import el.ka.rockdog.databinding.ArtistProfileFragmentBinding
import el.ka.rockdog.other.CropOptions
import el.ka.rockdog.other.GridSpacingItemDecoration
import el.ka.rockdog.other.ImageChanger
import el.ka.rockdog.view.adapter.lists.photos.PhotosAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ArtistViewModel

class ArtistProfileFragment : BaseFragment() {
  private lateinit var binding: ArtistProfileFragmentBinding
  private lateinit var viewModel: ArtistViewModel

  private lateinit var photosAdapter: PhotosAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[ArtistViewModel::class.java]

    photosAdapter = PhotosAdapter(limit = 6) { url -> openGallerySince(url) }

    binding = ArtistProfileFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@ArtistProfileFragment
      photosAdapter = this@ArtistProfileFragment.photosAdapter
      viewModel = this@ArtistProfileFragment.viewModel
    }

    return binding.root
  }

  private fun openGallerySince(url: String) {

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    imageChanger = ImageChanger(this)
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    getArtistFromArgs()

    val decoration = GridSpacingItemDecoration(3, 6, false)
    binding.artistImages.addItemDecoration(decoration)
  }

  private fun getArtistFromArgs() {
    val bundle = ArtistProfileFragmentArgs.fromBundle(requireArguments())
    val artist = bundle.artist
    viewModel.setArtist(artist)
  }

  override fun onResume() {
    super.onResume()
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.error.observe(viewLifecycleOwner, errorObserver)

    val list = listOf(
      "https://sun9-22.userapi.com/impg/RN0SJlcq1ZBzxQanoMi1vaLVQPBrIDaUYhp3kw/6P_dY7_6RLw.jpg?size=973x2160&quality=95&sign=a4be2ec028a94dc275cababc64a9eec5&type=album",
      "https://sun9-79.userapi.com/impg/iDUeTjNRoXtT5qO0gzzcdo_6nKWMIuCCLdpmlA/taLhox3ihb8.jpg?size=900x1600&quality=95&sign=b45a320b0f97db160eb7c76456b77e58&type=album",
      "https://sun9-74.userapi.com/impg/hVazjBrYJRG_P4ChW-nF10JSWCAmH5qs4_QHPA/R5Yw9snYFC0.jpg?size=1280x606&quality=95&sign=79a4880e6935726560fdb4fbe0422892&type=album",
      "https://sun9-8.userapi.com/impg/V3t7DW5fIkVdhAhOaOH0q4ACCw_xqZQkEdI9gw/s6VbXffrdxM.jpg?size=957x1280&quality=96&sign=ecab8502419fd96962a155bb319a0300&type=album",
      "https://sun9-50.userapi.com/impg/5Osh-D0bw3dUrT0GF6CcUej0QgkgfO5pMHC2ew/9lUuy6Axyr8.jpg?size=2560x1211&quality=96&sign=5bc484c0680c13ada8590f100a72d0ae&type=album",
      "https://sun9-83.userapi.com/impg/Wf8YaFrgbhFTcHWgv-vDHQJi_5tYKDkowchT3A/exTSnMeWrVw.jpg?size=2560x1211&quality=96&sign=2ae834d572454af0911af541f36c70c9&type=album",
      "https://sun9-50.userapi.com/impg/7IHv3gV-1mo1JVzCVBEuUbfpLq4wUwLfcMdCag/FgOq1LPsKAw.jpg?size=2560x1915&quality=96&sign=6dc221e38b718b8d11a2b67079777bcb&type=album"
    )
    photosAdapter.setItems(list)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.work.removeObserver(workObserver)
    viewModel.error.removeObserver(errorObserver)
  }

  // region Change profile image
  private lateinit var imageChanger: ImageChanger

  fun changeCoverImage() = imageChanger.change(CropOptions.rectCropImageOptions) { uri ->
    viewModel.updateCover(uri)
  }
  // endregion
}
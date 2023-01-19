package el.ka.rockdog.view.ui.artist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageOptions
import el.ka.rockdog.databinding.ArtistProfileFragmentBinding
import el.ka.rockdog.other.CropOptions
import el.ka.rockdog.other.GridSpacingItemDecoration
import el.ka.rockdog.other.ImageChanger
import el.ka.rockdog.service.model.BandMember
import el.ka.rockdog.view.adapter.lists.bandMembers.BandMembersAdapter
import el.ka.rockdog.view.adapter.lists.photos.PhotosAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ArtistViewModel

class ArtistProfileFragment : BaseFragment() {
  private lateinit var binding: ArtistProfileFragmentBinding
  private val viewModel by activityViewModels<ArtistViewModel>()


  private lateinit var photosAdapter: PhotosAdapter
  private val photosObserver = Observer<List<String>> {
    photosAdapter.setItems(it)
  }

  private lateinit var bandMemberAdapter: BandMembersAdapter
  private val bandMemberObserver = Observer<List<BandMember>> {
    bandMemberAdapter.setItems(it)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    photosAdapter = PhotosAdapter(limit = 6) { url -> openGallerySince(url) }
    bandMemberAdapter = BandMembersAdapter()

    binding = ArtistProfileFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@ArtistProfileFragment
      bandMembersAdapter = this@ArtistProfileFragment.bandMemberAdapter
      photosAdapter = this@ArtistProfileFragment.photosAdapter
      viewModel = this@ArtistProfileFragment.viewModel
    }

    return binding.root
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
    viewModel.photos.observe(viewLifecycleOwner, photosObserver)
    viewModel.bandMembers.observe(viewLifecycleOwner, bandMemberObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.work.removeObserver(workObserver)
    viewModel.error.removeObserver(errorObserver)
    viewModel.photos.removeObserver { photosObserver }
    viewModel.bandMembers.removeObserver { bandMemberObserver }
  }

  // region Images
  private lateinit var imageChanger: ImageChanger

  fun changeCoverImage() = imageChanger.change(CropOptions.rectCropImageOptions) { uri ->
    viewModel.updateCover(uri)
  }

  fun addPhoto() = imageChanger.change(CropOptions.freeCropImageOptions) { uri ->
    viewModel.addPhoto(uri)
  }
  // endregion

  private fun openGallerySince(url: String) {
    val artistId = viewModel.artist.value!!.id
    val photos = viewModel.photos.value!!
    val current = photos.indexOf(url)

    val direction = ArtistProfileFragmentDirections.actionArtistProfileFragmentToFullScreenViewerFragment(
      photos.toTypedArray(), artistId, current
    )
    findNavController(). navigate(direction)
  }
}
package el.ka.rockdog.view.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import el.ka.rockdog.databinding.ArtistProfileFragmentBinding
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ArtistViewModel

class ArtistProfileFragment : BaseFragment() {
  private lateinit var binding: ArtistProfileFragmentBinding
  private lateinit var viewModel: ArtistViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[ArtistViewModel::class.java]

    binding = ArtistProfileFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@ArtistProfileFragment
      viewModel = this@ArtistProfileFragment.viewModel
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    getArtistFromArgs()
  }

  private fun getArtistFromArgs() {
    val bundle = ArtistProfileFragmentArgs.fromBundle(requireArguments())
    val artist = bundle.artist
    viewModel.setArtist(artist)
  }

  fun changeProfileImage() {

  }

}
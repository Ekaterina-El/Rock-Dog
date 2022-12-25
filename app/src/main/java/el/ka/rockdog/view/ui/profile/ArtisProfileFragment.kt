package el.ka.rockdog.view.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.databinding.ArtistProfileFragmentBinding
import el.ka.rockdog.view.ui.BaseFragment

class ArtisProfileFragment : BaseFragment() {
  private lateinit var binding: ArtistProfileFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = ArtistProfileFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@ArtisProfileFragment
    }
    return binding.root
  }

  fun goBack() {
    findNavController().popBackStack()
  }
}
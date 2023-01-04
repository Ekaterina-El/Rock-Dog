package el.ka.rockdog.view.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.databinding.AboutFragmentBinding
import el.ka.rockdog.view.ui.BaseFragment

class AboutFragment : BaseFragment() {
  private lateinit var binding: AboutFragmentBinding


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = AboutFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@AboutFragment
    }
    return binding.root
  }

}
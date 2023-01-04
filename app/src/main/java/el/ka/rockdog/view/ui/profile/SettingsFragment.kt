package el.ka.rockdog.view.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.databinding.SettingsFragmentBinding
import el.ka.rockdog.view.ui.BaseFragment

class SettingsFragment : BaseFragment() {
  private lateinit var binding: SettingsFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = SettingsFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@SettingsFragment
    }
    return binding.root
  }

}
package el.ka.rockdog.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.databinding.AccountFragmentBinding
import el.ka.rockdog.databinding.PlayerFragmentBinding
import el.ka.rockdog.service.model.User

class AccountFragment: Fragment() {
  private lateinit var binding: AccountFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = AccountFragmentBinding.inflate(layoutInflater)

    val user = User(
      name = "Ekaterina El",
      profileUrl = "https://sun3-10.userapi.com/s/v1/ig2/LeEuVIPGImMW952DTGZU3nHdYuCXmQ0v6DLU7ojVB_Qwno_1GIWPFS2ajAv2ux-lw8XHsSmVCyI22M25EHPxwO6N.jpg?size=200x200&quality=96&crop=0,64,606,606&ava=1"
    )
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@AccountFragment
      this.user = user
    }

    return binding.root
  }

  fun goBack() {
    findNavController().popBackStack()
  }
}
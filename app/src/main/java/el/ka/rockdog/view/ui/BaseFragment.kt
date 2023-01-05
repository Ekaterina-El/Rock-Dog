package el.ka.rockdog.view.ui

import android.app.Dialog
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.MainActivity
import el.ka.rockdog.R
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.view.dialog.ErrorDialog


open class BaseFragment : Fragment() {
  val workObserver = Observer<List<Work>> {
    if (it.isEmpty()) hideLoadingDialog() else showLoadingDialog()
  }

  val errorObserver = Observer<ErrorApp?> {
    if (it != null) showErrorDialog(it)
  }

  private fun getLoadingDialog(): Dialog {
    val activity = requireActivity() as MainActivity
    if (activity.loadingDialog == null) createLoadingDialog()
    return activity.loadingDialog!!
  }

  fun showLoadingDialog() {
    val dialog = getLoadingDialog()
    if (!dialog.isShowing) dialog.show()
  }

  fun hideLoadingDialog() {
    val dialog = getLoadingDialog()
    dialog.dismiss()
  }

  private fun createLoadingDialog() {
    val loadingDialog = Dialog(requireContext(), R.style.AppTheme_FullScreenDialog)
    loadingDialog.setContentView(R.layout.fragment_loading_progress_bar)
    loadingDialog.window!!.setLayout(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.MATCH_PARENT,
    )
    loadingDialog.window!!.setWindowAnimations(R.style.Slide)
    loadingDialog.setCancelable(false)

    val activity = requireActivity() as MainActivity
    activity.loadingDialog = loadingDialog
  }

  private val errorDialog by lazy { ErrorDialog(requireContext()) }

  fun showErrorDialog(error: ErrorApp) {
    val message = getString(error.messageRes)
    errorDialog.openConfirmDialog(message)
  }

  fun restartApp() {
    val i = requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
      ?: return
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(i)
    ActivityCompat.finishAfterTransition(requireActivity())
  }

  fun goBack() {
    findNavController().popBackStack()
  }
}
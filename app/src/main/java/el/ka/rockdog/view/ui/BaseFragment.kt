package el.ka.rockdog.view.ui

import android.app.Dialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import el.ka.rockdog.MainActivity
import el.ka.rockdog.R

open class BaseFragment: Fragment() {
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
}
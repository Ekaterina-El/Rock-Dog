package el.ka.rockdog.other

import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

object CropOptions {
  private fun getCropOptions(byY: Int, byX: Int) = CropImageOptions(
    guidelines = CropImageView.Guidelines.ON,
    aspectRatioY = byY, aspectRatioX = byX, fixAspectRatio = true
  )

  val rectCropImageOptions = getCropOptions(1, 1)
}
package ie.headway.app.htdi_companion.camera.capture;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;

class ScaledJpegCallback {

  private final Resources mResources;

  public ScaledJpegCallback(final Resources resources) {
    mResources = resources;
  }

  public void onPictureTaken(final byte[] data, final Camera camera) {
    if (mResources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//      rotateCapturedBitmap(90.0f);
    }
  }

  private int getScreenWidth() {
    return mResources.getDisplayMetrics().widthPixels;
  }

  private int getScreenHeight() {
    return mResources.getDisplayMetrics().heightPixels;
  }

  private Bitmap scaleAndRotateCapturedBitmap(final Bitmap capturedBitmap, final float degrees) {

    final int height = getScreenHeight();
    final int width = getScreenWidth();

    final Bitmap scaledCapturedBitmap = Bitmap.createScaledBitmap(capturedBitmap, height, width, true);

    final int bitmapWidth = scaledCapturedBitmap.getWidth();
    final int bitmapHeight = scaledCapturedBitmap.getHeight();

    final Matrix mtx = new Matrix();
    mtx.postRotate(degrees);

    final Bitmap rotatedCapturedBitmap =
        Bitmap.createBitmap(scaledCapturedBitmap, 0, 0, bitmapWidth, bitmapHeight, mtx, true);

    return rotatedCapturedBitmap;
  }

}

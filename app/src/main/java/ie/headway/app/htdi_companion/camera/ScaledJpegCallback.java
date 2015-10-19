package ie.headway.app.htdi_companion.camera;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;

public class ScaledJpegCallback extends JpegCallback {

  private final Resources mResources;

  public ScaledJpegCallback(final Resources resources) {
    mResources = resources;
  }

  @Override
  public void onPictureTaken(final byte[] data, final Camera camera) {
    super.onPictureTaken(data, camera);

    final int screenWidth = getScreenWidth();
    final int screenHeight = getScreenHeight();

    scaleCapturedBitmap(screenWidth, screenHeight);

    if (mResources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      rotateCapturedBitmap(90.0f);
    }
  }

  private int getScreenWidth() {
    return mResources.getDisplayMetrics().widthPixels;
  }

  private int getScreenHeight() {
    return mResources.getDisplayMetrics().heightPixels;
  }

  private void scaleCapturedBitmap(final int width, final int height) {
    final Bitmap capturedBitmap = getCapturedBitmap();
    final Bitmap scaledCapturedBitmap = Bitmap.createScaledBitmap(capturedBitmap, height, width, true);
    setCapturedBitmap(scaledCapturedBitmap);
  }

  private void rotateCapturedBitmap(final float degrees) {
    final Bitmap capturedBitmap = getCapturedBitmap();

    final int bitmapWidth = capturedBitmap.getWidth();
    final int bitmapHeight = capturedBitmap.getHeight();

    final Matrix mtx = new Matrix();
    mtx.postRotate(degrees);

    final Bitmap rotatedCapturedBitmap =
        Bitmap.createBitmap(capturedBitmap, 0, 0, bitmapWidth, bitmapHeight, mtx, true);

    setCapturedBitmap(rotatedCapturedBitmap);
  }

}

//    try {
////            writeRawDataToFile(data, mFile);
//      writeBitmapToFile(bm, mFile);
//    } catch (IOException e) {
//      Log.e(TAG, "exception on line: writeBitmapToFile(bitmap, file)", e);
//    }

//    try {
//      mCamView.refreshCameraView();
//    } catch (Exception e) {
//      Log.e("mo", "Error occurred", e);
//    }

//  private void writeBitmapToFile(final Bitmap bitmap, final File file) throws IOException {
//    FileOutputStream out = new FileOutputStream(file);
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//    // PNG is a lossless format, the compression factor (100) is ignored
//    if (out != null) {
//      out.close();
//    }
//  }

//  public void setCamView(final CameraView camView) {
//    mCamView = camView;
//  }
//
//  public void setFile(File file) {
//    mFile = file;
//  }

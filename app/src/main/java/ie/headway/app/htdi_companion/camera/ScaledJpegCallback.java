package ie.headway.app.htdi_companion.camera;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;

/**
 * TODO: This class can be split up so that it extemds JpegCallback and adds the scaling stuff here.
 * */
public class ScaledJpegCallback implements Camera.PictureCallback {

  private final Resources mResources;

  public ScaledJpegCallback(final Resources resources) {
    mResources = resources;
  }

  @Override
  public void onPictureTaken(final byte[] data, final Camera camera) {

    final int screenWidth = getScreenWidth();
    final int screenHeight = getScreenHeight();

    final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

    if (mResources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      // Notice that width and height are reversed
      final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, screenHeight, screenWidth, true);
      final int scaledWidth = scaledBitmap.getWidth();
      final int scaledHeight = scaledBitmap.getHeight();
      // Setting post rotate to 90
      final Matrix mtx = new Matrix();
      mtx.postRotate(90);
      // Rotating Bitmap
      final Bitmap rotatedScaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledWidth, scaledHeight, mtx, true);
    } else {// LANDSCAPE MODE
      //No need to reverse width and height
      final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, true);
    }
  }

  private int getScreenWidth() {
    return mResources.getDisplayMetrics().widthPixels;
  }

  private int getScreenHeight() {
    return mResources.getDisplayMetrics().heightPixels;
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
  }

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
}

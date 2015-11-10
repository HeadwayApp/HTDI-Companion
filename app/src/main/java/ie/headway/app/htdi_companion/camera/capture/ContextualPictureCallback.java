package ie.headway.app.htdi_companion.camera.capture;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.OutputStream;

public class ContextualPictureCallback extends JpegPictureCallback {

  private final Resources mResources;

  public ContextualPictureCallback(final OutputStream outputStream, final Resources resources) {
    super(outputStream);
    mResources = resources;
  }

  @Override
  protected void writeBitmapToFile(final Bitmap bitmap) {
    final Bitmap contextualizedBitmap = contextualizeBitmap(bitmap);
    super.writeBitmapToFile(contextualizedBitmap);
  }

  protected Bitmap contextualizeBitmap(final Bitmap bitmap) {
    final int screenWidth = getScreenWidth();
    final int screenHeight = getScreenHeight();

    final int orientation = mResources.getConfiguration().orientation;

    final Bitmap scaledBitmap = scaleBitmap(bitmap, screenWidth, screenHeight);
    final Bitmap rotatedBitmap = rotateBitmap(scaledBitmap, orientation);

    return rotatedBitmap;
  }

  private int getScreenWidth() {
    return mResources.getDisplayMetrics().widthPixels;
  }

  private int getScreenHeight() {
    return mResources.getDisplayMetrics().heightPixels;
  }

  private Bitmap scaleBitmap(final Bitmap bitmap, final int width, final int height) {
    final Bitmap scaledCapturedBitmap = Bitmap.createScaledBitmap(bitmap, height, width, true);
    return scaledCapturedBitmap;
  }

  private Bitmap rotateBitmap(final Bitmap bitmap, final int orientation) {

    final int bitmapWidth = bitmap.getWidth();
    final int bitmapHeight = bitmap.getHeight();

    final Matrix mtx = new Matrix();
    if(orientation == Configuration.ORIENTATION_PORTRAIT) {
      mtx.postRotate(90.0F);
    }

    final Bitmap rotatedBitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, mtx, true);

    return rotatedBitmap;
  }

}

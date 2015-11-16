package ie.headway.app.htdi_companion.gui;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class FixBitmapRotationAndScale implements FixBitmap {

  private final Resources mResources;

  public FixBitmapRotationAndScale(final Resources resources) {
    mResources = resources;
  }

  @Override
  public Bitmap fixBitmap(final Bitmap bitmap) {
    final int screenWidth = getScreenWidth(mResources);
    final int screenHeight = getScreenHeight(mResources);

    final int orientation = mResources.getConfiguration().orientation;

    final Bitmap scaledBitmap = scaleBitmap(bitmap, screenWidth, screenHeight);
    final Bitmap rotatedBitmap = rotateBitmap(scaledBitmap, orientation);

    return rotatedBitmap;
  }

  private int getScreenWidth(final Resources resources) {
    return resources.getDisplayMetrics().widthPixels;
  }

  private int getScreenHeight(final Resources resources) {
    return resources.getDisplayMetrics().heightPixels;
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

    final Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, mtx, true);

    return rotatedBitmap;
  }

}

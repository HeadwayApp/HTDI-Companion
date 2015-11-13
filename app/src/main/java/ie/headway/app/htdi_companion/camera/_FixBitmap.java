package ie.headway.app.htdi_companion.camera;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public final class _FixBitmap {

  public static Bitmap fixBitmap(final Resources resources, final Bitmap bitmap) {
    final int screenWidth = getScreenWidth(resources);
    final int screenHeight = getScreenHeight(resources);

    final int orientation = resources.getConfiguration().orientation;

    final Bitmap scaledBitmap = scaleBitmap(bitmap, screenWidth, screenHeight);
    final Bitmap rotatedBitmap = rotateBitmap(scaledBitmap, orientation);

    return rotatedBitmap;
  }

  private static int getScreenWidth(final Resources resources) {
    return resources.getDisplayMetrics().widthPixels;
  }

  private static int getScreenHeight(final Resources resources) {
    return resources.getDisplayMetrics().heightPixels;
  }

  private static Bitmap scaleBitmap(final Bitmap bitmap, final int width, final int height) {
    final Bitmap scaledCapturedBitmap = Bitmap.createScaledBitmap(bitmap, height, width, true);
    return scaledCapturedBitmap;
  }

  private static Bitmap rotateBitmap(final Bitmap bitmap, final int orientation) {

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

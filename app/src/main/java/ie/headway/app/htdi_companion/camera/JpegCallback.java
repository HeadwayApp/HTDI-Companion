package ie.headway.app.htdi_companion.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

public class JpegCallback implements Camera.PictureCallback {

  private Bitmap mCapturedBitmap;

  public Bitmap getCapturedBitmap() {
    return mCapturedBitmap;
  }

  protected void setCapturedBitmap(final Bitmap bitmap) {
    mCapturedBitmap = bitmap;
  }

  @Override
  public void onPictureTaken(final byte[] data, final Camera camera) {
    final int offset = 0;
    final int length = (data != null) ? data.length : 0;
    mCapturedBitmap = BitmapFactory.decodeByteArray(data, offset, length);
  }

}


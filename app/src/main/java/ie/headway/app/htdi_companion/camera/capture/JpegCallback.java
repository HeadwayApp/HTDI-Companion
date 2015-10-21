package ie.headway.app.htdi_companion.camera.capture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import static com.google.common.base.Preconditions.checkState;

class JpegCallback implements Camera.PictureCallback {

  private Bitmap mCapturedBitmap;

  protected Bitmap getCapturedBitmap() {
    checkState(mCapturedBitmap != null, "No bitmap has been captured.");
    return mCapturedBitmap;
  }

  protected void setCapturedBitmap(final Bitmap bitmap) {
    mCapturedBitmap = bitmap;
  }

  protected boolean hasCapturedBitmap() {
    return mCapturedBitmap != null;
  }

  @Override
  public void onPictureTaken(final byte[] data, final Camera camera) {
    final int offset = 0;
    final int length = (data != null) ? data.length : 0;
    mCapturedBitmap = BitmapFactory.decodeByteArray(data, offset, length);
  }

}


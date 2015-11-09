package ie.headway.app.htdi_companion.camera.capture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import java.io.OutputStream;

public class JpegPictureCallback implements Camera.PictureCallback {

  private final OutputStream mOutputStream;

  public JpegPictureCallback(final OutputStream outputStream) {
    mOutputStream = outputStream;
  }

  @Override
  public void onPictureTaken(final byte[] data, final Camera camera) {
    final int offset = 0;
    final int length = (data != null) ? data.length : 0;
    final Bitmap capturedBitmap = BitmapFactory.decodeByteArray(data, offset, length);
    writeBitmapToFile(capturedBitmap);
  }

  private void writeBitmapToFile(final Bitmap bitmap) {
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, mOutputStream);
  }

}

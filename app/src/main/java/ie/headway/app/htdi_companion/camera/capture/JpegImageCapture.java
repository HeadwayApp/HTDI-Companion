package ie.headway.app.htdi_companion.camera.capture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import java.io.OutputStream;

public class JpegImageCapture implements ImageCapture {

  private OutputStream mOutputStream;

  public JpegImageCapture(final OutputStream outputStream) {
    mOutputStream = outputStream;
  }

  public void setOutputStream(final OutputStream outputStream) {
    mOutputStream = outputStream;
  }

  @Override
  public void takePicture(final Camera camera) {
    camera.takePicture(null, null, this);
  }

  @Override
  public void onPictureTaken(final byte[] data, final Camera camera) {
    final int offset = 0;
    final int length = (data != null) ? data.length : 0;
    final Bitmap capturedBitmap = BitmapFactory.decodeByteArray(data, offset, length);
    writeBitmapToFile(capturedBitmap);
  }

  private OutputStream prevOs;

  private void writeBitmapToFile(final Bitmap bitmap) {
    if((prevOs != null) && (prevOs == mOutputStream)) {
      throw new IllegalStateException("OutputStream was already used");
    }else {
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, mOutputStream);
      prevOs = mOutputStream;
    }
  }

}

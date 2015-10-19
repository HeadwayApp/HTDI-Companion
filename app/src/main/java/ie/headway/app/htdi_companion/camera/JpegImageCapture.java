package ie.headway.app.htdi_companion.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;

public class JpegImageCapture implements ImageCapture {

  private final JpegCallback mJpegCallback;

  public JpegImageCapture(final JpegCallback jpegCallback) {
    mJpegCallback = jpegCallback;
  }

  @Override
  public Bitmap takePicture(final Camera camera) {
    camera.takePicture(null, null, mJpegCallback);
    return mJpegCallback.getCapturedBitmap();
  }

}

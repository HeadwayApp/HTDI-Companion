package ie.headway.app.htdi_companion.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;

import java.io.IOException;

public abstract class JpegImageCapture implements ImageCapture {

  private final JpegCallback mJpegCallback;

  public JpegImageCapture(final JpegCallback jpegCallback) {
    mJpegCallback = jpegCallback;
  }

  protected JpegCallback getJpegCallback() {
    return mJpegCallback;
  }

  @Override
  public Bitmap takePicture(final Camera camera) {
    camera.takePicture(null, null, mJpegCallback);
    return mJpegCallback.getCapturedBitmap();
  }

  @Override
  public abstract void savePicture(Bitmap bitmap) throws IOException;

}

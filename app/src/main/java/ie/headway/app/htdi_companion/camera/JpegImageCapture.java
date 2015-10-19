package ie.headway.app.htdi_companion.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;

public class JpegImageCapture implements ImageCapture {

  private final Camera.PictureCallback mJpegCallback;

  public JpegImageCapture(Camera.PictureCallback jpegCallback) {
    mJpegCallback = jpegCallback;
  }

  @Override
  public Bitmap takePicture(final Camera camera) {
    camera.takePicture(null, null, mJpegCallback);
  }

}

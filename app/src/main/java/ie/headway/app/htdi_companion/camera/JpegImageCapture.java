package ie.headway.app.htdi_companion.camera;

import android.hardware.Camera;

public class JpegImageCapture implements ImageCapture {

  private final Camera.PictureCallback mJpegCallback;

  public JpegImageCapture(Camera.PictureCallback jpegCallback) {
    mJpegCallback = jpegCallback;
  }

  @Override
  public void takePicture(final Camera camera) {
    camera.takePicture(null, null, mJpegCallback);
  }

}

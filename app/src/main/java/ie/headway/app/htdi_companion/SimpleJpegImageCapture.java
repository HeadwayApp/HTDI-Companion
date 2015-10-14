package ie.headway.app.htdi_companion;

import android.hardware.Camera;

public class SimpleJpegImageCapture implements ImageCapture {

  private final Camera.PictureCallback mJpegCallback;

  public SimpleJpegImageCapture(Camera.PictureCallback jpegCallback) {
    mJpegCallback = jpegCallback;
  }

  @Override
  public void takePicture(final Camera camera) {
    camera.takePicture(null, null, mJpegCallback);
  }

}

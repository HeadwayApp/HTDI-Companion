package ie.headway.app.htdi_companion.camera;

import android.hardware.Camera;

import ie.headway.app.htdi_companion.camera.ImageCapture;

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

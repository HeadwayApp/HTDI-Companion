package ie.headway.app.htdi_companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import java.io.IOException;

import ie.headway.app.htdi_companion.camera.capture.ImageCapture;

public final class CameraView extends AbstractCameraView {

  private Camera mCamera;
  private ImageCapture mImageCapture;

  public CameraView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public void setCamera(final Camera camera) {
    mCamera = camera;
  }

  public void setImageCapture(final ImageCapture imageCapture) {
    mImageCapture = imageCapture;
  }

  @Override
  public void captureImage() {
    mImageCapture.takePicture(mCamera);

    try {
      mImageCapture.savePicture();
    }catch(IOException ioe) {
      throw new RuntimeException("Couldn't save image.", ioe);
    }
  }

  @Override
  protected void refreshCameraView() throws IOException {
    stopCamera();
    startCamera();
  }

  @Override
  protected void startCamera() throws IOException {
    final SurfaceHolder surfaceHolder = getHolder();
    mCamera.setPreviewDisplay(surfaceHolder);
    mCamera.startPreview();
  }

  @Override
  protected void stopCamera() {
    mCamera.stopPreview();
  }

  @Override
  protected void releaseCamera() {
    mCamera.release();
  }

}

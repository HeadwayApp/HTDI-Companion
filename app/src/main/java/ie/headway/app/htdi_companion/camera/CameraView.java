package ie.headway.app.htdi_companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
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
  public void captureImage(final Runnable runnable) {
    mImageCapture.takePicture(mCamera, runnable);
  }

  public void refresh() {
    try {
      refreshCameraView(getHolder());
    } catch (IOException e) {
      throw new RuntimeException("couldn't refresh camera view", e);
    }
  }

  @Override
  protected void refreshCameraView(final SurfaceHolder holder) throws IOException {
    stopCamera();
    startCamera(holder);

    Log.e("CameraView", "refreshed camera view");
  }

  @Override
  protected void startCamera(final SurfaceHolder holder) throws IOException {
    mCamera.setPreviewDisplay(holder);
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

package ie.headway.app.htdi_companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * TODO: Should CameraView return an ImageCapture which is used to invoke captureImage() on?
 * */
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

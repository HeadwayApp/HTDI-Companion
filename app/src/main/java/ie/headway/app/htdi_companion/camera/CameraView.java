package ie.headway.app.htdi_companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkState;

public final class CameraView extends AbstractCameraView {

  private Camera mCamera;
  private Camera.PictureCallback mPictureCallback;

  public CameraView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public void setCamera(final Camera camera) {
    mCamera = camera;
  }

  public void setPictureCallback(final Camera.PictureCallback pictureCallback) {
    mPictureCallback = pictureCallback;
  }

  @Override
  public void captureImage() {
    checkState(mPictureCallback != null, "picture callback not set");
    mCamera.takePicture(null, null, mPictureCallback);
  }

  @Override
  public void refreshCameraView() throws IOException {
    stopCameraPreview();
    startCameraPreview();
  }

  @Override
  public void startCameraPreview() throws IOException {
    final SurfaceHolder holder = getHolder();
    mCamera.setPreviewDisplay(holder);
    mCamera.startPreview();
  }

  @Override
  public void stopCameraPreview() {
    mCamera.stopPreview();
  }

  @Override
  public void releaseCamera() {
    mCamera.release();
  }

}

package ie.headway.app.htdi_companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;

public final class CameraView extends AbstractCameraView {

  private static final String TAG = "CameraView";

  private final Camera mCamera;
  private final ImageCapture mImageCapture;

  public static CameraView newInstance(final Context context, final Camera camera,
                                       final ImageCapture imageCapture) {
    final CameraView instance = new CameraView(context, camera, imageCapture);
    return instance;
  }

  private CameraView(final Context context, final Camera camera,
                     final ImageCapture imageCapture) {
    super(context);
    mCamera = camera;
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

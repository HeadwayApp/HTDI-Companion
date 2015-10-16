package ie.headway.app.htdi_companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

  private static final String TAG = "CameraView";

  private final Camera mCamera;
  private final ImageCapture mImageCapture;

  public static CameraView newInstance(final Context context, final Camera camera,
                                       final ImageCapture imageCapture) {
    final CameraView instance = new CameraView(context, camera, imageCapture);
    instance.getHolder().addCallback(instance);
    return instance;
  }

  private CameraView(final Context context, final Camera camera,
                     final ImageCapture imageCapture) {
    super(context);
    mCamera = camera;
    mImageCapture = imageCapture;
  }

  public void captureImage() {
    mImageCapture.takePicture(mCamera);
  }

  public void refreshCameraView() throws IOException {
    stopCamera();
    startCamera();
  }

  @Override
  public void surfaceChanged(final SurfaceHolder holder, final int format, final int w, final int h) {
    try {
      refreshCameraView();
    }catch(IOException ioe) {
      throw new RuntimeException("Couldn't refresh camera view.", ioe);
    }
  }

  @Override
  public void surfaceCreated(final SurfaceHolder holder) {
    try {
      startCamera();
    }catch(IOException ioe) {
      throw new RuntimeException("Couldn't start camera.", ioe);
    }
  }

  @Override
  public void surfaceDestroyed(final SurfaceHolder holder) {
    releaseCamera();
  }

  protected void startCamera() throws IOException {
    mCamera.setPreviewDisplay(getHolder());
    mCamera.startPreview();
  }

  protected void stopCamera() {
    mCamera.stopPreview();
  }

  protected void releaseCamera() {
    mCamera.stopPreview();
    mCamera.release();
  }

}

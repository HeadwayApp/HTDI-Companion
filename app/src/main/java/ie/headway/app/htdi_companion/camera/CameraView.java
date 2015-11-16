package ie.headway.app.htdi_companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.lang.ref.WeakReference;

public final class CameraView extends AbstractCameraView {

  private final Camera mCamera;
  private WeakReference<OnImageCapturedListener> mImageCapturedListenerRef;

  public CameraView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    mCamera = AutoOrientatedCamera.getCamera(context);
  }

  public void setOnImageCapturedListener(final OnImageCapturedListener listener) {
    mImageCapturedListenerRef = new WeakReference<>(listener);
  }

  @Override
  public void captureImage() {
    mCamera.takePicture(null, null, this);
  }

  @Override
  public void refreshCameraView() throws IOException {
    stopPreview();
    startPreview();
  }

  @Override
  public void startPreview() throws IOException {
    final SurfaceHolder holder = getHolder();
    mCamera.setPreviewDisplay(holder);
    mCamera.startPreview();
  }

  @Override
  public void stopPreview() {
    mCamera.stopPreview();
  }

  @Override
  public void releaseCamera() {
    mCamera.release();
  }

  @Override
  public void onPictureTaken(final byte[] data, final Camera camera) {
    try {
      final OnImageCapturedListener listener = mImageCapturedListenerRef.get();
      listener.onImageCaptured(data);
      refreshCameraView();
    } catch (IOException e) {
      throw new RuntimeException("couldn't refresh camera view", e);
    }
  }

}

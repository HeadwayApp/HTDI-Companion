package ie.headway.app.htdi__companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import java.io.IOException;

public final class CameraView extends AbstractCameraView {

  private Context mContext;

  private Camera mCamera;
  private OnImageCapturedListener mImageCapturedListener;

  public CameraView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
  }

  public void setOnImageCapturedListener(final OnImageCapturedListener listener) {
    mImageCapturedListener = listener;
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
    mCamera = AutoOrientatedCamera.getCamera(mContext);
    final SurfaceHolder holder = getHolder();
    mCamera.setPreviewDisplay(holder);
    mCamera.startPreview();
  }

  @Override
  public void stopPreview() {
    mCamera.stopPreview();
    releaseCamera();
  }

  @Override
  public void releaseCamera() {
    mCamera.release();
  }

  @Override
  public void onPictureTaken(final byte[] data, final Camera camera) {
    try {
      final OnImageCapturedListener listener = mImageCapturedListener;
      listener.onImageCaptured(data);
      refreshCameraView();
    } catch (IOException e) {
      throw new RuntimeException("couldn't refresh camera view", e);
    }
  }

}

package ie.headway.app.htdi_companion;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

  private static final String TAG = "CameraView";

  private final Camera mCamera;
  private final SurfaceHolder mSurfaceHolder;
  private final Camera.PictureCallback mJpegCallback;

  public CameraView(final Context context, final Camera camera, final Camera.PictureCallback pictureCallback) {
    super(context);
    mCamera = camera;
    mSurfaceHolder = getHolder();
    mSurfaceHolder.addCallback(this);
    mJpegCallback = pictureCallback;
  }

  public void captureImage() {
    mCamera.takePicture(null, null, mJpegCallback);
  }

  public void refreshCameraView() {
    if (mSurfaceHolder.getSurface() == null) {
      // preview surface does not exist
      return;
    }

    // stop preview before making changes
    try {
      mCamera.stopPreview();
    } catch (Exception e) {
      // ignore: tried to stop a non-existent preview
    }

    // set preview size and make any resize, rotate or
    // reformatting changes here
    // start preview with new settings
    try {
      mCamera.setPreviewDisplay(mSurfaceHolder);
      mCamera.startPreview();
    } catch (Exception e) {

    }
  }

  @Override
  public void surfaceChanged(final SurfaceHolder holder, final int format, final int w, final int h) {
    refreshCameraView();
  }

  @Override
  public void surfaceCreated(final SurfaceHolder holder) {
    try {
      mCamera.setPreviewDisplay(mSurfaceHolder);
    } catch (IOException e) {
      Log.e(TAG, "exception on line: mCamera.setPreviewDisplay(mSurfaceHolder)", e);
    }
    mCamera.startPreview();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    mCamera.stopPreview();
    mCamera.release();
  }

}

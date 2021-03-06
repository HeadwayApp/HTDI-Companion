package ie.headway.app.htdi__companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * NOTE: You'll notice there is no reference to android.hardware.Camera anywhere in the class.
 * While this was note completely intentional, it is a good design choice as the AbstractCameraView
 * is now not tied to either android.hardware.Camera or android.hardware.camera2 (which are
 * completely separate entities not related by any kind of inheritance or interface).
 */
public abstract class AbstractCameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback, CameraViewControls {

  protected AbstractCameraView(final Context context) {
    this(context, null);
  }

  protected AbstractCameraView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  protected AbstractCameraView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final SurfaceHolder holder = getHolder();
    final SurfaceHolder.Callback callback = this;
    holder.addCallback(callback);
  }

  @Override
  public void surfaceChanged(final SurfaceHolder holder, final int format, final int w, final int h) {
    try {
      refreshCameraView();
    } catch (IOException ioe) {
      throw new RuntimeException("Couldn't refresh camera view.", ioe);
    }
  }

  @Override
  public void surfaceCreated(final SurfaceHolder holder) {
    try {
      startPreview();
    } catch (IOException ioe) {
      throw new RuntimeException("Couldn't start camera.", ioe);
    }
  }

  @Override
  public void surfaceDestroyed(final SurfaceHolder holder) {
    stopPreview();
    releaseCamera();
  }

}

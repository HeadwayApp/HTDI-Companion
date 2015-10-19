package ie.headway.app.htdi_companion.camera;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public abstract class AbstractCameraView extends SurfaceView implements SurfaceHolder.Callback {

  protected AbstractCameraView(final Context context) {
    super(context);
    final SurfaceHolder holder = getHolder();
    final SurfaceHolder.Callback callback = this;
    holder.addCallback(callback);
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
    stopCamera();
    releaseCamera();
  }

  public abstract void captureImage();

  protected abstract void refreshCameraView() throws IOException;

  protected abstract void startCamera() throws IOException;

  protected abstract void stopCamera();

  protected abstract void releaseCamera();

}

package ie.headway.app.htdi__companion.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.WindowManager;

public final class AutoOrientatedCamera {

  private final Context mContext;

  private AutoOrientatedCamera(final Context context) {
    mContext = context;
  }

  public static Camera getCamera(final Context context) {
    final AutoOrientatedCamera aOCam = new AutoOrientatedCamera(context);
    final Camera camera = Camera.open();
    final int cameraId = 0;
    return aOCam.setCameraDisplayOrientation(cameraId, camera);
  }

  private Camera setCameraDisplayOrientation(final int cameraId, final Camera camera) {
    final Camera.CameraInfo info = new Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, info);
    int rotation = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
      default:
        throw new RuntimeException("Invalid rotation.");
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    } else { // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);

    return camera;
  }

}

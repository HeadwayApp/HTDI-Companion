package ie.headway.app.htdi_companion.camera.capture;

import android.hardware.Camera;

public interface ImageCapture extends Camera.PictureCallback {

  void takePicture(Camera camera);

}

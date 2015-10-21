package ie.headway.app.htdi_companion.camera.capture;

import android.hardware.Camera;

import java.io.IOException;

public interface ImageCapture {

  void takePicture(Camera camera);

  void savePicture() throws IOException;

}

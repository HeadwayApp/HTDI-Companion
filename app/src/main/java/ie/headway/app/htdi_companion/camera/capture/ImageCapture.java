package ie.headway.app.htdi_companion.camera.capture;

import android.hardware.Camera;

import java.io.IOException;

public interface ImageCapture {

  void takePicture(Camera camera);

  /**
   * This method is invoked from #takePicture, do not invoke this method directly.
   * */
  void savePicture() throws IOException;

}

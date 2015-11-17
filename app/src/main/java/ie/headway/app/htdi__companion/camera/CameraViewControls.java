package ie.headway.app.htdi__companion.camera;

import java.io.IOException;

public interface CameraViewControls extends CameraControls {

  void refreshCameraView() throws IOException;

  void startPreview() throws IOException;

  void stopPreview();

}

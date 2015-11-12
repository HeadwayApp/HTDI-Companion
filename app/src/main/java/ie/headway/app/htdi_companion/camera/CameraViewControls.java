package ie.headway.app.htdi_companion.camera;

import java.io.IOException;

public interface CameraViewControls extends CameraControls {

  void refreshCameraView() throws IOException;

  void startPreview() throws IOException;

  void stopPreview();

}

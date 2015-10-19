package ie.headway.app.htdi_companion.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;

import java.io.IOException;

public interface ImageCapture {

  Bitmap takePicture(Camera camera);

  void savePicture(Bitmap bitmap) throws IOException;

}

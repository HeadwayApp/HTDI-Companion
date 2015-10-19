package ie.headway.app.htdi_companion.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;

public interface ImageCapture {

  Bitmap takePicture(Camera camera);

}

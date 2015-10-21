package ie.headway.app.htdi_companion.camera.capture;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Camera;

import java.io.IOException;
import java.io.OutputStream;

import ie.headway.app.htdi_companion.util.BitmapHolder;

public class JpegImageCapture implements ImageCapture {

  private final JpegCallback mJpegCallback;
  private final OutputStream mOutputStream;

  public JpegImageCapture(final Resources resources, final OutputStream outputStream) {
    mJpegCallback = new ScaledJpegCallback(resources);
    mOutputStream = outputStream;
  }
  @Override
  public void takePicture(final Camera camera) {
    camera.takePicture(null, null, mJpegCallback);
  }

  @Override
  public void savePicture() throws IOException {

    final BitmapHolder bitmapHolder = new BitmapHolder();

    new Thread() {
      @Override
      public void run() {
        while(!mJpegCallback.hasCapturedBitmap()) {
          try {
            Thread.sleep(1000);
          }catch(InterruptedException ie) {
            throw new RuntimeException("Couldn't sleep...", ie);
          }
        }

        bitmapHolder.bitmap = mJpegCallback.getCapturedBitmap();
        writeBitmapToFile(bitmapHolder.bitmap);

      }
    }.start();
  }

  private void writeBitmapToFile(final Bitmap bitmap) {
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, mOutputStream);
  }

}

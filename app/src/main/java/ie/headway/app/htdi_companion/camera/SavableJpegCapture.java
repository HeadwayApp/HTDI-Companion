package ie.headway.app.htdi_companion.camera;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.OutputStream;

public class SavableJpegCapture extends JpegImageCapture {

  private final OutputStream mOutputStream;

  public SavableJpegCapture(final JpegCallback jpegCallback, final OutputStream outputStream) {
    super(jpegCallback);
    mOutputStream = outputStream;
  }

  @Override
  public void savePicture(final Bitmap bitmap) throws IOException {
    writeBitmapToFile(bitmap);
  }

  private void writeBitmapToFile(final Bitmap bitmap) throws IOException {
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, mOutputStream);
    mOutputStream.flush();
    mOutputStream.close();
  }

}

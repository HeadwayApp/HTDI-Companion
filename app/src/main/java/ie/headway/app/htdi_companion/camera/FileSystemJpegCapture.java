package ie.headway.app.htdi_companion.camera;

import android.graphics.Bitmap;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileSystemJpegCapture extends JpegImageCapture {

  private final FileOutputStream mOutputStream;

  public FileSystemJpegCapture(final JpegCallback jpegCallback, final FileOutputStream outputStream) {
    super(jpegCallback);
    mOutputStream = outputStream;
  }

  public void closeOutputStream() throws IOException {
    mOutputStream.close();
  }

  @Override
  public void savePicture(final Bitmap bitmap) throws IOException {
    writeBitmapToFile(bitmap);
  }

  private void writeBitmapToFile(final Bitmap bitmap) throws IOException {
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, mOutputStream);
    mOutputStream.flush();
  }

}

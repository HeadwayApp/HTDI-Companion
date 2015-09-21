package ie.headway.app.htdi_companion;

import android.hardware.Camera;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JpegCallback implements Camera.PictureCallback {

    private static final String TAG = "JpegCallback";

    private final File mFile;

    private JpegCallback(final File file) {
        mFile = file;
    }

    public static JpegCallback newInstance(final File file) {
        final JpegCallback jpegCallback = new JpegCallback(file);
        return jpegCallback;
    }

    @Override
    public void onPictureTaken(final byte[] data, final Camera camera) {
        try {
            writeRawDataToFile(data, mFile);
        } catch (IOException e) {
            Log.e(TAG, "exception on line: writeRawDataToFile(data, file)", e);
        }
    }

    private void writeRawDataToFile(final byte[] data, final File file) throws IOException {
        final FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    }

}

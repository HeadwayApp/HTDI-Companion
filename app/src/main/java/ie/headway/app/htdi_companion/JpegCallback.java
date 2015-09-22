package ie.headway.app.htdi_companion;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JpegCallback implements Camera.PictureCallback {

    private static final String TAG = "JpegCallback";

    private File mFile;
    private CameraView mCamView;

    private Context mContext;

    private JpegCallback(final File file, final CameraView cameraView, final Context context) {
        mFile = file;
        mCamView = cameraView;
        mContext = context;
    }

    public static JpegCallback newInstance(final File file, final CameraView cameraView, final Context context) {
        final JpegCallback jpegCallback = new JpegCallback(file, cameraView, context);
        return jpegCallback;
    }

    @Override
    public void onPictureTaken(final byte[] data, final Camera camera) {

        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Notice that width and height are reversed
            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
            int w = scaled.getWidth();
            int h = scaled.getHeight();
            // Setting post rotate to 90
            Matrix mtx = new Matrix();
            mtx.postRotate(90);
            // Rotating Bitmap
            bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
        }else{// LANDSCAPE MODE
            //No need to reverse width and height
            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth,screenHeight , true);
            bm=scaled;
        }

        try {
//            writeRawDataToFile(data, mFile);
            writeBitmapToFile(bm, mFile);
        } catch (IOException e) {
            Log.e(TAG, "exception on line: writeBitmapToFile(bitmap, file)", e);
        }

        try {
            mCamView.refreshCameraView();
        }catch(Exception e) {
            Log.e("mo", "Error occurred", e);
        }
    }

    private void writeRawDataToFile(final byte[] data, final File file) throws IOException {
        final FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    }

    private void writeBitmapToFile(final Bitmap bitmap, final File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        // PNG is a lossless format, the compression factor (100) is ignored
        if (out != null) {
            out.close();
        }
    }

    public void setCamView(final CameraView camView) {
        mCamView = camView;
    }

    public void setFile(File file) {
        mFile = file;
    }
}

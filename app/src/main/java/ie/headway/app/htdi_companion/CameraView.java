package ie.headway.app.htdi_companion;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraView";

    private final Camera mCamera; //TODO Should use dependancy injection.
    private SurfaceHolder mSurfaceHolder;
    private Camera.PictureCallback mJpegCallback;

    public CameraView(final Context context, final Camera camera) {
        super(context);
        mCamera = camera;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mJpegCallback = getJpegCallback();
    }

    private Camera.PictureCallback getJpegCallback() {
        return new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    writeRawDataToFile(data, new File("/sdcard/img"));
                } catch (IOException e) {
                    Log.e(TAG, "exception on line: writeRawDataToFile(data, file)", e);
                }
                refreshCamera();
            }
        };
    }

    private void writeRawDataToFile(final byte[] data, final File file) throws IOException {
        final FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    }

    public void captureImage() throws IOException {
        //take the picture
        mCamera.takePicture(null, null, mJpegCallback);
    }

    private void refreshCamera() {
        if (mSurfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, final int format, final int w, final int h) {
        refreshCamera();
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {

        mCamera.setDisplayOrientation(90);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            Log.e(TAG, "exception on line: mCamera.setPreviewDisplay(mSurfaceHolder)", e);
        }
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }

}

package ie.headway.app.htdi_companion;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera; //TODO Should use dependancy injection.
    private SurfaceHolder mSurfaceHolder;
    private Camera.PictureCallback mJpegCallback;

    public CameraView(final Context context) {
        super(context);
        mSurfaceHolder = getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mSurfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mJpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Toast.makeText(getContext().getApplicationContext(),
                        "Picture Saved", Toast.LENGTH_SHORT).show();
                refreshCamera();
            }
        };
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
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        // open the camera
        mCamera = Camera.open();

        mCamera.setDisplayOrientation(90);

        Camera.Parameters param;
        param = mCamera.getParameters();

        // modify parameter
        param.setPreviewSize(352, 288);
        mCamera.setParameters(param);
        // The Surface has been created, now tell the camera where to draw
        // the preview.
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

}

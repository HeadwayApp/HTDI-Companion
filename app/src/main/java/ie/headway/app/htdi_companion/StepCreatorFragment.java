package ie.headway.app.htdi_companion;

import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import ie.headway.app.xml.Step;

public class StepCreatorFragment extends Fragment implements SurfaceHolder.Callback {

	private static final String STEP_KEY = "STEP";

    private StepBuilder mStepBuilder = new StepBuilder();

	private LinearLayout mView;
	private TextView mText;
	private SurfaceView mImage;

	private Camera mCamera;
	private SurfaceHolder mSurfaceHolder;
	private PictureCallback mJpegCallback;

	public static final StepCreatorFragment newInstance() {
		final StepCreatorFragment stepCreatorFragment = new StepCreatorFragment();
		return stepCreatorFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mView = new LinearLayout(getActivity().getBaseContext());
		mView.setOrientation(VERTICAL);

		mText = new TextView(getActivity().getBaseContext());
		mText.setText(" ");
		mText.setTextSize(COMPLEX_UNIT_SP, 25);
		mText.setGravity(CENTER);
		mText.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		mView.addView(mText);

		mImage = new SurfaceView(getActivity().getBaseContext());
        mImage.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        mImage.setPadding(0, 0, 0, px2Dp(getActivity(), 15));

		mSurfaceHolder = mImage.getHolder();

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mSurfaceHolder.addCallback(this);

		// deprecated setting, but required on Android versions prior to 3.0
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mJpegCallback = new PictureCallback() {
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
				Toast.makeText(getActivity().getApplicationContext(), "Picture Saved", 2000).show();
				refreshCamera();
			}
		};

		mView.addView(mImage);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mView;
	}

	private int px2Dp(final Activity activity, final int dp) {
		final Resources resources = activity.getResources();
		final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		final float scale = displayMetrics.density;
		int px = (int) (dp * scale + 0.5f);
		return px;
	}

	private void captureImage(View v) throws IOException {
		//take the picture
		mCamera.takePicture(null, null, mJpegCallback);
	}

	public void refreshCamera() {
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

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		refreshCamera();
	}

	public void surfaceCreated(SurfaceHolder holder) {
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

	public void surfaceDestroyed(SurfaceHolder holder) {
		// stop preview and release camera
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

}

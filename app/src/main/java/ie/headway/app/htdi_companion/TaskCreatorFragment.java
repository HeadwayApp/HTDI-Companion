package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import ie.headway.app.disk.AppDir;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Task;

import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class TaskCreatorFragment extends Fragment {

  private static final String TAG = "TaskCreatorFragment";

  private int stepCnt = 0;

  private JpegCallback jCall;
  private ImageCapture mImageCapture;

  private Task mTask;

  private LinearLayout mView;
  private TextView mText;
  private CameraView mImage;

  private Button mCreateStepButton;

  private Button mFinishedButton;

  private File currentJpegFile;

  public static final TaskCreatorFragment newInstance(final Task task) {
    final TaskCreatorFragment taskCreatorFragment = new TaskCreatorFragment();
    final Bundle args = new Bundle();
    args.putParcelable("task", task);
    taskCreatorFragment.setArguments(args);
    return taskCreatorFragment;
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                           final Bundle savedInstanceState) {

    final String taskName = ((Task) getArguments().getParcelable("task")).getName();

    currentJpegFile = AppDir.ROOT.getFile(File.separator + taskName +
        File.separator + "imgs" +
        File.separator + String.valueOf(stepCnt) + ".jpg");

    mView = new LinearLayout(getActivity().getBaseContext());
    mView.setOrientation(VERTICAL);

    mText = new EditText(getActivity().getBaseContext());
    mText.setText(" ");
    mText.setTextSize(COMPLEX_UNIT_SP, 25);
    mText.setGravity(CENTER);
    mText.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    mView.addView(mText);

    mCreateStepButton = new Button(getActivity().getBaseContext());
    mCreateStepButton.setText("Create Step");
    mCreateStepButton.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    mCreateStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View view) {

        captureImage();

        loadTaskFromParcelable();

        Log.i("mo", "Received Task as arg: " + mTask.toString());

        currentJpegFile = getNextJpegFile();

        String filePath = currentJpegFile.getAbsolutePath();
        filePath = filePath.replace(Environment.getExternalStorageDirectory().getAbsolutePath(), PortableStep.PATH_ARTIFACT);

        final PortableStep step = new PortableStep(mText.getText().toString(), filePath, "");
        Log.w("mo", "created step " + step);

        jCall.setFile(currentJpegFile);

        mTask.addStep(step);

        final Serializer serializer = new Persister();

        try {
          serializer.write(mTask, new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "task.xml")));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }

        mText.setText("");

      }
    });

    mView.addView(mCreateStepButton);

    jCall = JpegCallback.newInstance(currentJpegFile, null, getActivity());
    mImageCapture = new SimpleJpegImageCapture(jCall);

    final Camera camera = Camera.open();
    setCameraDisplayOrientation(getActivity(), 0, camera);

    mImage = CameraView.newInstance(getActivity().getApplicationContext(), camera, jCall);
    jCall.setCamView(mImage);
    mImage.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
    mImage.setPadding(0, 0, 0, px2Dp(getActivity(), 15));
    mImage.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        final String taskName = (mTask != null) ? mTask.getName() : "";
        final Toast toast = Toast.makeText(getActivity(), "Created Task " + taskName, Toast.LENGTH_LONG);
        toast.show();
        startActivity(new Intent(getActivity(), TaskInitialiserActivity.class));
        return true;
      }
    });
    mView.addView(mImage);

    return mView;
  }

  private File getNextJpegFile() {
    final String taskName = mTask.getName();
    final File jpegFile = AppDir.ROOT.getFile(File.separator + taskName +
        File.separator + "imgs" +
        File.separator + String.valueOf(++stepCnt) + ".jpg");

    return jpegFile;
  }

  private void captureImage() {
    mImage.captureImage(mImageCapture);
  }

  private void loadTaskFromParcelable() {
    mTask = getArguments().getParcelable("task");
  }

  /**
   * TODO Possible duplicate definition.
   */
  private static int px2Dp(final Activity activity, final int dp) {
    final Resources resources = activity.getResources();
    final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    final float scale = displayMetrics.density;
    final int px = (int) (dp * scale + 0.5f);
    return px;
  }

  public static void setCameraDisplayOrientation(final Activity activity,
                                                 final int cameraId, final Camera camera) {
    final Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, info);
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    } else { // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);
  }

}

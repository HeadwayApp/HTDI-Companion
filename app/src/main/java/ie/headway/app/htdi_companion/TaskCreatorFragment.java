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

  private Task mTask;
  private int stepCnt;

  public static final TaskCreatorFragment newInstance(final Task task) {
    final TaskCreatorFragment taskCreatorFragment = new TaskCreatorFragment();
    final Bundle args = new Bundle();
    args.putParcelable("task", task);
    taskCreatorFragment.setArguments(args);
    return taskCreatorFragment;
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadTaskFromArguments();
    Log.v(TAG, "Loaded task " + mTask.getName() + " from parcelable.");
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                           final Bundle savedInstanceState) {

    final LinearLayout parentLayout = new LinearLayout(getActivity());
    parentLayout.setOrientation(VERTICAL);

    final EditText stepDescriptionField = new EditText(getActivity());
    stepDescriptionField.setText(" ");
    stepDescriptionField.setTextSize(COMPLEX_UNIT_SP, 25);
    stepDescriptionField.setGravity(CENTER);
    stepDescriptionField.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));

    final JpegCallback jpegCallback = JpegCallback.newInstance(getNextJpegFile(), null, getActivity());
    final ImageCapture imageCapture = new SimpleJpegImageCapture(jpegCallback);

    final Camera camera = Camera.open();
    setCameraDisplayOrientation(getActivity(), 0, camera);

    final CameraView cameraView = CameraView.newInstance(getActivity().getApplicationContext(), camera, imageCapture);
    jpegCallback.setCamView(cameraView);
    cameraView.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
    cameraView.setPadding(0, 0, 0, px2Dp(getActivity(), 15));
    cameraView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        final String taskName = (mTask != null) ? mTask.getName() : "";
        final Toast toast = Toast.makeText(getActivity(), "Created Task " + taskName, Toast.LENGTH_LONG);
        toast.show();
        startActivity(new Intent(getActivity(), TaskInitialiserActivity.class));
        return true;
      }
    });

    final Button createStepButton = new Button(getActivity());
    createStepButton.setText("Create Step");
    createStepButton.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    createStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View view) {

        cameraView.captureImage();

        loadTaskFromArguments();

        final File nextJpegFile = getNextJpegFile();
        jpegCallback.setFile(nextJpegFile);

        final String filePath = nextJpegFile.getAbsolutePath().replace(
            Environment.getExternalStorageDirectory().getAbsolutePath(), PortableStep.PATH_ARTIFACT);

        final PortableStep step = new PortableStep(stepDescriptionField.getText().toString(), filePath, "");

        mTask.addStep(step);

        final Serializer serializer = new Persister();

        try {
          serializer.write(mTask, new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "task.xml")));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }

        stepDescriptionField.setText("");

      }
    });

    parentLayout.addView(stepDescriptionField);
    parentLayout.addView(createStepButton);
    parentLayout.addView(cameraView);

    return parentLayout;
  }

  private File getNextJpegFile() {
    final String taskName = mTask.getName();
    final File jpegFile = AppDir.ROOT.getFile(File.separator + taskName +
        File.separator + "imgs" +
        File.separator + String.valueOf(stepCnt++) + ".jpg");

    return jpegFile;
  }

  private void loadTaskFromArguments() {
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
      default: throw new RuntimeException("Invalid rotation.");
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

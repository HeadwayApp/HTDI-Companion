package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import ie.headway.app.disk.AppDir;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TaskCreatorFragment extends Fragment {

  private static final String TAG = "TaskCreatorFragment";

  private Task mTask;
  private int stepCnt;

  private CameraView mCameraView;

  private JpegCallback mJpegCallback;

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
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                           final Bundle savedInstanceState) {
    final LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.task_creator_fragment, container);

    mJpegCallback =
        JpegCallback.newInstance(getNextJpegFile(), null, getActivity());

    final ImageCapture imageCapture = new SimpleJpegImageCapture(mJpegCallback);
    final Camera camera = openCamera();

    mCameraView = makeCameraView(camera, mJpegCallback, imageCapture);
//    layout.addView(mCameraView);

    final FrameLayout cameraViewPlaceHolder = (FrameLayout)getActivity().findViewById(R.id.cameraViewPlaceHolder);
    cameraViewPlaceHolder.addView(mCameraView);

    return null;
  }

  public void onClickCreateStepButton(final View view) {

    mCameraView.captureImage();

    final File nextJpegFile = getNextJpegFile();
    mJpegCallback.setFile(nextJpegFile);

    final String filePath = nextJpegFile.getAbsolutePath().replace(
        Environment.getExternalStorageDirectory().getAbsolutePath(), PortableStep.PATH_ARTIFACT);

    final String stepDescription = getStepDescription().toString();

    final PortableStep step = new PortableStep(stepDescription, filePath, "");

    saveStep(step);

    clearStepDescriptionField();

  }

  private CharSequence getStepDescription() {
    final EditText editText = (EditText)getView().findViewById(R.id.inputStepDescriptionView);
    return editText.getText();
  }

  private void clearStepDescriptionField() {
    final EditText editText = (EditText)getView().findViewById(R.id.inputStepDescriptionView);
    editText.getText().clear();
  }

  private void saveStep(final Step step) {
    mTask.addStep(step);

    final Serializer serializer = new Persister();

    try {
      serializer.write(mTask, new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "task.xml")));
    }catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

  private CameraView makeCameraView(final Camera camera,
                                    final JpegCallback jpegCallback, ImageCapture imageCapture) {
    final CameraView cameraView =
        CameraView.newInstance(getActivity().getApplicationContext(), camera, imageCapture);
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
    return cameraView;
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

  private Camera openCamera() {
    final Camera camera = Camera.open();
    setCameraDisplayOrientation(0, camera);
    return camera;
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

  private void setCameraDisplayOrientation(final int cameraId, final Camera camera) {
    final Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, info);
    int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
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

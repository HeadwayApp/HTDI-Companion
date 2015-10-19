package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ie.headway.app.disk.AppDir;
import ie.headway.app.htdi_companion.camera.AutoOrientatedCamera;
import ie.headway.app.htdi_companion.camera.CameraView;
import ie.headway.app.htdi_companion.camera.ImageCapture;
import ie.headway.app.htdi_companion.camera.JpegCallback;
import ie.headway.app.htdi_companion.camera.SavableJpegCapture;
import ie.headway.app.htdi_companion.camera.ScaledJpegCallback;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

/**
 * TODO: Should getActivity().findViewById() be replaced with getView().findViewById()?
 */
public class TaskCreatorFragment extends Fragment {

  private static final String TAG = "TaskCreatorFragment";
  private static final String EMPTY_STRING = "";
  private static final View NO_VIEW_TO_RETURN = null;

  private Task mTask;
  private int stepCnt;

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
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

    inflater.inflate(R.layout.task_creator_fragment, container);

    return NO_VIEW_TO_RETURN;
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final Resources resources = getResources();
    mJpegCallback = new ScaledJpegCallback(resources);

    FileOutputStream jpegOutputStream;

    try{
      jpegOutputStream = new FileOutputStream(getNextJpegFile());
    }catch(FileNotFoundException fnfe){
      throw new RuntimeException("File not found", fnfe);
    }

    final ImageCapture imageCapture = new SavableJpegCapture(mJpegCallback, jpegOutputStream);
    final Camera camera = openCamera();

    setUpCameraView(camera, mJpegCallback, imageCapture);
  }

  protected CharSequence getStepDescription() {
    final EditText editText = (EditText) getActivity().findViewById(R.id.inputStepDescriptionView);
    return editText.getText();
  }

  protected void clearStepDescriptionField() {
    final EditText editText = (EditText) getActivity().findViewById(R.id.inputStepDescriptionView);
    editText.getText().clear();
  }

  protected void saveStep(final Step step) {
    mTask.addStep(step);

    final Serializer serializer = new Persister();

    try {
      serializer.write(mTask, new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "task.xml")));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  void onClickCreateStepButton(final View view) {

    final CameraView cameraView = (CameraView) getActivity().findViewById(R.id.cameraView);

    cameraView.captureImage();

    final File nextJpegFile = getNextJpegFile();
//    mJpegCallback.setFile(nextJpegFile);

    final String filePath = nextJpegFile.getAbsolutePath().replace(
        Environment.getExternalStorageDirectory().getAbsolutePath(), PortableStep.PATH_ARTIFACT);

    final String stepDescription = getStepDescription().toString();

    final PortableStep step = new PortableStep(stepDescription, filePath, EMPTY_STRING);

    saveStep(step);

    clearStepDescriptionField();

  }

  private void setUpCameraView(final Camera camera, final JpegCallback jpegCallback, ImageCapture imageCapture) {

    final CameraView cameraView = (CameraView) getActivity().findViewById(R.id.cameraView);
    cameraView.setCamera(camera);
    cameraView.setImageCapture(imageCapture);

    cameraView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        final String taskName = (mTask != null) ? mTask.getName() : EMPTY_STRING;
        final Toast toast = Toast.makeText(getActivity(), "Created Task " + taskName, Toast.LENGTH_LONG);
        toast.show();
        startActivity(new Intent(getActivity(), TaskInitialiserActivity.class));
        return true;
      }
    });
  }

  private File getNextJpegFile() {
    final String taskName = mTask.getName();
    final File jpegFile = AppDir.ROOT.getFile(File.separator + taskName +
        File.separator + "imgs" +
        File.separator + stepCnt++ + ".jpg");

    return jpegFile;
  }

  private void loadTaskFromArguments() {
    final Bundle arguments = getArguments();
    mTask = arguments.getParcelable("task");
  }

  private Camera openCamera() {
    final Activity activity = getActivity();
    final Camera camera = AutoOrientatedCamera.getCamera(activity);
    return camera;
  }

}

package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import java.io.IOException;
import java.io.OutputStream;

import ie.headway.app.disk.AppDir;
import ie.headway.app.htdi_companion.camera.AutoOrientatedCamera;
import ie.headway.app.htdi_companion.camera.CameraView;
import ie.headway.app.htdi_companion.camera.capture.JpegPictureCallback;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

public class StepCreatorFragment extends Fragment {

  private static final String EMPTY_STRING = "";
  private static final View VIEW_ALREADY_ATTACHED = null;

  private Task mTask;

  public static final StepCreatorFragment newInstance(final Task task) {
    final StepCreatorFragment stepCreatorFragment = new StepCreatorFragment();
    final Bundle args = new Bundle();
    args.putParcelable("task", task);
    stepCreatorFragment.setArguments(args);
    return stepCreatorFragment;
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadTaskFromArguments();
    setJpegFile();
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    inflater.inflate(R.layout.task_creator_fragment, container);
    return VIEW_ALREADY_ATTACHED;
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    OutputStream jpegOutputStream;

    try{
      jpegOutputStream = new FileOutputStream(nextJpeg);
    }catch(FileNotFoundException fnfe){
      throw new RuntimeException("File not found", fnfe);
    }

    final Camera.PictureCallback pictureCallback = new JpegPictureCallback(jpegOutputStream);
    final Camera camera = openCamera();

    initialiseCameraView(camera, pictureCallback);
  }

  protected CharSequence getStepDescription() {
    final EditText editText = (EditText) getActivity().findViewById(R.id.inputStepDescriptionView);
    return editText.getText();
  }

  protected void serialiseStep(final Step step) {
    mTask.addStep(step);

    final Serializer serializer = new Persister();

    try {
      serializer.write(mTask, new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "task.xml")));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * TODO: Throw exception if an action is attempted after captureImage on the UI thread.
   * */
  void onClickCreateStepButton(final View view) {

    final CameraView cameraView = (CameraView) getActivity().findViewById(R.id.cameraView);

    cameraView.captureImage();

    final String filePath = nextJpeg.getAbsolutePath().replace(
        Environment.getExternalStorageDirectory().getAbsolutePath(), PortableStep.PATH_ARTIFACT);

    final String stepDescription = getStepDescription().toString();

    final PortableStep step = new PortableStep(stepDescription, filePath, EMPTY_STRING);

    serialiseStep(step);

    try {
      cameraView.refreshCameraView();
    } catch (IOException e) {
      throw new RuntimeException("couldn't refresh camera view");
    }

    try{
      jpegOutputStream = new FileOutputStream(nextJpeg);
    }catch(FileNotFoundException fnfe){
      throw new RuntimeException("File not found", fnfe);
    }

    ((JpegPictureCallback) mPictureCallback).setOutputStream(jpegOutputStream);
    final Camera camera = openCamera();

  }

  private void initialiseCameraView(final Camera camera, final Camera.PictureCallback pictureCallback) {

    final CameraView cameraView = (CameraView) getActivity().findViewById(R.id.cameraView);
    cameraView.setCamera(camera);
    cameraView.setPictureCallback(pictureCallback);

    cameraView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(final View view) {
        return onLongClickCameraView(view);
      }
    });
  }

  private boolean onLongClickCameraView(final View View) {
    final String taskName = (mTask != null) ? mTask.getName() : EMPTY_STRING;
    final Toast toast = Toast.makeText(getActivity(), "Created Task " + taskName, Toast.LENGTH_LONG);
    toast.show();
    startActivity(new Intent(getActivity(), TaskInitialiserActivity.class));
    return true;
  }

  private void setJpegFile() {
    final String taskName = mTask.getName();

    nextJpeg = AppDir.ROOT.getFile(File.separator + taskName +
        File.separator + "imgs" +
        File.separator + stepCnt++ + ".jpg");
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

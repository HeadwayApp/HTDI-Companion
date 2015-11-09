package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ie.headway.app.htdi_companion.camera.AutoOrientatedCamera;
import ie.headway.app.htdi_companion.camera.CameraView;
import ie.headway.app.htdi_companion.camera.capture.ScaledJpegPictureCallback;
import ie.headway.app.xml.Step;

public class StepCreatorFragment extends Fragment {

  private static final View VIEW_ALREADY_ATTACHED = null;

  private File mTmpImg;
  private OutputStream mOutputStream;

  public static final StepCreatorFragment newInstance() {
    final StepCreatorFragment stepCreatorFragment = new StepCreatorFragment();
    return stepCreatorFragment;
  }

  public StepCreatorFragment() {
   setTmpImg();
    setOutputStream();
  }

  public void setTmpImg() {
    mTmpImg = new File("/sdcard/Headway/ " + System.currentTimeMillis() + ".jpg");
  }

  public void setOutputStream() {
    try {
      mOutputStream = new FileOutputStream(mTmpImg);
      getCameraView().setPictureCallback(new ScaledJpegPictureCallback(mOutputStream, getResources(), getCameraView()));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }catch(NullPointerException npe) {}
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    inflater.inflate(R.layout.task_creator_fragment, container);
    return VIEW_ALREADY_ATTACHED;
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initialiseCameraView();
  }

  public Step onClickCreateStepButton(final View view) {
    final CameraView cameraView = getCameraView();
    cameraView.captureImage();

    return new Step(new String(getStepDescription().toString()), new String(mTmpImg.getAbsolutePath()), "");
  }

  private CharSequence getStepDescription() {
    final EditText editText = (EditText) getActivity().findViewById(R.id.inputStepDescriptionView);
    return editText.getText();
  }

  private CameraView getCameraView() {
    final CameraView cameraView = (CameraView) getActivity().findViewById(R.id.cameraView);
    return cameraView;
  }

  private void initialiseCameraView() {
    final Camera camera = openCamera();
    final CameraView cameraView = (CameraView) getActivity().findViewById(R.id.cameraView);
    cameraView.setCamera(camera);
    cameraView.setPictureCallback(new ScaledJpegPictureCallback(mOutputStream, getResources(), getCameraView()));
  }

  private Camera openCamera() {
    final Activity activity = getActivity();
    final Camera camera = AutoOrientatedCamera.getCamera(activity);
    return camera;
  }

}

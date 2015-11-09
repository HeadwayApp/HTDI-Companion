package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ie.headway.app.disk.AppDir;
import ie.headway.app.htdi_companion.camera.AutoOrientatedCamera;
import ie.headway.app.htdi_companion.camera.CameraView;
import ie.headway.app.htdi_companion.camera.capture.ContextualJpegPictureCallback;
import ie.headway.app.xml.Step;

public class StepCreatorFragment extends Fragment {

  private static final View NO_VIEW_TO_RETURN = null;

  private File mTmpImg;
  private OutputStream mOutputStream;

  public static final StepCreatorFragment newInstance() {
    final StepCreatorFragment stepCreatorFragment = new StepCreatorFragment();
    return stepCreatorFragment;
  }

  public StepCreatorFragment() {
    mTmpImg = new File(AppDir.TMP.getPath(System.currentTimeMillis() + ".jpg"));
    init();
  }

  private void init() {
    try {
      mTmpImg = new File(AppDir.TMP.getPath(System.currentTimeMillis() + ".jpg"));
      mOutputStream = new FileOutputStream(mTmpImg);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  void refresh() {
    init();
    getCameraView().setPictureCallback(new PictureCallback(mOutputStream, getResources()));
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    inflater.inflate(R.layout.task_creator_fragment, null);
    return NO_VIEW_TO_RETURN;
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initialiseCameraView();
  }

  public Step onClickCreateStepButton(final View view) {
    final CameraView cameraView = getCameraView();
    cameraView.captureImage();

    return new Step(getStepDescription().toString(), mTmpImg.getAbsolutePath(), "");
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
    cameraView.setPictureCallback(new PictureCallback(mOutputStream, getResources()));
  }

  private Camera openCamera() {
    final Activity activity = getActivity();
    final Camera camera = AutoOrientatedCamera.getCamera(activity);
    return camera;
  }

  class PictureCallback extends ContextualJpegPictureCallback {

    public PictureCallback(OutputStream outputStream, Resources resources) {
      super(outputStream, resources);
    }

    @Override
    protected void writeBitmapToFile(final Bitmap bitmap) {
      super.writeBitmapToFile(bitmap);
      try {
        getCameraView().refreshCameraView();
      } catch (IOException e) {
        throw new RuntimeException("couldn't refresh camera view", e);
      }
    }

  }

}

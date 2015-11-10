package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import ie.headway.app.htdi_companion.camera.capture.ContextualPictureCallback;
import ie.headway.app.htdi_companion.step.LatentStep;
import ie.headway.app.xml.Step;

public class StepCreatorFragment extends Fragment {

  private File mTmpImg;
  private OutputStream mOutputStream;

  public static final StepCreatorFragment newInstance() {
    final StepCreatorFragment stepCreatorFragment = new StepCreatorFragment();
    return stepCreatorFragment;
  }

  public StepCreatorFragment() {
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
    clearStepDescription();
    getCameraView().setPictureCallback(new _PictureCallback(mOutputStream, getResources()));
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.task_creator_fragment, null);
  }

  @Override
  public void onResume() {
    super.onResume();
    initialiseCameraView();
  }

  public Step onClickCreateStepButton(final View view) {
    final CameraView cameraView = getCameraView();
    cameraView.captureImage();

    return new LatentStep(getStepDescription().toString(), mTmpImg.getAbsolutePath(), "");
  }

  private CharSequence getStepDescription() {
    final EditText editText = (EditText) getActivity().findViewById(R.id.input_step_description_view);
    return editText.getText();
  }

  private void clearStepDescription() {
    final EditText editText = (EditText) getActivity().findViewById(R.id.input_step_description_view);
    editText.setText("");
  }

  private Camera openCamera() {
    final Activity activity = getActivity();
    final Camera camera = AutoOrientatedCamera.getCamera(activity);
    return camera;
  }

  private CameraView getCameraView() {
    final CameraView cameraView = (CameraView) getActivity().findViewById(R.id.camera_view);
    return cameraView;
  }

  private void initialiseCameraView() {
    final Camera camera = openCamera();
    final CameraView cameraView = (CameraView) getActivity().findViewById(R.id.camera_view);
    cameraView.setCamera(camera);
    cameraView.setPictureCallback(new _PictureCallback(mOutputStream, getResources()));

    cameraView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        startActivity(new Intent(getActivity(), TaskInitialiserActivity.class));
        return true;
      }
    });
  }

  private class _PictureCallback extends ContextualPictureCallback {

    _PictureCallback(final OutputStream outputStream, final Resources resources) {
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

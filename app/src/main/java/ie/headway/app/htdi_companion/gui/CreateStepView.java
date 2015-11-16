package ie.headway.app.htdi_companion.gui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import ie.headway.app.htdi_companion.R;
import ie.headway.app.htdi_companion.camera.CameraView;
import ie.headway.app.htdi_companion.camera.OnImageCapturedListener;
import ie.headway.app.util.AppDir;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Step;

public class CreateStepView extends LinearLayout {

  @Bind(R.id.input_step_description_view) EditText mStepDescriptionField;
  @Bind(R.id.create_step_button) Button mCreateStepButton;
  @Bind(R.id.camera_view) CameraView mCameraView;

  private OnStepCreatedListener mOnStepCreatedListener;

  public CreateStepView(final Context context) {
    this(context, null);
  }

  public CreateStepView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CreateStepView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflateLayout(context);
    mCreateStepButton.setOnClickListener(new _OnClickListener());
    mCameraView.setOnLongClickListener(new _OnLongClickListener());
    mCameraView.setOnImageCapturedListener(new _OnImageCapturedListener());
  }

  public void setOnStepCreatedListener(final OnStepCreatedListener listener) {
    mOnStepCreatedListener = listener;
  }

  public String getStepDescription() {
    final CharSequence text = mStepDescriptionField.getText();
    return text.toString();
  }

  public void clearStepDescriptionField() {
    mStepDescriptionField.setText("");
  }

  private void inflateLayout(final Context context) {
    final ViewGroup layout = (ViewGroup) inflate(context, R.layout.step_creator_view, null);
    addView(layout);  //TODO: Causes LinearLayout to be added to LinearLayout, inefficient and may cause problems.
    ButterKnife.bind(this, layout);
  }

  private void writeDataAsBitmap(final OutputStream os, final byte[] data) {
    final int offset = 0;
    final int length = (data != null) ? data.length : 0;
    Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length);
    final Resources resources = getResources();
    final FixBitmap fixBitmap = new FixBitmapRotationAndScale(resources);
    bitmap = fixBitmap.fixBitmap(bitmap);  //TODO: Lot of processing happens here. Do this off the main thread.
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
  }

  private class _OnClickListener implements View.OnClickListener {
    @Override
    public void onClick(final View view) {
      mCameraView.captureImage();
    }
  }

  private class _OnImageCapturedListener implements OnImageCapturedListener {
    @Override
    public void onImageCaptured(final byte[] data) {
      final String stepDescription = getStepDescription();

      final File imgFile = AppDir.TMP.getFile(System.currentTimeMillis() + ".jpg");
      OutputStream os;

      try {
        os = new FileOutputStream(imgFile);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("couldn't open output stream for " + imgFile, e);
      }

      writeDataAsBitmap(os, data);

      final Step step = new PortableStep(stepDescription, imgFile.getAbsolutePath(), "");
      mOnStepCreatedListener.onStepCreated(step);

      clearStepDescriptionField();
    }
  }

  private class _OnLongClickListener implements OnLongClickListener {
    @Override
    public boolean onLongClick(final View v) {
      final Context context = getContext();
      final Intent endTaskCreationIntent = new Intent(context, TaskInitialiserActivity.class);
      context.startActivity(endTaskCreationIntent);
      return true;
    }
  }

}

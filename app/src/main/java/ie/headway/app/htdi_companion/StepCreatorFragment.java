package ie.headway.app.htdi_companion;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ie.headway.app.htdi_companion.camera.OnImageCapturedListener;
import ie.headway.app.util.AppDir;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Step;

public class StepCreatorFragment extends Fragment {

  private CreateStepView mCreateStepView;
  private OnStepCreatedListener mOnStepCreatedListener;

  public static final StepCreatorFragment newInstance() {
    final StepCreatorFragment stepCreatorFragment = new StepCreatorFragment();
    return stepCreatorFragment;
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    final View layout = inflater.inflate(R.layout.step_creator_fragment, null);
    mCreateStepView = (CreateStepView) layout;
    mCreateStepView.registerOnImageCapturedListener(new OnImageCapturedListener() {
      @Override
      public void onImageCaptured(final byte[] data) {
        final String stepDescription = mCreateStepView.getStepDescription();

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
      }
    });
    return layout;
  }

  public void registerOnStepCreatedListener(final OnStepCreatedListener listener) {
    mOnStepCreatedListener = listener;
  }

  protected void writeDataAsBitmap(final OutputStream os, final byte[] data) {
    final int offset = 0;
    final int length = (data != null) ? data.length : 0;
    Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length);
    final Resources resources = getResources();
    final FixBitmap fixBitmap = new FixBitmapRotationAndScale(resources);
    bitmap = fixBitmap.fixBitmap(bitmap);  //TODO: Lot of processing happens here. Do this off the main thread.
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
  }

}

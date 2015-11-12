package ie.headway.app.htdi_companion;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.io.output.ByteArrayOutputStream;

import ie.headway.app.htdi_companion.camera.OnImageCapturedListener;
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
      public void onImageCaptured(byte[] data) {
        final String stepDescription = mCreateStepView.getStepDescription();
        final Step step = new InMemoryStep(stepDescription, data, null);
        mOnStepCreatedListener.onStepCreated(step);
      }
    });
    return layout;
  }

  public void registerOnStepCreatedListener(final OnStepCreatedListener listener) {
    mOnStepCreatedListener = listener;
  }

  protected ByteArrayOutputStream writeDataToMemory(final byte[] data) {
//    final int offset = 0;
//    final int length = (data != null) ? data.length : 0;
//    final Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length);
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, mOutputStream);

    //new ByteArrayOutputStream(data.length); //TODO: Should that be divided by 4?

    throw new UnsupportedOperationException();
  }

}

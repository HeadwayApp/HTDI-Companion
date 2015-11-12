package ie.headway.app.htdi_companion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import ie.headway.app.htdi_companion.camera.CameraView;
import ie.headway.app.htdi_companion.camera.OnImageCapturedListener;

public class CreateStepView extends LinearLayout {

  public CreateStepView(final Context context) {
    this(context, null);
  }

  public CreateStepView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CreateStepView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflateLayout(context);
  }

  public String getStepDescription() {
    final EditText editText = (EditText) findViewById(R.id.input_step_description_view);
    final CharSequence text = editText.getText();
    return text.toString();
  }

  public void clearStepDescription() {
    final EditText editText = (EditText) findViewById(R.id.input_step_description_view);
    editText.setText("");
  }

  public void registerOnImageCapturedListener(final OnImageCapturedListener listener) {
    final CameraView cameraView = (CameraView) findViewById(R.id.camera_view);
    cameraView.registerOnImageCapturedListener(listener);
  }

  private void inflateLayout(final Context context) {
    final ViewGroup layout = (ViewGroup) inflate(context, R.layout.step_creator_view, null);
    addView(layout);  //Causes LinearLayout to be added to LinearLayout, inefficient and may cause problems.
  }

}

package ie.headway.app.htdi_companion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ie.headway.app.htdi_companion.camera.CameraView;
import ie.headway.app.htdi_companion.camera.OnImageCapturedListener;

/**
 * TODO: Should this class implement {@link ie.headway.app.htdi_companion.camera.CameraControls}?
 * */
public class CreateStepView extends LinearLayout {

  private final EditText mStepDescriptionField;
  private final Button mCreateStepButton;
  private final CameraView mCameraView;

  public CreateStepView(final Context context) {
    this(context, null);
  }

  public CreateStepView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CreateStepView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflateLayout(context);
    mStepDescriptionField = (EditText) findViewById(R.id.input_step_description_view);
    mCreateStepButton = (Button) findViewById(R.id.create_step_button);
    mCreateStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
    public void onClick(final View view) {
        mCameraView.captureImage();
      }
    });
    mCameraView = (CameraView) findViewById(R.id.camera_view);
  }

  public String getStepDescription() {
    final CharSequence text = mStepDescriptionField.getText();
    return text.toString();
  }

  public void clearStepDescriptionField() {
    mStepDescriptionField.setText("");
  }

  public void registerOnImageCapturedListener(final OnImageCapturedListener listener) {
    mCameraView.registerOnImageCapturedListener(listener);
  }

  private void inflateLayout(final Context context) {
    final ViewGroup layout = (ViewGroup) inflate(context, R.layout.step_creator_view, null);
    addView(layout);  //Causes LinearLayout to be added to LinearLayout, inefficient and may cause problems.
  }

}

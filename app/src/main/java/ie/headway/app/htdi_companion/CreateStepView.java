package ie.headway.app.htdi_companion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

  private void inflateLayout(final Context context) {
    final ViewGroup layout = (ViewGroup)inflate(context, R.layout.step_creator_view, null);
    addView(layout);
  }

}

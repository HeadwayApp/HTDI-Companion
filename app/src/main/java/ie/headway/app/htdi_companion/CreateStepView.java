package ie.headway.app.htdi_companion;

import android.content.Context;
import android.util.AttributeSet;
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
    inflate(context, R.layout.step_creator_view, this);
  }

}

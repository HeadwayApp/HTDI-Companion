package ie.headway.app.htdi_companion;

import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import ie.headway.app.xml.Step;

public class StepCreatorFragment extends Fragment {

    private StepBuilder mStepBuilder = new StepBuilder();

	private LinearLayout mView;
	private TextView mText;
	private CameraView mImage;

	public static final StepCreatorFragment newInstance() {
		final StepCreatorFragment stepCreatorFragment = new StepCreatorFragment();
		return stepCreatorFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mView = new LinearLayout(getActivity().getBaseContext());
		mView.setOrientation(VERTICAL);

		mText = new EditText(getActivity().getBaseContext());
		mText.setText(" ");
		mText.setTextSize(COMPLEX_UNIT_SP, 25);
		mText.setGravity(CENTER);
		mText.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		mView.addView(mText);

		mImage = new CameraView(getActivity().getBaseContext());
        mImage.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        mImage.setPadding(0, 0, 0, px2Dp(getActivity(), 15));

		mView.addView(mImage);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mView;
	}

    /**
     * TODO Possible duplicate definition.
     * */
	private int px2Dp(final Activity activity, final int dp) {
		final Resources resources = activity.getResources();
		final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		final float scale = displayMetrics.density;
		int px = (int) (dp * scale + 0.5f);
		return px;
	}

}

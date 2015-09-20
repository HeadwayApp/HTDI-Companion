package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import ie.headway.app.disk.AppDir;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class StepCreatorFragment extends Fragment {

    private static final String TAG = "StepCreatorFragment";

	private Task mTask;

    private LinearLayout mView;
	private TextView mText;
	private CameraView mImage;

    private Button mCreateStepButton;

    final File file = new File(
            Environment.getExternalStorageDirectory() +
                    File.separator + "Headway" +
                    File.separator + "TestCompanion" +
                    File.separator + "1.jpg");

	public static final StepCreatorFragment newInstance(final Task task) {
        final StepCreatorFragment stepCreatorFragment = new StepCreatorFragment();
        final Bundle args = new Bundle();
        args.putParcelable("task", task);
        stepCreatorFragment.setArguments(args);
        return stepCreatorFragment;
    }

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {

        mView = new LinearLayout(getActivity().getBaseContext());
        mView.setOrientation(VERTICAL);

        mText = new EditText(getActivity().getBaseContext());
        mText.setText(" ");
        mText.setTextSize(COMPLEX_UNIT_SP, 25);
        mText.setGravity(CENTER);
        mText.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        mView.addView(mText);

        mCreateStepButton = new Button(getActivity().getBaseContext());
        mCreateStepButton.setText("Create Step");
        mCreateStepButton.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        mCreateStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImage.captureImage();
                final Step step = new Step(mText.getText().toString(), file.getAbsolutePath(), "");

                mTask = getArguments().getParcelable("task");
                mTask.addStep(step);

                final Serializer serializer = new Persister();

                try {
                    serializer.write(mTask, new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "task.xml")));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        mView.addView(mCreateStepButton);

        mImage = new CameraView(getActivity().getBaseContext(), Camera.open(), JpegCallback.newInstance(file));
        mImage.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        mImage.setPadding(0, 0, 0, px2Dp(getActivity(), 15));
        mView.addView(mImage);

		return mView;
	}

    /**
     * TODO Possible duplicate definition.
     * */
	private static int px2Dp(final Activity activity, final int dp) {
		final Resources resources = activity.getResources();
		final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		final float scale = displayMetrics.density;
		final int px = (int) (dp * scale + 0.5f);
		return px;
	}

}

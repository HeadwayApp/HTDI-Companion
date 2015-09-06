package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class SnapActivity extends Activity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

		setContentView(R.layout.activity_snap);

		final Fragment newFragment = StepCreatorFragment.newInstance();
		getFragmentManager().beginTransaction().add(R.id.splash_screen_layout, newFragment, "tagoo").commit();
	}

}

package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import ie.headway.app.xml.Step;

public class SplashScreenActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);

		final Fragment newFragment = StepCreatorFragment.newInstance(new Step("TEXT", "", ""));
		getFragmentManager().beginTransaction().add(R.id.splash_screen_layout, newFragment, "tagoo").commit();
	}

}

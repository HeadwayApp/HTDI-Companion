package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;

import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

public class SnapActivity extends Activity {

    private String mTaskName = "TestCompanion";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

		setContentView(R.layout.activity_snap);

		final Fragment newFragment = StepCreatorFragment.newInstance(
				new Task(mTaskName, new ArrayList<Step>()));

		getFragmentManager().beginTransaction().add(R.id.splash_screen_layout, newFragment, "tagoo")
                .commit();
	}

}

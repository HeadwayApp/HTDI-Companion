package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.util.ArrayList;

import ie.headway.app.disk.AppDir;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

public class SnapActivity extends Activity {

    private String mTaskName = "TestCompanion";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_snap);

		Task task = (Task) getIntent().getParcelableExtra("task");

		task = new Task(task.getName(), new ArrayList<Step>());

		final File file = AppDir.ROOT.getFile(task.getName() + File.separator + "imgs");
		file.mkdirs();

		final Fragment newFragment = StepCreatorFragment.newInstance(task);

		getFragmentManager().beginTransaction().add(R.id.splash_screen_layout, newFragment, "tagoo")
				.commit();
	}

}

package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import ie.headway.app.xml.Task;

public class TaskCreatorActivity extends Activity {

    private Fragment mTaskCreatorFragment;

    private Task mTask;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_task_creator);

		mTask = getIntent().getParcelableExtra("task");

//		task = new Task(task.getName(), new ArrayList<Step>());

        mTask.makeRequiredDirs();
	}

    @Override
    protected void onResume() {
        super.onResume();
        mTaskCreatorFragment = attachTaskCreatorFragmentForTask(mTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachTaskCreatorFragment(mTaskCreatorFragment);
    }

    private Fragment attachTaskCreatorFragmentForTask(final Task task) {
        final Fragment newFragment = TaskCreatorFragment.newInstance(task);
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction()
                .add(R.id.splash_screen_layout, newFragment, task.getName());

        fragmentTransaction.commit();

        return newFragment;
    }

    private void detachTaskCreatorFragment(final Fragment fragment) {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction()
                .remove(fragment);
        fragmentTransaction.commit();
        mTaskCreatorFragment = null;
    }

}

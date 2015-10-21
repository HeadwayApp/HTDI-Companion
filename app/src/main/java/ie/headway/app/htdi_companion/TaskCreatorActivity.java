package ie.headway.app.htdi_companion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import ie.headway.app.xml.Task;

import static com.google.common.base.Preconditions.checkState;

public class TaskCreatorActivity extends HeadwayActivity {

  private Task mTask;
  private TaskCreatorFragment mTaskCreatorFragment;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_creator);
    loadTask();
  }

  @Override
  protected void onResume() {
    super.onResume();
    attachTaskCreatorFragmentForTask(mTask);
  }

  @Override
  protected void onPause() {
    super.onPause();
    detachTaskCreatorFragment();
  }

  protected void attachTaskCreatorFragmentForTask(final Task task) {
    checkState(mTaskCreatorFragment == null, "task creator fragment is already assigned.");
    final String taskName = task.getName();
    mTaskCreatorFragment = TaskCreatorFragment.newInstance(task);
    addFragmentToLayout(R.id.splash_screen_layout, mTaskCreatorFragment, taskName);
  }

  protected void detachTaskCreatorFragment() {
    removeFragmentFromLayout(mTaskCreatorFragment);
    mTaskCreatorFragment = null;
  }

  /**
   * TODO: Proxy for fragment method, may need refactoring.
   */
  public void onClickCreateStepButton(final View view) {
    mTaskCreatorFragment.onClickCreateStepButton(view);
  }

  private void loadTask() {
    final Intent intent = getIntent();
    final Parcelable taskParcleable = intent.getParcelableExtra("task");
    final Task task = (Task) taskParcleable;
    mTask = task;
    mTask.makeRequiredDirs();
  }

}

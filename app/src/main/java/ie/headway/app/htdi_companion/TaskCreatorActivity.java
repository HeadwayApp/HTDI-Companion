package ie.headway.app.htdi_companion;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import ie.headway.app.xml.Task;

import static com.google.common.base.Preconditions.checkState;

public class TaskCreatorActivity extends HeadwayActivity {

  private static final Fragment NO_FRAGMENT = null;

  private Task mTask;
  private Fragment mTaskCreatorFragment;

  private int fragCnt;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_creator);
    loadTask();
  }

  @Override
  protected void onResume() {
    super.onResume();
    attachTaskCreatorFragment();
  }

  @Override
  protected void onPause() {
    super.onPause();
    detachTaskCreatorFragment();
  }

  protected void attachTaskCreatorFragment() {
    checkState(mTaskCreatorFragment == NO_FRAGMENT, "task creator fragment is already assigned");
    final String taskName = mTask.getName();
    mTaskCreatorFragment = TaskCreatorFragment.newInstance(mTask, fragCnt);
    addFragmentToLayout(R.id.splash_screen_layout, mTaskCreatorFragment, taskName + fragCnt++);
  }

  protected void detachTaskCreatorFragment() {
    checkState(mTaskCreatorFragment != NO_FRAGMENT, "no task creator fragment to detach");
    removeFragmentFromLayout(mTaskCreatorFragment);
    mTaskCreatorFragment = NO_FRAGMENT;
  }

  /**
   * TODO: Proxy for fragment method, may need refactoring.
   */
  public void onClickCreateStepButton(final View view) {
    ((TaskCreatorFragment)mTaskCreatorFragment).onClickCreateStepButton(view);
  }

  private void loadTask() {
    final Intent intent = getIntent();
    final Parcelable taskParcleable = intent.getParcelableExtra("task");
    final Task task = (Task) taskParcleable;
    mTask = task;
    mTask.makeRequiredDirs();
  }

}

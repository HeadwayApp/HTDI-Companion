package ie.headway.app.htdi_companion;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;

import ie.headway.app.disk.AppDir;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

import static com.google.common.base.Preconditions.checkState;

public class TaskCreatorActivity extends HeadwayActivity {

  private static final Fragment NO_FRAGMENT = null;

  private Task mTask;
  private Fragment mTaskCreatorFragment;

  private int fragCnt;

  private int stepCnt;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_creator);
    loadTaskFromIntent();
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
    mTaskCreatorFragment = StepCreatorFragment.newInstance();
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
    final Step step = ((StepCreatorFragment)mTaskCreatorFragment).onClickCreateStepButton(view);
    serializeStep(step);
  }

  private void serializeStep(final Step step) {

    ((StepCreatorFragment)mTaskCreatorFragment).setTmpImg();
    ((StepCreatorFragment)mTaskCreatorFragment).setOutputStream();

        final File imgFile = new File(step.getImagePath());
        while(!imgFile.exists()) {
          try { Thread.sleep(1000); } catch (InterruptedException ie) {}
        }

        final File newImg = new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "imgs" + File.separator + stepCnt++ + ".jpg"));

        try {
          FileUtils.moveFile(imgFile, newImg);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        mTask.addStep(new PortableStep(step.getText(), newImg.getAbsolutePath().replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "EXTERNAL_STORAGE_DIRECTORY"), ""));

        final Serializer serializer = new Persister();

        try {
          serializer.write(mTask, new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "task.xml")));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
  }

  private void loadTaskFromIntent() {
    final Intent intent = getIntent();
    final Parcelable taskParcleable = intent.getParcelableExtra("task");
    final Task task = (Task) taskParcleable;
    mTask = task;
    mTask.makeRequiredDirs();
  }

}

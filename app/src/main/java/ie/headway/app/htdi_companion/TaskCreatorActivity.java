package ie.headway.app.htdi_companion;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import ie.headway.app.disk.AppDir;
import ie.headway.app.htdi_companion.tmp.util.TaskSerialiser;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

public class TaskCreatorActivity extends HeadwayActivity {

  private Task mTask;
  private StepCreatorFragment mStepCreatorFragment;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_creator);
    mTask = getTask();
    mTask.makeRequiredDirs();
    mStepCreatorFragment = getStepCreatorFragment();
  }

  public void onClickCreateStepButton(final View view) {
    final Step step = mStepCreatorFragment.onClickCreateStepButton(view);
    serializeStep(step);
  }

  private void serializeStep(final Step step) {

    mStepCreatorFragment.refresh();

    final File imgFile = new File(step.getImagePath());
    while(!imgFile.exists()) {
      try { Thread.sleep(1000); } catch (InterruptedException ie) {}
    }

    final File newImg = new File(AppDir.ROOT.getPath(mTask.getName() + File.separator + "imgs" + File.separator + mTask.getStepCount() + ".jpg"));

    try {
      FileUtils.moveFile(imgFile, newImg);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    mTask.addStep(new PortableStep(step.getText(), newImg.getAbsolutePath(), ""));

    final TaskSerialiser taskSerialiser = new TaskSerialiser();
    taskSerialiser.write(mTask);

  }

  private Task getTask() {
    final Intent intent = getIntent();
    final Parcelable taskParcleable = intent.getParcelableExtra("task");
    final Task task = (Task) taskParcleable;
    return task;
  }

  private StepCreatorFragment getStepCreatorFragment() {
    final FragmentManager fragmentManager = getFragmentManager();
    final Fragment fragment = fragmentManager.findFragmentById(R.id.step_creator_fragment);
    final StepCreatorFragment stepCreatorFragment = (StepCreatorFragment)fragment;
    return stepCreatorFragment;
  }

}

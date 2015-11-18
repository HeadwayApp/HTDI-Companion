package ie.headway.app.htdi__companion.gui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import butterknife.Bind;
import butterknife.ButterKnife;
import ie.headway.app.htdi__companion.R;
import ie.headway.app.util.HeadwayActivity;
import ie.headway.app.xml.PortableStep;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.StepUtils;
import ie.headway.app.xml.Task;
import ie.headway.app.xml.TaskPersister;

public class TaskCreatorActivity extends HeadwayActivity {

  private static final TaskPersister TASK_SERIALIZER = new TaskPersister();

  private Task mTask;
  @Bind(R.id.create_step_view) CreateStepView mCreateStepView;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_creator);
    ButterKnife.bind(this);
    mTask = getTask();
    mTask.makeRequiredDirs();
    mCreateStepView.setOnStepCreatedListener(new OnStepCreatedListener() {
      @Override
      public void onStepCreated(final Step step) {
        serializeStep(step);
      }
    });
  }

  private void serializeStep(final Step step) {
    final PortableStep contextualisedStep = StepUtils.contextualiseStep(mTask, step);
    mTask.addStep(contextualisedStep);
    TASK_SERIALIZER.write(mTask);
  }

  private Task getTask() {
    final Intent intent = getIntent();
    final Parcelable taskParcleable = intent.getParcelableExtra("task");
    final Task task = (Task) taskParcleable;
    return task;
  }

}

package ie.headway.app.htdi_companion.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.headway.app.htdi_companion.R;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;
import ie.headway.app.xml.TaskNotFoundException;
import ie.headway.app.xml.TaskPersister;

public class TaskInitialiserActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_initialiser);
  }

  public void onClickStartTaskCreationButton(final View v) {
    final Context context = getApplicationContext();
    final String taskNameFromView = getEnteredTaskName();

    if (taskNameFromView.isEmpty()) {
      toastTaskName(context);
    } else {
      startTaskCreation(context, taskNameFromView);
    }
  }

  private String getEnteredTaskName() {
    final EditText inputTaskNameView = (EditText) findViewById(R.id.input_task_name_view);
    final String taskNameFromView = inputTaskNameView.getText().toString();
    return taskNameFromView.trim().replaceAll("[^a-zA-Z0-9-_\\.]", "_"); //Sanitise input for file name.
  }

  private void toastTaskName(final Context context) {
    final String toastText = getString(R.string.enter_task_toast_text);
    final Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
    toast.show();
  }

  private void startTaskCreation(final Context context, final String taskName) {
    final Task task = loadTask(taskName);

    final Intent intent = new Intent(context, TaskCreatorActivity.class);
    intent.putExtra("task", task);

    startActivity(intent);
  }

  private Task loadTask(final String taskName) {
    final TaskPersister taskDeserialiser = new TaskPersister(taskName);

    Task task;

    try {
      task = taskDeserialiser.read();
    } catch (TaskNotFoundException tnf) {
      task = makeNewTask(taskName);
    }

    return task;
  }

  private static Task makeNewTask(final String taskName) {
    final List<Step> stepsLst = new ArrayList<>(10);
    final Task task = new Task(taskName, stepsLst);
    return task;
  }

}
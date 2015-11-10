package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.headway.app.htdi_companion.tmp.util.TaskNotFoundException;
import ie.headway.app.htdi_companion.tmp.util.TaskPersister;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

public class TaskInitialiserActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_initialiser);
  }

  public void onClickStartTaskCreationButton(final View v) {
    final Context context = getApplicationContext();
    final String taskNameFromView = getCurrentlyEnteredTaskNameText();

    if (taskNameFromView.isEmpty()) {
      showEnterTaskNameToast(context);
    } else {
      startTaskCreation(context, taskNameFromView);
    }
  }

  private String getCurrentlyEnteredTaskNameText() {
    final EditText inputTaskNameView = (EditText) findViewById(R.id.input_task_name_view);
    final String taskNameFromView = inputTaskNameView.getText().toString();
    return taskNameFromView;
  }

  private void showEnterTaskNameToast(final Context context) {
    final String toastText = getString(R.string.enter_task_toast_text);
    final Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
    toast.show();
  }

  private void startTaskCreation(final Context context, final CharSequence taskName) {

    final TaskPersister taskDeserialiser = new TaskPersister();

    Task task;

    try {
      task = taskDeserialiser.read(taskName.toString());
    } catch (TaskNotFoundException tnf) {
      final String taskNameStr = taskName.toString();
      final List<Step> stepsLst = new ArrayList<>(10);
      task = new Task(taskNameStr, stepsLst);
    }

    final Intent intent = new Intent(context, TaskCreatorActivity.class);
    intent.putExtra("task", task);

    startActivity(intent);
  }

}

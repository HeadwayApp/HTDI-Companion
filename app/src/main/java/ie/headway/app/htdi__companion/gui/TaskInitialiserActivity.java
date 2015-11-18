package ie.headway.app.htdi__companion.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import ie.headway.app.htdi__companion.R;
import ie.headway.app.xml.task.Task;
import ie.headway.app.xml.task.TaskAlreadyExistsException;

public class TaskInitialiserActivity extends Activity {

  @Bind(R.id.input_task_name_view) EditText mInputTaskNameView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_initialiser);
    ButterKnife.bind(this);
  }

  public void onClickStartTaskCreationButton(final View v) {
    final Context context = getApplicationContext();
    final String taskNameFromView = getEnteredTaskName();

    if (taskNameFromView.isEmpty()) {
      showEmptyTaskNameToast(context);
    } else {
      try {
        startTaskCreation(context, taskNameFromView);
      } catch (TaskAlreadyExistsException tae) {
        mInputTaskNameView.setText("");
        showTaskAlreadyExistsToast();
      }
    }
  }

  private String getEnteredTaskName() {
    final String taskNameFromView = mInputTaskNameView.getText().toString();
    return taskNameFromView.trim().replaceAll("[^a-zA-Z0-9-_\\.]", "_"); //Sanitise input for file name.
  }

  private void showTaskAlreadyExistsToast() {
    final String toastText = getString(R.string.task_already_exists_toast_text);
    final Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_LONG);
    toast.show();
  }

  private void showEmptyTaskNameToast(final Context context) {
    final String toastText = getString(R.string.enter_task_toast_text);
    final Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
    toast.show();
  }

  private void startTaskCreation(final Context context, final String taskName) throws TaskAlreadyExistsException {
    final Task task = Task.newInstance(taskName);
    final Intent intent = new Intent(context, TaskCreatorActivity.class);
    intent.putExtra("task", task);
    startActivity(intent);
  }

}

package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ie.headway.app.htdi_companion.R.layout;
import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

public class TaskInitialiserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_task_initialiser);
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
        final EditText inputTaskNameView = (EditText)findViewById(R.id.input_task_name_view);
        final String taskNameFromView = inputTaskNameView.getText().toString();
        return taskNameFromView;
    }

    private void showEnterTaskNameToast(final Context context) {
        final String toastText = getString(R.string.enter_task_toast_text);
        final Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void startTaskCreation(final Context context, final CharSequence taskName) {
        final Intent intent = new Intent(context, SnapActivity.class);
        final Task task = new Task(taskName.toString(), new ArrayList<Step>(10));
        intent.putExtra("task", task);
        startActivity(intent);
    }

}

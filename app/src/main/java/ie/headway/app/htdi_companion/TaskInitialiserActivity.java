package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ie.headway.app.xml.Step;
import ie.headway.app.xml.Task;

public class TaskInitialiserActivity extends Activity {

    private LinearLayout mContentView;

    private EditText mInputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentView = new LinearLayout(this));
        mContentView.setOrientation(LinearLayout.VERTICAL);
        mContentView.addView(getTextView());
        mContentView.addView(getTextBox());
        mContentView.addView(getEntryButton());
    }

    private TextView getTextView() {
        final TextView tv = new TextView(this);
        tv.setText("Enter name of task");
        return tv;
    }

    private EditText getTextBox() {
        final EditText et = new EditText(this);
        mInputText = et;
        return et;
    }

    private Button getEntryButton() {
        final Button btn = new Button(this);
        btn.setText("Create Task");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputText.getText().length() < 1) {
                    Toast.makeText(
                            TaskInitialiserActivity.this, "Enter name of task", Toast.LENGTH_LONG)
                            .show();
                } else {
                    final Intent intent = new Intent(TaskInitialiserActivity.this, SnapActivity.class);
                    final Task task = new Task(mInputText.getText().toString(), new ArrayList<Step>());
                    intent.putExtra("task", task);
                    startActivity(intent);
                }
            }
        });

        return btn;
    }

}

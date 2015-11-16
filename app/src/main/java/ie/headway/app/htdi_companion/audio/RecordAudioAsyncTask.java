package ie.headway.app.htdi_companion.audio;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

public class RecordAudioAsyncTask extends AsyncTask<String, Integer, Void> {

  private final Context mContext;
  private final ProgressDialog mProgressDialog;

  private MediaRecorder mAudioRecorder;

  public RecordAudioAsyncTask(final Context context) {
    mContext = context;
    mProgressDialog = new ProgressDialog(context);
  }

  @Override
  protected void onPreExecute() {
    mProgressDialog.show();
  }

  @Override
  protected Void doInBackground(final String... params) {
    checkArgument(params.length == 1, "wrong number of parameters, should be params.length == 1");



    return null;
  }


}

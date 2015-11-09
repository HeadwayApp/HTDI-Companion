package ie.headway.app.htdi_companion.tmp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class StartActivityRunnable implements Runnable {

  private final Context mContext;
  private final Intent mIntent;

  public StartActivityRunnable(final Context context,
                               final Class<? extends Activity> activityClass) {
    mContext = context;

    final Intent intent = new Intent(context, activityClass);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    mIntent = intent;
  }

  @Override
  public void run() {
    mContext.startActivity(mIntent);
  }

}

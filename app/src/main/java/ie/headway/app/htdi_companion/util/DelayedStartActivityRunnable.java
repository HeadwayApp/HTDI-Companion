package ie.headway.app.htdi_companion.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

public class DelayedStartActivityRunnable extends StartActivityRunnable {

  private long mDelay;

  public DelayedStartActivityRunnable(final Context context,
                                      final Class<? extends Activity> activityClass) {
    super(context, activityClass);
  }

  public long getDelay() {
    return mDelay;
  }

  public void setDelay(final long delay) {
    mDelay = delay;
  }

  @Override
  public void run() {
    final Runnable superRunnable = makeRunnableFromSuper();
    runAfterDelay(superRunnable, mDelay);
  }

  private Runnable makeRunnableFromSuper() {
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        DelayedStartActivityRunnable.super.run();
      }
    };

    return runnable;
  }

  private static void runAfterDelay(final Runnable runnable, final long delay) {
    final Handler handler = new Handler();
    handler.postDelayed(runnable, delay);
  }

}

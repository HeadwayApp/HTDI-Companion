package ie.headway.app.htdi_companion.splash;

import android.content.Context;
import android.os.Bundle;

import ie.headway.app.HeadwaySplashScreenActivity;
import ie.headway.app.htdi_companion.TaskInitialiserActivity;
import ie.headway.app.htdi_companion.util.DelayedStartActivityRunnable;

import static com.google.common.base.Preconditions.checkArgument;

public class SplashScreenActivity extends HeadwaySplashScreenActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void exitSplashScreen(final long delay) {
    checkArgument(delay >= 0L, "delay value is negative");
    startTaskInitialiserActivityWithDelay(delay);
  }

  private void startTaskInitialiserActivityWithDelay(final long delay) {
    final Context applicationContext = getApplicationContext();
    final DelayedStartActivityRunnable startActivityRunnable =
        new DelayedStartActivityRunnable(applicationContext, TaskInitialiserActivity.class);

    startActivityRunnable.setDelay(delay);
    startActivityRunnable.run();
  }

}

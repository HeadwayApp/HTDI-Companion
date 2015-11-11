package ie.headway.app.htdi_companion;

import android.content.Context;
import android.content.Intent;

import ie.headway.app.util.HeadwaySplashScreenActivity;

public class SplashScreenActivity extends HeadwaySplashScreenActivity {

  @Override
  protected void exitSplashScreen(final long delay) {
    final Context context = getBaseContext();
    final Intent intent = new Intent(context, TaskInitialiserActivity.class);
    runAfterDelay(new Runnable() {
      @Override
      public void run() {
        startActivity(intent);
      }
    }, delay);
  }

}

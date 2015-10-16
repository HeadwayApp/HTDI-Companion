package ie.headway.app.htdi_companion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ie.headway.app.HeadwaySplashScreenActivity;

import static com.google.common.base.Preconditions.checkArgument;

public class SplashScreenActivity extends HeadwaySplashScreenActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void exitSplashScreen(final long delay) {
    checkArgument(delay >= 0, "delay value is negative");
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        final Intent intent = new Intent(getApplicationContext(), TaskInitialiserActivity.class);
        startActivity(intent);
      }
    }, delay);
  }

}

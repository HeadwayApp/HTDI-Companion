package ie.headway.app.htdi_companion;

import android.content.Intent;
import android.os.Handler;

import ie.headway.app.HeadwaySplashScreenActivity;

public class SplashScreenActivity extends HeadwaySplashScreenActivity {

    @Override
    protected void exitSplashScreen(long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent i = new Intent(getApplicationContext(), SnapActivity.class);
                startActivity(i);
            }
        }, delay);
    }

}

package ie.headway.app;

import android.app.Activity;
import android.os.Bundle;

import static ie.headway.app.disk.AppDir.makeAppDirs;

public abstract class HeadwaySplashScreenActivity extends Activity {

  public HeadwaySplashScreenActivity() {
    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
  }

  @Override
  protected void onCreate(final Bundle savedInstanceBundle) {
    super.onCreate(savedInstanceBundle);
    setContentView(R.layout.activity_splash_screen);
    makeAppDirs();    //NOTE  Having this across both apps may cause problems, keep that in mind.
//    exitSplashScreen(5000);
    exitSplashScreen(1);
  }

  /**
   * TODO Inspect this method to see if it can be improved,
   * or if a more efficient approach can be taken. This code
   * was copy-pasta from stackOverflow and I don't know how it works.
   * <p/>
   * Override this method to decide what will happen after the SplashScreen executes.
   * <p/>
   * TODO ^^^Improve this javadoc, give better explanation.^^^
   */
  protected abstract void exitSplashScreen(final long delay);

}

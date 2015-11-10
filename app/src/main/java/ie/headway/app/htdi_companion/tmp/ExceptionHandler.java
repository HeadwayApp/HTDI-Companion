package ie.headway.app.htdi_companion.tmp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
//    Log.e("headway", "exception occurred", ex);
    final File logFile = new File("/sdcard/headway.log");
    try {
      final PrintStream ps = new PrintStream(logFile);
      ex.printStackTrace(ps);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

}

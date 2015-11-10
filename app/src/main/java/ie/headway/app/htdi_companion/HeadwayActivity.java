package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

import ie.headway.app.disk.AppDir;

public class HeadwayActivity extends Activity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    emptyTmpDir();
  }

  private void emptyTmpDir() {
    try {
      AppDir.TMP.empty();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

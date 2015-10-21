package ie.headway.app.htdi_companion;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class HeadwayActivity extends Activity {

  protected void addFragmentToLayout(final int layoutId, final Fragment fragment, final String tag) {
    final FragmentManager fragmentManager = getFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(layoutId, fragment, tag);
    fragmentTransaction.commit();
  }

  protected void removeFragmentFromLayout(final Fragment fragment) {
    final FragmentManager fragmentManager = getFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.remove(fragment);
    fragmentTransaction.commit();
  }

}

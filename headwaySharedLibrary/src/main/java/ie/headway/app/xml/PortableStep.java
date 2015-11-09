package ie.headway.app.xml;

import android.os.Environment;

public class PortableStep extends Step {

  /**
   * This constant represents the system dependant path element which PortableStep will resolve.
   */
  public static final String PATH_ARTIFACT = "EXTERNAL_STORAGE_DIRECTORY";

  public PortableStep() {

  }

  public PortableStep(String text, String imagePath, String audioPath) {
    super(text, imagePath, audioPath);
  }

  @Override
  public String getImagePath() {
    return super.getImagePath().replace(
        PATH_ARTIFACT, Environment.getExternalStorageDirectory().getPath());
  }

  @Override
  public String getAudioPath() {
    return super.getAudioPath().replace(
        PATH_ARTIFACT, Environment.getExternalStorageDirectory().getPath());
  }

}

package ie.headway.app.htdi_companion.tmp.util;

import org.simpleframework.xml.core.Persister;

import java.io.File;

import ie.headway.app.disk.AppDir;
import ie.headway.app.xml.Task;

/**
 * TODO: If one of the steps serialised is a PortableStep, do the replace(PATH_ARTIFACT) there?
 * */
public class TaskSerialiser extends Persister {

  public void write(final Task task) {
    final File file = new File(AppDir.ROOT.getPath(task.getName() + File.separator + "task.xml"));
    try {
      write(task, file);
    } catch (Exception e) {
      throw new RuntimeException("couldn't serialize " + task.toString());
    }
  }

}

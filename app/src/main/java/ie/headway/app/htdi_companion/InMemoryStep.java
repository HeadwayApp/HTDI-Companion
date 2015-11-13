package ie.headway.app.htdi_companion;

import org.apache.commons.io.output.ByteArrayOutputStream;

import ie.headway.app.xml.PortableStep;

public class InMemoryStep extends PortableStep {

  public InMemoryStep() {
  }

  /**
   * TODO: Pass Artefact.MEM_STEP to super(), so if the mem step is serialized too early an exception is thrown by the Persister either upon writing or reading?
   * */
  public InMemoryStep(final String text, final ByteArrayOutputStream imageData, final ByteArrayOutputStream audioData) {
    super(text, artefisePath(imagePath), artefisePath(audioPath));
  }

  @Override
  public String getImagePath() {
    final String artifisedPath = super.getImagePath();
    return normalisePath(artifisedPath);
  }

  @Override
  public String getAudioPath() {
    final String artifisedPath = super.getAudioPath();
    return normalisePath(artifisedPath);
  }

}

package ie.headway.app.htdi_companion;

import ie.headway.app.xml.Step;

public class StepBuilder {

    private String mText;
    private String mImagePath;
    private String mAudioPath;

    public void setText(CharSequence text) {
        mText = text.toString();
    }

    public void setImagePath(CharSequence imagePath) {
        mImagePath = imagePath.toString();
    }

    public void setAudioPath(CharSequence audioPath) {
        mAudioPath = audioPath.toString();
    }

    public Step buildStep() {
        if(hasUnassignedFields()) {
            throw new IllegalStateException("StepBuilder has unassigned fields.");
        }else {
            return new Step(mText, mImagePath, mAudioPath);
        }
    }

    private boolean hasUnassignedFields() {
        return mText != null && mImagePath != null && mAudioPath != null;
    }

}

package ie.headway.app.xml;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

@Root
public class Task implements Parcelable {

	@Attribute private String name;
    @ElementListUnion({
        @ElementList(entry="step", inline=true, type=Step.class),
        @ElementList(entry="portable_step", inline=true, type=PortableStep.class)               
    })
	private List<Step> steps;
	
	public Task() {
		
	}
	
	public Task(final String name, List<Step> steps) {
		this.name = name;
		this.steps = steps;
	}

    public String getName() {
        return name;
    }

	public void addStep(Step step) {
		steps.add(step);
	}
	
	public Step getStep(int index) {
		return steps.get(index);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeList(steps);
	}

	public Task(final Parcel in) {
		name = in.readString();
		in.readList(steps, null);
	}

	public static final Creator<Task> CREATOR
			= new Creator<Task>() {
		public Task createFromParcel(final Parcel in) {
			return new Task(in);
		}

		public Task[] newArray(final int size) {
			return new Task[size];
		}
	};

}

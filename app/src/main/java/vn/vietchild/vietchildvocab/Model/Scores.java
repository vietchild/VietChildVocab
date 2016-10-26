package vn.vietchild.vietchildvocab.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nguyen Phung Hung on 07/10/16.
 */

public class Scores implements Parcelable{
    String topicname;
    String topicalias;
    String topicdesc;
    Float topicscore;

    public Scores(String topicname, String topicalias, String topicdesc, Float topicscore) {
        this.topicname = topicname;
        this.topicalias = topicalias;
        this.topicdesc = topicdesc;
        this.topicscore = topicscore;
    }

    public Scores() {
    }

    protected Scores(Parcel in) {
        topicname = in.readString();
        topicalias = in.readString();
        topicdesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topicname);
        dest.writeString(topicalias);
        dest.writeString(topicdesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Scores> CREATOR = new Creator<Scores>() {
        @Override
        public Scores createFromParcel(Parcel in) {
            return new Scores(in);
        }

        @Override
        public Scores[] newArray(int size) {
            return new Scores[size];
        }
    };

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }

    public Float getTopicscore() {
        return topicscore;
    }

    public void setTopicscore(Float topicscore) {
        this.topicscore = topicscore;
    }

    public String getTopicdesc() {
        return topicdesc;
    }

    public void setTopicdesc(String topicdesc) {
        this.topicdesc = topicdesc;
    }

    public String getTopicalias() {
        return topicalias;
    }

    public void setTopicalias(String topicalias) {
        this.topicalias = topicalias;
    }
}

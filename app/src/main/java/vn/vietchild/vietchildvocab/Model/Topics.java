package vn.vietchild.vietchildvocab.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Nguyen Phung Hung on 03/10/16.
 */

public class Topics implements Parcelable {
    String topicname;
    String topicalias;
    String topicdesc;
    List<Item> Items;

    public Topics(String topicname, List<Item> items, String topicdesc, String topicalias) {
        this.topicname = topicname;
        Items = items;
        this.topicdesc = topicdesc;
        this.topicalias = topicalias;
    }

    public Topics() {

    }

    protected Topics(Parcel in) {
        topicname = in.readString();
        topicalias = in.readString();
        topicdesc = in.readString();
        Items = in.createTypedArrayList(Item.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topicname);
        dest.writeString(topicalias);
        dest.writeString(topicdesc);
        dest.writeTypedList(Items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Topics> CREATOR = new Creator<Topics>() {
        @Override
        public Topics createFromParcel(Parcel in) {
            return new Topics(in);
        }

        @Override
        public Topics[] newArray(int size) {
            return new Topics[size];
        }
    };

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }

    public String getTopicalias() {
        return topicalias;
    }

    public void setTopicalias(String topicalias) {
        this.topicalias = topicalias;
    }

    public String getTopicdesc() {
        return topicdesc;
    }

    public void setTopicdesc(String topicdesc) {
        this.topicdesc = topicdesc;
    }

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }
}

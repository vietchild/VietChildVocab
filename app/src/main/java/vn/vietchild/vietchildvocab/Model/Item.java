package vn.vietchild.vietchildvocab.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Nguyen Phung Hung on 05/10/16.
 */
@IgnoreExtraProperties
public class Item implements Parcelable{
    String itemalias;
    String itemname;
    String itempronunciation;
    String itemdesc;
    int itemposition;

    public Item() {
    }

    public Item(String itemalias, String itemname, String itempronunciation, String itemdesc, int itemposition) {
        this.itemalias = itemalias;
        this.itemname = itemname;
        this.itempronunciation = itempronunciation;
        this.itemdesc = itemdesc;
        this.itemposition = itemposition;
    }

    protected Item(Parcel in) {
        itemalias = in.readString();
        itemname = in.readString();
        itempronunciation = in.readString();
        itemdesc = in.readString();
        itemposition = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemalias);
        dest.writeString(itemname);
        dest.writeString(itempronunciation);
        dest.writeString(itemdesc);
        dest.writeInt(itemposition);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getItemalias() {
        return itemalias;
    }

    public void setItemalias(String itemalias) {
        this.itemalias = itemalias;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItempronunciation() {
        return itempronunciation;
    }

    public void setItempronunciation(String itempronunciation) {
        this.itempronunciation = itempronunciation;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public int getItemposition() {
        return itemposition;
    }

    public void setItemposition(int itemposition) {
        this.itemposition = itemposition;
    }
}

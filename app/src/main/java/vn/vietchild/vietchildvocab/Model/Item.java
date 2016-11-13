package vn.vietchild.vietchildvocab.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Nguyen Phung Hung on 05/10/16.
 */
@IgnoreExtraProperties
public class Item implements Parcelable{
    String itemalias; // Quan ly anh + sound cua item
    String itemname;  // ten cua item
    String itempronunciation;  // Cach doc
    String itemdesc; // Cach su dung
    int itemposition = 0; //vi tri cua item
    int itempassed = 0;   // 0 - item chua hoc xong, 1 item da hoc xong
    int itemlearning = 0; // 0 - item chua hoc, hoac hoc xong roi, 1 item dang hoc
    int itemmistake = 0;  // so luong loi lien quan den item
    long itemreceiveddate = 0; // ngay bat dau hoc item
    long itempasseddate = 0;  // ngay hoc xong item

    public Item() {
    }

    public Item(String itemalias, String itemname, String itempronunciation, String itemdesc, int itemposition, int itempassed, int itemlearning, int itemmistake, long itemreceiveddate, long itempasseddate) {
        this.itemalias = itemalias;
        this.itemname = itemname;
        this.itempronunciation = itempronunciation;
        this.itemdesc = itemdesc;
        this.itemposition = itemposition;
        this.itempassed = itempassed;
        this.itemlearning = itemlearning;
        this.itemmistake = itemmistake;
        this.itemreceiveddate = itemreceiveddate;
        this.itempasseddate = itempasseddate;
    }


    protected Item(Parcel in) {
        itemalias = in.readString();
        itemname = in.readString();
        itempronunciation = in.readString();
        itemdesc = in.readString();
        itemposition = in.readInt();
        itempassed = in.readInt();
        itemlearning = in.readInt();
        itemmistake = in.readInt();
        itemreceiveddate = in.readLong();
        itempasseddate = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemalias);
        dest.writeString(itemname);
        dest.writeString(itempronunciation);
        dest.writeString(itemdesc);
        dest.writeInt(itemposition);
        dest.writeInt(itempassed);
        dest.writeInt(itemlearning);
        dest.writeInt(itemmistake);
        dest.writeLong(itemreceiveddate);
        dest.writeLong(itempasseddate);
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

    public int getItempassed() {
        return itempassed;
    }

    public void setItempassed(int itempassed) {
        this.itempassed = itempassed;
    }

    public int getItemlearning() {
        return itemlearning;
    }

    public void setItemlearning(int itemlearning) {
        this.itemlearning = itemlearning;
    }

    public int getItemmistake() {
        return itemmistake;
    }

    public void setItemmistake(int itemmistake) {
        this.itemmistake = itemmistake;
    }

    public long getItemreceiveddate() {
        return itemreceiveddate;
    }

    public void setItemreceiveddate(long itemreceiveddate) {
        this.itemreceiveddate = itemreceiveddate;
    }

    public long getItempasseddate() {
        return itempasseddate;
    }

    public void setItempasseddate(long itempasseddate) {
        this.itempasseddate = itempasseddate;
    }
}

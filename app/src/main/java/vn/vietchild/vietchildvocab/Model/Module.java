package vn.vietchild.vietchildvocab.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Nguyen Phung Hung on 24/10/16.
 */
@IgnoreExtraProperties
public class Module implements Parcelable{
    String modulename, modulevideo,moduleimage,moduledescription;
    int modulescore = 0;
    List<Item> items;

    public Module() {
    }

    public Module(String modulename, String modulevideo, String moduleimage, String moduledescription, List<Item> items) {
        this.modulename = modulename;
        this.modulevideo = modulevideo;
        this.moduleimage = moduleimage;
        this.moduledescription = moduledescription;

        this.items = items;
    }

    protected Module(Parcel in) {
        modulename = in.readString();
        modulevideo = in.readString();
        moduleimage = in.readString();
        moduledescription = in.readString();
        modulescore = in.readInt();
        items = in.createTypedArrayList(Item.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(modulename);
        dest.writeString(modulevideo);
        dest.writeString(moduleimage);
        dest.writeString(moduledescription);
        dest.writeInt(modulescore);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public String getModulevideo() {
        return modulevideo;
    }

    public void setModulevideo(String modulevideo) {
        this.modulevideo = modulevideo;
    }

    public String getModuleimage() {
        return moduleimage;
    }

    public void setModuleimage(String moduleimage) {
        this.moduleimage = moduleimage;
    }

    public String getModuledescription() {
        return moduledescription;
    }

    public void setModuledescription(String moduledescription) {
        this.moduledescription = moduledescription;
    }

    public int getModulescore() {
        return modulescore;
    }

    public void setModulescore(int modulescore) {
        this.modulescore = modulescore;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

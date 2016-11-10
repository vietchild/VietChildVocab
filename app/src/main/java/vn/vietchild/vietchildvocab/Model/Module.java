package vn.vietchild.vietchildvocab.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Nguyen Phung Hung on 24/10/16.
 */
@IgnoreExtraProperties
public class Module{
    private String modulename, modulevideo,moduledescription,modulealias;
    private int moduletotalitems,modulelearneditems;
    private int modulepassed = 0; // Trang thai module da hoc xong chua
    int modulescore = 0; // Diem hien tai cua module
    int moduleposition; // Vi tri cua module trong course
    int modulescoretopass; // Diem can thiet de qua module va active module tiep theo
    int moduleactive; // module co duoc active ko
    long moduleactivedate; //ngay bat dau nhan module
    long modulefinishdate; // ngay ket thuc module
    List<Item> items;

    public Module() {
    }

    public Module(String modulename, String modulevideo, String moduledescription, String modulealias, int moduletotalitems, int modulelearneditems, int modulepassed, int modulescore, int moduleposition, int modulescoretopass, int moduleactive, long moduleactivedate, long modulefinishdate, List<Item> items) {
        this.modulename = modulename;
        this.modulevideo = modulevideo;
        this.moduledescription = moduledescription;
        this.modulealias = modulealias;
        this.moduletotalitems = moduletotalitems;
        this.modulelearneditems = modulelearneditems;
        this.modulepassed = modulepassed;
        this.modulescore = modulescore;
        this.moduleposition = moduleposition;
        this.modulescoretopass = modulescoretopass;
        this.moduleactive = moduleactive;
        this.moduleactivedate = moduleactivedate;
        this.modulefinishdate = modulefinishdate;
        this.items = items;
    }

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

    public String getModuledescription() {
        return moduledescription;
    }

    public void setModuledescription(String moduledescription) {
        this.moduledescription = moduledescription;
    }

    public String getModulealias() {
        return modulealias;
    }

    public void setModulealias(String modulealias) {
        this.modulealias = modulealias;
    }

    public int getModuletotalitems() {
        return moduletotalitems;
    }

    public void setModuletotalitems(int moduletotalitems) {
        this.moduletotalitems = moduletotalitems;
    }

    public int getModulelearneditems() {
        return modulelearneditems;
    }

    public void setModulelearneditems(int modulelearneditems) {
        this.modulelearneditems = modulelearneditems;
    }

    public int getModulepassed() {
        return modulepassed;
    }

    public void setModulepassed(int modulepassed) {
        this.modulepassed = modulepassed;
    }

    public int getModulescore() {
        return modulescore;
    }

    public void setModulescore(int modulescore) {
        this.modulescore = modulescore;
    }

    public int getModuleposition() {
        return moduleposition;
    }

    public void setModuleposition(int moduleposition) {
        this.moduleposition = moduleposition;
    }

    public int getModulescoretopass() {
        return modulescoretopass;
    }

    public void setModulescoretopass(int modulescoretopass) {
        this.modulescoretopass = modulescoretopass;
    }

    public int getModuleactive() {
        return moduleactive;
    }

    public void setModuleactive(int moduleactive) {
        this.moduleactive = moduleactive;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public long getModuleactivedate() {
        return moduleactivedate;
    }

    public void setModuleactivedate(long moduleactivedate) {
        this.moduleactivedate = moduleactivedate;
    }

    public long getModulefinishdate() {
        return modulefinishdate;
    }

    public void setModulefinishdate(long modulefinishdate) {
        this.modulefinishdate = modulefinishdate;
    }
}

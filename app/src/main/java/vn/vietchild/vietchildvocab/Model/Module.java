package vn.vietchild.vietchildvocab.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Nguyen Phung Hung on 24/10/16.
 */
@IgnoreExtraProperties
public class Module{
    private String modulename, modulevideo,moduleimage,moduledescription,modulealias;
    private int moduletotalitems,modulelearneditems;
    private int modulepassed = 0;
    int modulescore = 0;
    List<Item> items;

    public Module() {
    }

    public Module(String modulename, String modulevideo, String moduleimage, String moduledescription, String modulealias, int moduletotalitems, int modulelearneditems, int modulepassed, int modulescore, List<Item> items) {
        this.modulename = modulename;
        this.modulevideo = modulevideo;
        this.moduleimage = moduleimage;
        this.moduledescription = moduledescription;
        this.modulealias = modulealias;
        this.moduletotalitems = moduletotalitems;
        this.modulelearneditems = modulelearneditems;
        this.modulepassed = modulepassed;
        this.modulescore = modulescore;
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

    public int isModulepassed() {
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

package vn.vietchild.vietchildvocab.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nguyen Phung Hung on 23/10/16.
 */
@IgnoreExtraProperties
public class Course {

    public String coursename,coursedescription,courseid;
    public Long courseprice,coursetotalitems;
    HashMap<String,Module> modules;

    public Course() {
    }

    public Course(String coursename, String coursedescription, String courseid, Long courseprice, Long coursetotalitems, HashMap<String, Module> modules) {
        this.coursename = coursename;
        this.coursedescription = coursedescription;
        this.courseid = courseid;
        this.courseprice = courseprice;
        this.coursetotalitems = coursetotalitems;
        this.modules = modules;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCoursedescription() {
        return coursedescription;
    }

    public void setCoursedescription(String coursedescription) {
        this.coursedescription = coursedescription;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public Long getCourseprice() {
        return courseprice;
    }

    public void setCourseprice(Long courseprice) {
        this.courseprice = courseprice;
    }

    public Long getCoursetotalitems() {
        return coursetotalitems;
    }

    public void setCoursetotalitems(Long coursetotalitems) {
        this.coursetotalitems = coursetotalitems;
    }

    public HashMap<String, Module> getModules() {
        return modules;
    }

    public void setModules(HashMap<String, Module> modules) {
        this.modules = modules;
    }

    // [START course_to_map]
    @Exclude
    public Map<String, Object> toCourseMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("courseid", courseid);
        result.put("coursename", coursename);
        result.put("coursedescription", coursedescription);
        result.put("coursetotalitems", coursetotalitems);
        result.put("modules",modules);

        return result;
    }
// [END course_to_map]

}

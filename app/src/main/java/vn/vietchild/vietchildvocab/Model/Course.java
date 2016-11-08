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

    private String coursename,coursedescription,courseid;

    private int coursetotalitems;
    private int courseprice = 0;
    private int courselearneditems = 0;
    private Long courseregisterdate = Long.valueOf(0);
    private Long courselastlearn= Long.valueOf(0);;
    private int coursestatus = 0;
    private int coursenotifytime = 20;
    private int courseitemperday = 5;
    private int coursescore = 0;

    HashMap<String,Module> modules;

    public Course() {
    }

    public Course(String coursename, String coursedescription, String courseid, int courseprice, int coursetotalitems, int courselearneditems, int coursescore, Long courseregisterdate, Long courselastlearn, int coursestatus, int coursenotifytime, int courseitemperday, HashMap<String, Module> modules) {
        this.coursename = coursename;
        this.coursedescription = coursedescription;
        this.courseid = courseid;
        this.courseprice = courseprice;
        this.coursetotalitems = coursetotalitems;
        this.courselearneditems = courselearneditems;
        this.coursescore = coursescore;
        this.courseregisterdate = courseregisterdate;
        this.courselastlearn = courselastlearn;
        this.coursestatus = coursestatus;
        this.coursenotifytime = coursenotifytime;
        this.courseitemperday = courseitemperday;
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

    public int getCourseprice() {
        return courseprice;
    }

    public void setCourseprice(int courseprice) {
        this.courseprice = courseprice;
    }

    public int getCoursetotalitems() {
        return coursetotalitems;
    }

    public void setCoursetotalitems(int coursetotalitems) {
        this.coursetotalitems = coursetotalitems;
    }

    public int getCourselearneditems() {
        return courselearneditems;
    }

    public void setCourselearneditems(int courselearneditems) {
        this.courselearneditems = courselearneditems;
    }

    public int getCoursescore() {
        return coursescore;
    }

    public void setCoursescore(int coursescore) {
        this.coursescore = coursescore;
    }

    public Long getCourseregisterdate() {
        return courseregisterdate;
    }

    public void setCourseregisterdate(Long courseregisterdate) {
        this.courseregisterdate = courseregisterdate;
    }

    public Long getCourselastlearn() {
        return courselastlearn;
    }

    public void setCourselastlearn(Long courselastlearn) {
        this.courselastlearn = courselastlearn;
    }

    public int getCoursestatus() {
        return coursestatus;
    }

    public void setCoursestatus(int coursestatus) {
        this.coursestatus = coursestatus;
    }

    public int getCoursenotifytime() {
        return coursenotifytime;
    }

    public void setCoursenotifytime(int coursenotifytime) {
        this.coursenotifytime = coursenotifytime;
    }

    public int getCourseitemperday() {
        return courseitemperday;
    }

    public void setCourseitemperday(int courseitemperday) {
        this.courseitemperday = courseitemperday;
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

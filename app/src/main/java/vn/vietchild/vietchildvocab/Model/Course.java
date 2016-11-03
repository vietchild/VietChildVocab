package vn.vietchild.vietchildvocab.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nguyen Phung Hung on 23/10/16.
 */
@IgnoreExtraProperties
public class Course implements Parcelable {

    public String coursename,courseimage,coursedescription,courseid;
    public Long courseprice,coursetotalitems;
    public Long coursescore = Long.valueOf(0);


    public Course() {
    }

    public Course(String coursename, String courseimage, String coursedescription, String courseid, Long courseprice) {
        this.coursename = coursename;
        this.courseimage = courseimage;
        this.coursedescription = coursedescription;
        this.courseid = courseid;
        this.courseprice = courseprice;
    }

    protected Course(Parcel in) {
        coursename = in.readString();
        courseimage = in.readString();
        coursedescription = in.readString();
        courseid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(coursename);
        dest.writeString(courseimage);
        dest.writeString(coursedescription);
        dest.writeString(courseid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public Long getCourseprice() {
        return courseprice;
    }

    public void setCourseprice(Long courseprice) {
        this.courseprice = courseprice;
    }

    public String getCourseimage() {
        return courseimage;
    }

    public void setCourseimage(String courseimage) {
        this.courseimage = courseimage;
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

    public Long getCoursetotalitems() {
        return coursetotalitems;
    }

    public void setCoursetotalitems(Long coursetotalitems) {
        this.coursetotalitems = coursetotalitems;
    }

    // [START course_to_map]
    @Exclude
    public Map<String, Object> toCourseMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("courseid", courseid);
        result.put("coursename", coursename);
        result.put("coursedescription", coursedescription);
        result.put("coursetotalitems", coursetotalitems);
        result.put("coursescore", coursescore);

        return result;
    }
// [END course_to_map]

}

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

    public String coursename,courseimage,coursedescription;
    public Long courseprice,coursetotalitems;
    public Long coursescore = Long.valueOf(0);


    public Course() {
    }

    public Course(String coursename, String courseimage, String coursedescription, Long courseprice) {
        this.coursename = coursename;
        this.courseimage = courseimage;
        this.coursedescription = coursedescription;
        this.courseprice = courseprice;
    }

    protected Course(Parcel in) {
        coursename = in.readString();
        courseimage = in.readString();
        coursedescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(coursename);
        dest.writeString(courseimage);
        dest.writeString(coursedescription);
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

    public Long getCoursetotalitems() {
        return coursetotalitems;
    }

    public void setCoursetotalitems(Long coursetotalitems) {
        this.coursetotalitems = coursetotalitems;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public Long getCoursescore() {
        return coursescore;
    }

    public void setCoursescore(Long coursescore) {
        this.coursescore = coursescore;
    }

    public Long getCourseprice() {
        return courseprice;
    }

    public void setCourseprice(Long courseprice) {
        this.courseprice = courseprice;
    }

    public String getCoursedescription() {
        return coursedescription;
    }

    public void setCoursedescription(String coursedescription) {
        this.coursedescription = coursedescription;
    }

    public String getCourseimage() {
        return courseimage;
    }

    public void setCourseimage(String courseimage) {
        this.courseimage = courseimage;
    }

    // [START course_to_map]
    @Exclude
    public Map<String, Object> toCourseMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("coursename", coursename);
        result.put("courseimage", courseimage);
        result.put("coursedescription", coursedescription);
        result.put("courseprice", courseprice);
        result.put("coursescore", coursescore);

        return result;
    }
// [END course_to_map]

}

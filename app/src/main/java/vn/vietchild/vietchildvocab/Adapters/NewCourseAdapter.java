package vn.vietchild.vietchildvocab.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import vn.vietchild.vietchildvocab.DownloadManager.VCDownloader;
import vn.vietchild.vietchildvocab.Model.Course;
import vn.vietchild.vietchildvocab.R;

/**
 * Created by Nguyen Phung Hung on 29/10/16.
 */

public class NewCourseAdapter extends RecyclerView.Adapter<NewCourseAdapter.CourseViewHolder>{
    private static final String TAG = "rvNewCourseAdapter";
    private List<Course> mNewCourse;

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCourseName, textViewCoursePrice, textViewCourseDesc, textViewCourseTotalItems ;
        ImageView imageCourse;
        Button btnCourseBuy;

        public CourseViewHolder(View view) {
            super(view);
            textViewCourseName = (TextView)view.findViewById(R.id.textViewCourseName);
            textViewCoursePrice = (TextView)view.findViewById(R.id.textViewCoursePrice);
            textViewCourseDesc = (TextView)view.findViewById(R.id.textViewCourseDesc);
            textViewCourseTotalItems = (TextView)view.findViewById(R.id.textViewCourseTotalItems);
            imageCourse = (ImageView)view.findViewById(R.id.imageViewCourse);
            btnCourseBuy = (Button)view.findViewById(R.id.btnCourseBuy);
        }
    }

    public NewCourseAdapter(Context mContext, List<Course> mNewCourse) {
        this.mContext = mContext;
        this.mNewCourse = mNewCourse;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public CourseViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.layout_all_course,parent,false);
        return new CourseViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(CourseViewHolder holder, final int position) {
        final Course currentItem = mNewCourse.get(position);
        holder.textViewCourseName.setText(currentItem.getCoursename());
        holder.textViewCoursePrice.setText("Price: " + currentItem.getCourseprice() + " coin");
        holder.textViewCourseDesc.setText(currentItem.getCoursedescription());
        holder.textViewCourseTotalItems.setText(currentItem.getCoursetotalitems() + " items");

        String mPath = "file://"+ mContext.getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ currentItem.getCourseid() + ".jpg";
        Picasso.with(mContext).load(mPath)
                .transform(new RoundedCornersTransformation(6,0))
                .fit()
                .into(holder.imageCourse);

        holder.btnCourseBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                writeUserNewCourse(currentItem);
                VCDownloader newDownloader = new VCDownloader(mContext,currentItem.getCourseid().toString());
                newDownloader.downloadCourseImages();
                newDownloader.downloadModulesImages();


            }
        });

    }


    @Override
    public int getItemCount() {
        return mNewCourse.size();
    }

    private void writeUserNewCourse(Course course) {

        DatabaseReference mDatabases = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage mStorages = FirebaseStorage.getInstance();
        FirebaseAuth mAuths = FirebaseAuth.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Ghi vào course vào Users/UserID/Courses/ với giá trị true

        mDatabases.child("Stats").child("Courses").child(course.getCourseid().toString())
                .child("Users").child(userID).setValue(true);
        Map<String, Object> newCourse = course.toCourseMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + userID +"/Courses/" + course.getCourseid().toString(), true);
        childUpdates.put("/Users/" + userID +"/userprogress/Courses/" + course.getCourseid().toString(), newCourse);
       // childUpdates.put("/Users/" + userID +"/userprogress/Courses/" + course.getCourseid().toString() + "/courselearneditems",Long.valueOf(0));
       // childUpdates.put("/Users/" + userID +"/userprogress/Courses/" + course.getCourseid().toString() + "/courseregisterđate", ServerValue.TIMESTAMP);

        mDatabases.updateChildren(childUpdates);
        mDatabases.child("Users").child(userID).child("userprogress").child("Courses").child(course.getCourseid().toString())
                .child("courselearneditems").setValue(Long.valueOf(0));
        mDatabases.child("Users").child(userID).child("userprogress").child("Courses").child(course.getCourseid().toString())
                .child("courseregisterdate").setValue(ServerValue.TIMESTAMP);
    }


}

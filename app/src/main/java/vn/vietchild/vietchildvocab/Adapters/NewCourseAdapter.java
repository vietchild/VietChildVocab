package vn.vietchild.vietchildvocab.Adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import vn.vietchild.vietchildvocab.Model.Course;
import vn.vietchild.vietchildvocab.NavigationActivity;
import vn.vietchild.vietchildvocab.R;
import vn.vietchild.vietchildvocab.SQLite.DatabaseHelper;

/**
 * Created by Nguyen Phung Hung on 29/10/16.
 */

public class NewCourseAdapter extends RecyclerView.Adapter<NewCourseAdapter.CourseViewHolder>{
    private static final String TAG = "rvNewCourseAdapter";
    private List<Course> mNewCourse;

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


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
            btnCourseBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
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

        /*holder.btnCourseBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





            }
        }); */

    }


    @Override
    public int getItemCount() {
        return mNewCourse.size();
    }



}

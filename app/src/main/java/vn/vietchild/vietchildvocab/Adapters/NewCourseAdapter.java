package vn.vietchild.vietchildvocab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import vn.vietchild.vietchildvocab.Model.Course;
import vn.vietchild.vietchildvocab.R;

/**
 * Created by Nguyen Phung Hung on 24/10/16.
 */

public class NewCourseAdapter extends BaseAdapter {
    Context mcontext;
    List<Course> arrayCourses;
    LayoutInflater layoutInflater;

    public NewCourseAdapter(Context mcontext, List<Course> arrayCourses) {
        this.mcontext = mcontext;
        this.arrayCourses =  arrayCourses;
        layoutInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return arrayCourses.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayCourses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textViewCourseName, textViewCoursePrice, textViewCourseDesc, textViewCourseTotalItems ;
        ImageView imageCourse;
        Button btnCourseBuy;
        public ViewHolder(View view) {
            textViewCourseName = (TextView)view.findViewById(R.id.textViewCourseName);
            textViewCoursePrice = (TextView)view.findViewById(R.id.textViewCoursePrice);
            textViewCourseDesc = (TextView)view.findViewById(R.id.textViewCourseDesc);
            textViewCourseTotalItems = (TextView)view.findViewById(R.id.textViewCourseTotalItems);
            imageCourse = (ImageView)view.findViewById(R.id.imageViewCourse);
            btnCourseBuy = (Button)view.findViewById(R.id.btnCourse);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_all_course,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Course currentItem = (Course) getItem(position);
        viewHolder.textViewCourseName.setText(currentItem.getCoursename());
        viewHolder.textViewCoursePrice.setText("Price: " + currentItem.getCourseprice() + "coin");
        viewHolder.textViewCourseDesc.setText(currentItem.getCoursedescription());
        viewHolder.textViewCourseTotalItems.setText(currentItem.getCoursetotalitems() + " items");
        Picasso.with(mcontext).load(currentItem.getCourseimage().toString()).transform(new RoundedCornersTransformation(15,0)).into(viewHolder.imageCourse);
        return convertView;

    }


}

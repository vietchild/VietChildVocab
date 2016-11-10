package vn.vietchild.vietchildvocab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import vn.vietchild.vietchildvocab.Model.Scores;
import vn.vietchild.vietchildvocab.R;


/**
 * Created by Nguyen Phung Hung on 01/10/16.
 */



public class CustomAdapter extends BaseAdapter {
    Context mcontext;
    List<Scores> arrayWords;
    LayoutInflater layoutInflater;
    private DatabaseReference mDatabases;
    private FirebaseStorage mStorages;
    private FirebaseAuth mAuths;

    public CustomAdapter(Context mcontext, List<Scores> arrayWords) {
        this.mcontext = mcontext;
        this.arrayWords =  arrayWords;
        layoutInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return arrayWords.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayWords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textViewTopic;
        ImageView imageTopic;
        RatingBar ratingBarTopic;

        public ViewHolder(View view) {
            textViewTopic = (TextView)view.findViewById(R.id.textViewTopic);
            imageTopic = (ImageView) view.findViewById(R.id.imageViewTopic);
            ratingBarTopic = (RatingBar) view.findViewById(R.id.ratingBarTopic);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_all_topics,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Scores currentItem = (Scores)getItem(position);
        viewHolder.textViewTopic.setText(currentItem.getTopicname());
        viewHolder.ratingBarTopic.setRating(currentItem.getTopicscore());
        String mPath = "file://"+ mcontext.getFilesDir().getAbsolutePath()+"/"+currentItem.getTopicalias()+ ".jpg";
        Picasso.with(mcontext).load(mPath).transform(new RoundedCornersTransformation(15,0)).into(viewHolder.imageTopic);
        return convertView;
    }


}

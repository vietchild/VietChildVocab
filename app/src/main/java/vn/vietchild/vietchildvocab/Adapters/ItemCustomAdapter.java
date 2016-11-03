package vn.vietchild.vietchildvocab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import vn.vietchild.vietchildvocab.Model.ItemAsked;
import vn.vietchild.vietchildvocab.R;

/**
 * Created by Nguyen Phung Hung on 07/10/16.
 */

public class ItemCustomAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<ItemAsked> arrayItems;
    private LayoutInflater layoutInflater;

    public ItemCustomAdapter(Context mcontext, ArrayList<ItemAsked> arrayItems) {
        this.mcontext = mcontext;
        this.arrayItems = arrayItems;
        layoutInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return arrayItems.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageItem,imageItemTrue,imageItemFalse;

        private ViewHolder(View view) {

            imageItem = (ImageView) view.findViewById(R.id.imageViewItem);
            imageItemTrue = (ImageView) view.findViewById(R.id.imageViewItemTrue);
            imageItemFalse = (ImageView) view.findViewById(R.id.imageViewItemFalse);


        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemCustomAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_items_test, parent,false);
            viewHolder = new ItemCustomAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemCustomAdapter.ViewHolder) convertView.getTag();
        }
        ItemAsked currentItem = (ItemAsked) getItem(position);
        String mPath = "file://"+ mcontext.getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ currentItem.getItemalias() + ".jpg";
        Picasso.with(mcontext).load(mPath).transform(new RoundedCornersTransformation(15,0)).into(viewHolder.imageItem);
        viewHolder.imageItemTrue.setImageResource(R.drawable.itemtrue);
        viewHolder.imageItemFalse.setImageResource(R.drawable.itemfalse);
        viewHolder.imageItemTrue.setVisibility(View.INVISIBLE);
        viewHolder.imageItemFalse.setVisibility(View.INVISIBLE);
       // Picasso.with(mcontext).load(R.drawable.itemtrue).into(viewHolder.imageItemTrue);
        return convertView;
    }

}

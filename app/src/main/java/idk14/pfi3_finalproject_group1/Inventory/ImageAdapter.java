package idk14.pfi3_finalproject_group1.Inventory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import idk14.pfi3_finalproject_group1.R;

/**
 * Created by larsy09 on 6/1/15.
 */
public class ImageAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> inventoryList;



    public ImageAdapter(Context c, ArrayList<String> inventory) {
        super(c, 0, inventory);
        inventoryList = inventory;
        mContext = c;

    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i("MyListAdapter", "Called for position: " + position);

        String treasure = inventoryList.get(position);

        convertView = inflater.inflate(R.layout.grid_item,null);

        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_treasurePic);

        if(treasure == "1"){
            iv.setImageResource(R.drawable.grid_water);
        }
        if(treasure == "2"){
            iv.setImageResource(R.drawable.grid_air);
        }
        if(treasure == "3"){
            iv.setImageResource(R.drawable.grid_sun);
        }
        if(treasure == "0") {
        }

        return convertView;
    }
}
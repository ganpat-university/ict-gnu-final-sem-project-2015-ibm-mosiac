package com.example.vaibh.mosaic;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by vaibh on 1/31/2019.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView name;
    public TextView desc;
    public LinearLayout linearLayout;



    public ViewHolder(View itemView) {

        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.imageview_id);
        name = (TextView) itemView.findViewById(R.id.name_textview);
        desc = (TextView) itemView.findViewById(R.id.desc_textview);
//        linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_list);


    }
}

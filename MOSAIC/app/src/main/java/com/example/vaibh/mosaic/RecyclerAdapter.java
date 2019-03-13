package com.example.vaibh.mosaic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder1> {

    private List<RestaurantName> users;
    private Context context;

    public RecyclerAdapter(Context context, List<RestaurantName> users) {
        this.users = users;
        this.context = context;
    }

    @Override
    public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list,parent,false);
        return new ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder1 holder, final int position) {

        final RestaurantName sampleuser = users.get(position);

        holder.name.setText(sampleuser.getRestname());
        holder.desc.setText(sampleuser.getAddress());
        holder.owner.setText(sampleuser.getOwner());
        holder.number.setText(sampleuser.getNumber());
        holder.distance.setText(String.valueOf(sampleuser.getDistance()));
        holder.image.setImageResource(sampleuser.getUserimage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(context,"item click : " + (position + 1) ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,DisplayRestaurants.class);
                intent.putExtra("RESTAURANT",(Serializable) sampleuser);
                context.startActivity(intent);

            }
        });
//        /holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText()
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    class ViewHolder1 extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public TextView desc;
        public TextView owner;
        public TextView number;
        public TextView distance;



        public ViewHolder1(View itemView) {

            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageview_id);
            name = (TextView) itemView.findViewById(R.id.name_textview);
            desc = (TextView) itemView.findViewById(R.id.desc_textview);
            owner = (TextView) itemView.findViewById(R.id.owner_textview);
            number = (TextView) itemView.findViewById(R.id.mobile_textview);
            distance = (TextView) itemView.findViewById(R.id.dist_textview);
//        linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_list);


        }
    }

}

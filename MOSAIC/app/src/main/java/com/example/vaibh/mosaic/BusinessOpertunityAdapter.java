package com.example.vaibh.mosaic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;


public class BusinessOpertunityAdapter extends RecyclerView.Adapter<BusinessOpertunityAdapter.ViewHolder1> {

    private List<BussinessOpportunity> users;
    private DisplayRestaurants context;
    private RestaurantName restaurantName;
 //   private UserSession userSession;

    public BusinessOpertunityAdapter(DisplayRestaurants context, List<BussinessOpportunity> users,RestaurantName restaurantName) {
        this.users = users;
        this.context = context;
        this.restaurantName=restaurantName;
    }


    @Override
    public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_business_item_cell,parent,false);
        return new ViewHolder1(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder1 holder, final int position) {

        final BussinessOpportunity sampleuser = users.get(position);

        holder.requirement.setText(sampleuser.getBusinessRequirement());
        holder.desc.setText(sampleuser.getDescription());

        holder.btnMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.finish();
                Intent intent = new Intent(context,MeetingActivity.class);
                intent.putExtra("restaurant_id",restaurantName.getNumid());
                intent.putExtra("business_opportunity_id",sampleuser.getBusinessOpportunityId());
              //  intent.putExtra("employee_id", userSession.getEmpId());


                context.startActivity(intent);
            }
        });
        holder.btnViewMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.finish();

                Intent intent = new Intent(context,ViewMeeting.class);
                intent.putExtra("restaurant_id",restaurantName.getNumid());
                intent.putExtra("business_opportunity_id",sampleuser.getBusinessOpportunityId());

                context.startActivity(intent);


            }
        });
    }


    @Override
    public int getItemCount() {
        return users.size();
    }
    class ViewHolder1 extends RecyclerView.ViewHolder {

        public TextView requirement;
        public TextView desc;
        public Button btnMeeting;
        public Button btnViewMeeting;



        public ViewHolder1(View itemView) {

            super(itemView);

            requirement = (TextView) itemView.findViewById(R.id.txtnew);
            desc = (TextView) itemView.findViewById(R.id.txtnew1);
            btnMeeting = (Button) itemView.findViewById(R.id.btnAddRecord);
            btnViewMeeting = (Button) itemView.findViewById(R.id.btnViewMeeting);



        }

    }

}

package com.example.vaibh.mosaic;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ViewMeetingAdapter extends RecyclerView.Adapter<ViewMeetingAdapter.ViewHolder1> {

    private List<ViewMt> users;
    private Context context;

    public ViewMeetingAdapter(Context context, List<ViewMt> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public ViewMeetingAdapter.ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_meeting_cell, parent, false);
        return new ViewMeetingAdapter.ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(ViewMeetingAdapter.ViewHolder1 holder, final int position) {

        final ViewMt sampleuser = users.get(position);

        holder.emp_textview.setText(String.valueOf(sampleuser.getMeetingEmpId()));
        holder.contact_textview.setText(sampleuser.getContactedPerson());
        holder.meetingDate_textview.setText(sampleuser.getMeetingDate());
        holder.meetingDesc_textview.setText(sampleuser.getMeetingDescription());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {

        public TextView emp_textview;
        public TextView contact_textview;
        public TextView meetingDate_textview;
        public TextView meetingDesc_textview;




        public ViewHolder1(View itemView) {

            super(itemView);

            emp_textview = (TextView) itemView.findViewById(R.id.emp_textview);
            contact_textview = (TextView) itemView.findViewById(R.id.contact_textview);
            meetingDate_textview = (TextView) itemView.findViewById(R.id.meetingDate_textview);
            meetingDesc_textview = (TextView) itemView.findViewById(R.id.meetingDesc_textview);



        }

    }
}
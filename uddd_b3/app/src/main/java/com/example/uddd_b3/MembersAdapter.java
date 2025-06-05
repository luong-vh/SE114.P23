package com.example.uddd_b3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {
    private Context context;
    private List<String> memberList;
    private List<String> memberPhone;
    public MembersAdapter(Context context, List<String> names, List<String> phones){
        this.context = context;
        this.memberList = names;
        this.memberPhone = phones;
    }
    public static class MembersViewHolder extends  RecyclerView.ViewHolder
    {
        TextView name;
        TextView phone;
        public MembersViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.memberName);
            phone = view.findViewById(R.id.memberPhone);
        }
    }
    @Override
    public MembersAdapter.MembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.members_list_view,parent,false);
        return  new MembersViewHolder(itemView);
    }
    @Override
    public void  onBindViewHolder(MembersAdapter.MembersViewHolder holder, int position){
        holder.name.setText(memberList.get(position));
        holder.phone.setText(memberPhone.get(position));
    }
    @Override
    public int getItemCount(){
        return memberList.size();
    }
}

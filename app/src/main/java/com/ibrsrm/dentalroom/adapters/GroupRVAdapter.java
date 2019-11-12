package com.ibrsrm.dentalroom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;

import java.util.List;

public class GroupRVAdapter extends RecyclerView.Adapter<GroupRVAdapter.ViewHolder> {

    private Context mContext;
    private List<Group> mGroups;
    private ItemClickListener clickListener;

    public GroupRVAdapter(Context context, List<Group> groups) {
        this.mGroups = groups;
        this.mContext = context;
    }

    @NonNull
    @Override
    public GroupRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_groups, parent, false);
        final GroupRVAdapter.ViewHolder holder = new GroupRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupRVAdapter.ViewHolder holder, int position) {
        Group group = mGroups.get(position);
        holder.mGroupName.setText(group.getName());
        if (group.getNewMessageStatus()) {
            holder.mNewMessage.setVisibility(View.VISIBLE);
        }
        if (group.getUrl() != null) {
            /* TODO: find a better way */
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(group.getUrl());
            GlideApp.with(mContext)
                    .load(ref)
                    .circleCrop()
                    .into(holder.mGroupImage);
        }
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mGroupName;
        public ImageView mNewMessage;
        public ImageView mGroupImage;


        public ViewHolder(View itemView) {
            super(itemView);
            mGroupName = itemView.findViewById(R.id.textViewGroupName);
            mNewMessage = itemView.findViewById(R.id.newMassageAllert);
            mGroupImage = itemView.findViewById(R.id.ImageViewGroupImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }

    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

}

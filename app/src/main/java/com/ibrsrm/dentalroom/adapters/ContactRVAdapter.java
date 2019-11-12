package com.ibrsrm.dentalroom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.model.Repository.Common.Contact;

import java.util.HashSet;
import java.util.List;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ViewHolder> {

    private Context mContext;
    private boolean mIsSelectable;
    private List<Contact> mContacts;
    private HashSet<Contact> mSet;

    public ContactRVAdapter(Context context, List<Contact> contacts, boolean isSelectable) {
        this.mContacts = contacts;
        this.mContext = context;
        this.mIsSelectable = isSelectable;
        if (isSelectable) {
            mSet = new HashSet<>();
        }
    }

    public @Nullable HashSet<Contact> getContactSet() {
        return mSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contacts, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.mNick.setText(contact.getNick());
        holder.mEmail.setText(contact.getMail());

        if (mIsSelectable) {
            holder.mAddCheckBox.setVisibility(View.VISIBLE);
            holder.mAddCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mSet.add(mContacts.get(holder.getAdapterPosition()));
                    } else {
                        mSet.remove(mContacts.get(holder.getAdapterPosition()));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNick;
        public TextView mEmail;
        public CheckBox mAddCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mNick = itemView.findViewById(R.id.textViewNick);
            mEmail = itemView.findViewById(R.id.textViewMail);
            mAddCheckBox = itemView.findViewById(R.id.userAddCheckBox);
        }

    }

}

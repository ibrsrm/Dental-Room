package com.ibrsrm.dentalroom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.model.Auth.BaseAuthentication;
import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Repository.BaseRepository;
import com.ibrsrm.dentalroom.model.Repository.Common.Contact;
import com.ibrsrm.dentalroom.model.Repository.Common.Message;

import java.util.List;

public class MessageRVAdapter extends RecyclerView.Adapter<MessageRVAdapter.ViewHolder> {

    private Context mContext;
    private List<Message> mMessages;
    private BaseRepository mRepository;
    private BaseAuthentication mAuthentication;

    public MessageRVAdapter(Context context, List<Message> messages) {
        this.mMessages = messages;
        this.mContext = context;
        mRepository = ConfigManager.getInstance().getRepositoryManager();
        mAuthentication = ConfigManager.getInstance().getAuthenticationManager();
    }

    @NonNull
    @Override
    public MessageRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messages, parent, false);
        final MessageRVAdapter.ViewHolder holder = new MessageRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageRVAdapter.ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.mMessageText.setText(message.getText());
        holder.mMessageRanking.setText(String.valueOf(message.getRating()));

        if (message.getSender().equals(mAuthentication.getCurrentUserEMail())) {
            holder.mMessageSender.setText("myself");
        } else {
            Contact contact = mRepository.getContact(message.getSender());
            if (contact == null) {
                holder.mMessageSender.setText(message.getSender());
            } else {
                holder.mMessageSender.setText(contact.getNick());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mMessageText;
        public TextView mMessageSender;
        public TextView mMessageRanking;

        public ViewHolder(View itemView) {
            super(itemView);
            mMessageText = itemView.findViewById(R.id.textViewMessage);
            mMessageSender = itemView.findViewById(R.id.textViewSender);
            mMessageRanking = itemView.findViewById(R.id.textViewRanking);
        }

    }

}


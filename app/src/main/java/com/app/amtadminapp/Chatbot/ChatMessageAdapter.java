package com.app.amtadminapp.Chatbot;





import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.amtadminapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder>
{
    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public ChatMessageAdapter(List<Messages> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView receiver_profile_image , sender_profile_image , receiver_file, sender_file;
        public TextView receiver_message_text , receiver_date_time , sender_message_text , sender_date_time;
        public ImageView receiver_image , sender_image;
        public LinearLayout LLReceiver , LLSender , receiver_pdf , sender_pdf;
        public TextView date;

        public MessageViewHolder(@NotNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            // Receiver
            LLReceiver = itemView.findViewById(R.id.LLReceiver);
            receiver_profile_image = itemView.findViewById(R.id.receiver_profile_image);
            receiver_message_text = itemView.findViewById(R.id.receiver_message_text);
            receiver_image = itemView.findViewById(R.id.receiver_image);
            receiver_pdf = itemView.findViewById(R.id.receiver_pdf);
            receiver_date_time = itemView.findViewById(R.id.receiver_date_time);
            receiver_file = itemView.findViewById(R.id.receiver_file);

            // Sender
            LLSender = itemView.findViewById(R.id.LLSender);
            sender_profile_image = itemView.findViewById(R.id.sender_profile_image);
            sender_message_text = itemView.findViewById(R.id.sender_message_text);
            sender_image = itemView.findViewById(R.id.sender_image);
            sender_pdf = itemView.findViewById(R.id.sender_pdf);
            sender_date_time = itemView.findViewById(R.id.sender_date_time);
            sender_file = itemView.findViewById(R.id.sender_file);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_custom_msg, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull MessageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();

        usersRef = FirebaseDatabase.getInstance().getReference().child(ChatConstant.F_CUSTOMER).child(fromUserID);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("message")) {
                    String receiverImage = snapshot.child("message").getValue().toString();
                    Picasso.get().load(receiverImage).placeholder(R.drawable.ic_profile).into(holder.receiver_profile_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        usersRef = FirebaseDatabase.getInstance().getReference().child(ChatConstant.F_EMPLOYEE).child(messageSenderId);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("message")) {
                    String senderImage = snapshot.child("message").getValue().toString();
                    Picasso.get().load(senderImage).placeholder(R.drawable.ic_profile).into(holder.sender_profile_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.LLReceiver.setVisibility(View.GONE);
        holder.receiver_message_text.setVisibility(View.GONE);
        holder.receiver_image.setVisibility(View.GONE);
        holder.receiver_pdf.setVisibility(View.GONE);
        holder.LLSender.setVisibility(View.GONE);
        holder.sender_message_text.setVisibility(View.GONE);
        holder.sender_image.setVisibility(View.GONE);
        holder.sender_pdf.setVisibility(View.GONE);

        holder.date.setText(messages.getDate());

        if(position > 0) {
            if(userMessagesList.get(position-1).getDate().equals(userMessagesList.get(position).getDate())) {
                holder.date.setVisibility(View.GONE);
            } else {
                holder.date.setVisibility(View.VISIBLE);
            }
        } else if(position == 0) {
            holder.date.setVisibility(View.VISIBLE);
        }

        if(fromMessageType.equals("text"))
        {
            if(fromUserID.equals(messageSenderId)) {
                holder.LLSender.setVisibility(View.VISIBLE);
                holder.sender_message_text.setVisibility(View.VISIBLE);
                holder.sender_message_text.setText(messages.getMessage());
                holder.sender_date_time.setText(messages.getTime());

            } else {
                holder.LLReceiver.setVisibility(View.VISIBLE);
                holder.receiver_message_text.setVisibility(View.VISIBLE);
                holder.receiver_message_text.setText(messages.getMessage());
                holder.receiver_date_time.setText(messages.getTime());
            }
        }
        else if (fromMessageType.equals("image"))
        {
            if(fromUserID.equals(messageSenderId)) {
                holder.LLSender.setVisibility(View.VISIBLE);
                holder.sender_image.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).resize(150,120).into(holder.sender_image);
                holder.sender_date_time.setText(messages.getTime());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            } else {
                holder.LLReceiver.setVisibility(View.VISIBLE);
                holder.receiver_image.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).resize(150,120).into(holder.receiver_image);
                holder.receiver_date_time.setText(messages.getTime());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }
        }
        else if (fromMessageType.equals("pdf") || fromMessageType.equals("docx") )
        {
            if(fromUserID.equals(messageSenderId)) {
                holder.LLSender.setVisibility(View.VISIBLE);
                holder.sender_pdf.setVisibility(View.VISIBLE);
                if(fromMessageType.equals("pdf")) {
                    holder.sender_file.setImageResource(R.drawable.pdf_icon);
                } else {
                    holder.sender_file.setImageResource(R.drawable.file);
                }
                holder.sender_date_time.setText(messages.getTime());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            } else {
                holder.LLReceiver.setVisibility(View.VISIBLE);
                holder.receiver_pdf.setVisibility(View.VISIBLE);
                if(fromMessageType.equals("pdf")) {
                    holder.receiver_file.setImageResource(R.drawable.pdf_icon);
                } else {
                    holder.receiver_file.setImageResource(R.drawable.file);
                }
                holder.receiver_date_time.setText(messages.getTime());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

}

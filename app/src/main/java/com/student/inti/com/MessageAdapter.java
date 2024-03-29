package com.student.inti.com;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


//get data from firebase
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    //List hold messages from model class
    private List<Messages> mMessageList;

    //List, help to add view to it
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;


    public MessageAdapter(List<Messages> mMessageList) {

        //add view to it
        this.mMessageList = mMessageList;

    }
    //Below code is for get Data

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = view.findViewById(R.id.circleImageView);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        //mAuth = FirebaseAuth.getInstance();
        //String current_userId = mAuth.getCurrentUser().getUid();
        Messages c = mMessageList.get(i);
        //viewHolder.messageText.setText(c.getMessage());

        //get id form  message's model class
        String from_user = c.getFrom();
        String message_type = c.getType();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();


                viewHolder.displayName.setText(name);

                Picasso.get().load(image).placeholder(R.mipmap.ic_launcher_foreground).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")) {

            //see get message. If message type is text type. will show the text and hide the image view
            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);


        } else {//else the message type is image, it will show image

            viewHolder.messageText.setVisibility(View.INVISIBLE);

            Picasso.get().load(c.getMessage())
                    .placeholder(R.mipmap.ic_launcher_foreground).into(viewHolder.messageImage);

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }




}





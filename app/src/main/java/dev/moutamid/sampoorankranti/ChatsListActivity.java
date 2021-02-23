package dev.moutamid.sampoorankranti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsListActivity extends AppCompatActivity {
    private static final String TAG = "ChatsListActivity";

//    private ArrayList<String> currentRequestsArrayList = new ArrayList<>();

    private ArrayList<String> chatsName = new ArrayList<>();
    private ArrayList<String> userChats = new ArrayList<>();
    private ArrayList<String> chatsList = new ArrayList<>();

    private ArrayList<USER> users = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        startActivity(new Intent(ChatsListActivity.this, FilterChatActivity.class));

    }

    private void initList() {
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: executed");

                chatsName.clear();
                userChats.clear();
                chatsList.clear();
                users.clear();

                for (DataSnapshot snapshot : dataSnapshot.child("chats").getChildren()) {

                    String chat = snapshot.getKey();
                    chatsName.add(chat);
                    Log.d(TAG, "onDataChange: datasnapshot child added " + chat);

                }

                for (int position = 0; position <= chatsName.size() - 1; position++) {
                    Log.d(TAG, "onDataChange: loop executed");

                    if (chatsName.get(position).contains(auth.getCurrentUser().getUid())) {

                        userChats.add(chatsName.get(position));
                        Log.d(TAG, "onDataChange: matching name added in userChats " + chatsName.get(position));

                    }

                }

                for (int position = 0; position <= userChats.size() - 1; position++) {

                    String myString = userChats.get(position);
                    String[] splitString = myString.split("_");

                    Log.i(TAG, "onDataChange: "+ Arrays.toString(splitString));

                    String firstName = splitString[0];
                    String secondName = splitString[1];

                    if (firstName.contains(auth.getCurrentUser().getUid()))
                        chatsList.add(secondName);

                    if (secondName.contains(auth.getCurrentUser().getUid()))
                        chatsList.add(firstName);

                }

                for (int position = 0; position <= chatsList.size() - 1; position++) {

                    if (dataSnapshot.child("users").hasChild(chatsList.get(position))) {

                        String chatName = chatsList.get(position);

                        String profileImg = dataSnapshot.child("users").child(chatsList.get(position))
                                .child("profileUrl").getValue(String.class);

                        String name = dataSnapshot.child("users").child(chatsList.get(position))
                                .child("name").getValue(String.class);

                        users.add(new USER(chatName, name, profileImg));

                    }
                }

                initRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.requestbtnchatslist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatsListActivity.this, FilterChatActivity.class));
            }
        });
    }

    private static class USER {
        private String chatName, name, profileUrl;

        public String getChatName() {
            return chatName;
        }

        public void setChatName(String chatName) {
            this.chatName = chatName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public USER(String chatName, String name, String profileUrl) {
            this.chatName = chatName;
            this.name = name;
            this.profileUrl = profileUrl;
        }

        USER() {
        }

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");

        conversationRecyclerView = findViewById(R.id.recyclerviewchatslist);
        adapter = new RecyclerViewAdapterMessages();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);

        conversationRecyclerView.setLayoutManager(linearLayoutManager);
//        conversationRecyclerView.setHasFixedSize(true);
//        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

        scrollRecyclerViewToEnd();

        if (adapter.getItemCount() == 0) {

            findViewById(R.id.nochatchatslist).setVisibility(View.VISIBLE);

        }
    }

    private void scrollRecyclerViewToEnd() {
        Log.d(TAG, "scrollRecyclerViewToEnd: ");
        conversationRecyclerView.scrollToPosition(conversationRecyclerView.getAdapter().getItemCount() - 1);

    }

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public RecyclerViewAdapterMessages.ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chats, parent, false);
            return new RecyclerViewAdapterMessages.ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewAdapterMessages.ViewHolderRightMessage holder, int position) {

            holder.rightText.setText(users.get(holder.getAdapterPosition()).getName());

            Picasso.with(ChatsListActivity.this)
                    .load(users.get(holder.getAdapterPosition()).getProfileUrl())
                    .placeholder(R.color.lighterGrey)
                    .into(holder.profileImageview);

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ChatsListActivity.this, ConversationActivity.class);
                    intent.putExtra("chatName", userChats.get(holder.getAdapterPosition()));
                    intent.putExtra("userName", users.get(holder.getAdapterPosition()).getName());
                    intent.putExtra("userImage", users.get(holder.getAdapterPosition()).getProfileUrl());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            if (users == null)
                return 0;
            return users.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            TextView rightText;
            CircleImageView profileImageview;
            RelativeLayout parentLayout;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                rightText = v.findViewById(R.id.nametextviewchats);
                profileImageview = v.findViewById(R.id.profileImagelayoutchats);
                parentLayout = v.findViewById(R.id.parentlayoutlayoutchats);
            }
        }

    }


}
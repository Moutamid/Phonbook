package dev.moutamid.sampoorankranti;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListActivity extends AppCompatActivity {

    private ArrayList<UserCompleteDetailsModel> userDetailsList;

    private static final String TAG = "NotoficationActivity";
    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private FirebaseAuth mAuth;

    private static class UserCompleteDetailsModel {
        private String uid, name, email, profileUrl, mNo, district, village, ward, category;

        public UserCompleteDetailsModel(String uid, String name, String email, String profileUrl, String mNo, String district, String village, String ward, String category) {
            this.uid = uid;
            this.name = name;
            this.email = email;
            this.profileUrl = profileUrl;
            this.mNo = mNo;
            this.district = district;
            this.village = village;
            this.ward = ward;
            this.category = category;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getmNo() {
            return mNo;
        }

        public void setmNo(String mNo) {
            this.mNo = mNo;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getVillage() {
            return village;
        }

        public void setVillage(String village) {
            this.village = village;
        }

        public String getWard() {
            return ward;
        }

        public void setWard(String ward) {
            this.ward = ward;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        UserCompleteDetailsModel() {
        }

    }

    private static class RequestModel {
        private String uid, name, profileUrl;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public RequestModel(String uid, String name, String profileUrl) {
            this.uid = uid;
            this.name = name;
            this.profileUrl = profileUrl;
        }

        RequestModel() {
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        mAuth = FirebaseAuth.getInstance();

        // GETTING DATA FROM FILTERCHAT ACTIVITY
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        userDetailsList = (ArrayList<UserCompleteDetailsModel>) args.getSerializable("USERSLIST");

        initRecyclerView();

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");

        conversationRecyclerView = findViewById(R.id.recyclerviewuserlist);
        adapter = new RecyclerViewAdapterMessages();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);

        conversationRecyclerView.setLayoutManager(linearLayoutManager);
//        conversationRecyclerView.setHasFixedSize(true);
//        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

        scrollRecyclerViewToEnd();

    }

    private void scrollRecyclerViewToEnd() {
        Log.d(TAG, "scrollRecyclerViewToEnd: ");
        conversationRecyclerView.scrollToPosition(conversationRecyclerView.getAdapter().getItemCount() - 1);

    }

//    private void fetchChats() {
//
//        private ArrayList<String> chatsName = new ArrayList<>();
//        private ArrayList<String> userChats = new ArrayList<>();
//        private ArrayList<String> chatsList = new ArrayList<>();
//
//        private ArrayList<USERS> users = new ArrayList<>();
//
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: executed");
//
//                chatsName.clear();
//                userChats.clear();
//                chatsList.clear();
//                users.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.child(CHATS).getChildren()) {
//
//                    String chat = snapshot.getKey();
//                    chatsName.add(chat);
//                    Log.d(TAG, "onDataChange: datasnapshot child added " + chat);
//
//                }
//
//                for (int position = 0; position <= chatsName.size() - 1; position++) {
//                    Log.d(TAG, "onDataChange: loop executed");
//
//                    if (chatsName.get(position).toLowerCase().contains(storedName.toLowerCase())) {
//
//                        userChats.add(chatsName.get(position));
//                        Log.d(TAG, "onDataChange: matching name added in userChats " + chatsName.get(position));
//
//                    }
//
//                }
//
//                for (int position = 0; position <= userChats.size() - 1; position++) {
//
//                    String myString = userChats.get(position);
//                    String[] splitString = myString.split("_");
//
//                    String firstName = splitString[0];
//                    String secondName = splitString[1];
//
//                    if (firstName.contains(storedName))
//                        chatsList.add(secondName);
//
//                    if (secondName.contains(storedName))
//                        chatsList.add(firstName);
//
//                }
//
//                for (int position = 0; position <= chatsList.size() - 1; position++) {
//
//                    if (dataSnapshot.child(USERS).hasChild(chatsList.get(position))) {
//
//                        String name = chatsList.get(position);
//
//                        String profileImg = dataSnapshot.child(USERS).child(chatsList.get(position)).child(PUBLIC)
//                                .child(PROFILE_IMAGE).getValue(String.class);
//
//                        String onlineStatus = dataSnapshot.child(USERS).child(chatsList.get(position)).child(PUBLIC)
//                                .child(ONLINE_STATUS).getValue(String.class);
//
//                        String lastMessage = dataSnapshot.child(CHATS).child(CHAT_STATUS)
//                                .child(userChats.get(position) + "_" + STATUS)
//                                .child(LAST_MESSAGE).getValue(String.class);
//
//                        String lastMessageTime = dataSnapshot.child(CHATS).child(CHAT_STATUS)
//                                .child(userChats.get(position) + "_" + STATUS)
//                                .child(LAST_MESSAGE_TIME).getValue(String.class);
//
//                        users.add(new USERS(name, profileImg, onlineStatus, lastMessage, lastMessageTime));
//
//                    }
//                }
//
//                initChatsRecyclerView();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public RecyclerViewAdapterMessages.ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_request, parent, false);
            return new RecyclerViewAdapterMessages.ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewAdapterMessages.ViewHolderRightMessage holder, int position) {

            holder.rightText.setText(userDetailsList.get(holder.getAdapterPosition()).getName());

            Picasso.with(UsersListActivity.this)
                    .load(userDetailsList.get(holder.getAdapterPosition()).getProfileUrl())
                    .into(holder.profileImageview);
            holder.requestBtn.setText("REQUEST");

            holder.requestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ProgressDialog progressDialog = new ProgressDialog(UsersListActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    final SharedPreferences sharedPreferences = getSharedPreferences("dev.moutamid.sampoorankranti", Context.MODE_PRIVATE);

                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("requests")
                            .child(userDetailsList.get(holder.getAdapterPosition()).getUid())
                            .push()
                            .setValue(new RequestModel(
                                    mAuth.getCurrentUser().getUid(),
                                    mAuth.getCurrentUser().getDisplayName(),
                                    sharedPreferences.getString("myprofilelink", "error")
                            )).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                progressDialog.dismiss();

                                Toast.makeText(UsersListActivity.this, "Request sent!", Toast.LENGTH_SHORT).show();
                                holder.requestBtn.setVisibility(View.GONE);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(UsersListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });


        }

        @Override
        public int getItemCount() {
            if (userDetailsList == null)
                return 0;
            return userDetailsList.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            TextView rightText;
            CircleImageView profileImageview;
            Button requestBtn;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                rightText = v.findViewById(R.id.nametextviewnotification);
                profileImageview = v.findViewById(R.id.profileImagelayoutnotificationrequest);
                requestBtn = v.findViewById(R.id.acceptrequestbtnlayoutnotificationrequest);
            }
        }

    }

}
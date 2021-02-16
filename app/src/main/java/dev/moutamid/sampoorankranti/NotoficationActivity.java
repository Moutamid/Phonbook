package dev.moutamid.sampoorankranti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotoficationActivity extends AppCompatActivity {

    private static final String TAG = "NotoficationActivity";
    private ArrayList<RequestModel> currentRequestsArrayList = new ArrayList<>();
    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private FirebaseAuth auth;

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
        setContentView(R.layout.activity_notofication);
        Log.d(TAG, "onCreate: ");

        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("requests").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currentRequestsArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    currentRequestsArrayList.add(dataSnapshot.getValue(RequestModel.class));

                }

                initRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");

        conversationRecyclerView = findViewById(R.id.notificationrecyclerview);
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

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_request, parent, false);
            return new ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {

            holder.rightText.setText(currentRequestsArrayList.get(holder.getAdapterPosition()).getName());

            Picasso.with(NotoficationActivity.this)
                    .load(currentRequestsArrayList.get(holder.getAdapterPosition()).getProfileUrl())
                    .into(holder.profileImageview);

            holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ProgressDialog progressDialog = new ProgressDialog(NotoficationActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("chats")
                            .child(auth.getCurrentUser().getUid() + "_" + currentRequestsArrayList
                                    .get(holder.getAdapterPosition()).getUid())
                            .push()
                            .setValue(new ChatMessage("Hi", auth.getCurrentUser().getUid()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(NotoficationActivity.this, ConversationActivity.class);
                                    intent.putExtra("chatName", auth.getCurrentUser().getUid() + "_" + currentRequestsArrayList.get(holder.getAdapterPosition()).getUid());
                                    intent.putExtra("userName", currentRequestsArrayList.get(holder.getAdapterPosition()).getName());
                                    intent.putExtra("userImage", currentRequestsArrayList.get(holder.getAdapterPosition()).getProfileUrl());
                                    startActivity(intent);
                                }
                            });
                }
            });
        }

        @Override
        public int getItemCount() {
            if (currentRequestsArrayList == null)
                return 0;
            return currentRequestsArrayList.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            TextView rightText;
            CircleImageView profileImageview;
            Button acceptBtn;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                rightText = v.findViewById(R.id.nametextviewnotification);
                profileImageview = v.findViewById(R.id.profileImagelayoutnotificationrequest);
                acceptBtn = v.findViewById(R.id.acceptrequestbtnlayoutnotificationrequest);
            }
        }

    }

}
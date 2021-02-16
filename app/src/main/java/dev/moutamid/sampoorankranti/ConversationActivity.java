package dev.moutamid.sampoorankranti;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity {
    private static final String TAG = "ConversationActivity";

    private ArrayList<ChatMessage> currentMessagesArrayList = new ArrayList<>();
    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private TextView userNameTextView;
    private String chatName;
    private CircleImageView userProfileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        userNameTextView = findViewById(R.id.user_name_conversation);
        userProfileImageView = findViewById(R.id.user_profile_image_conversation);

        if (getIntent().hasExtra("chatName")){

            userNameTextView.setText(getIntent().getStringExtra("userName"));
            Picasso.with(ConversationActivity.this).load("userImage").into(userProfileImageView);

            chatName = getIntent().getStringExtra("chatName");

            DatabaseReference databaseChats = FirebaseDatabase.getInstance().getReference().child("chats");

            databaseChats.child(chatName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    currentMessagesArrayList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        currentMessagesArrayList.add(snapshot.getValue(ChatMessage.class));

                    }

                    initRecyclerView();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.addBtnLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = findViewById(R.id.editTextconversation);

                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Toast.makeText(ConversationActivity.this, "Please add a message!", Toast.LENGTH_SHORT).show();
                } else {



                    ChatMessage chatMessage = new ChatMessage(editText.getText().toString(), firebaseAuth.getCurrentUser().getUid());

                    adapter.addMessage(chatMessage);

                }

            }
        });

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");

        conversationRecyclerView = findViewById(R.id.recyclerView_conversation);
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
            <RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (viewType == 1) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msg_left, parent, false);
                return new ViewHolderLeftMessage(view);

            } else {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msg_right, parent, false);
                return new ViewHolderRightMessage(view1);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if (holder.getItemViewType() == 1) {
                ViewHolderLeftMessage holderLeftMessage = (ViewHolderLeftMessage) holder;
                holderLeftMessage.leftText.setText(currentMessagesArrayList.get(holder.getAdapterPosition()).getMsgText());
            } else {
                ViewHolderRightMessage holderRightMessage = (ViewHolderRightMessage) holder;
                holderRightMessage.rightText.setText(currentMessagesArrayList.get(holder.getAdapterPosition()).getMsgText());
            }

        }

        @Override
        public int getItemCount() {
            if (currentMessagesArrayList == null)
                return 0;
            return currentMessagesArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {

            /*

             * 1 Left Message
             * 2 Right Message
             */

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            if (currentMessagesArrayList.get(position).getMsgUser().equals(firebaseAuth.getCurrentUser().getUid())) {
                return 2;
            } else {
                return 1;
            }
        }

        public class ViewHolderLeftMessage extends RecyclerView.ViewHolder {

            TextView leftText;

            public ViewHolderLeftMessage(@NonNull View v) {
                super(v);
                leftText = v.findViewById(R.id.leftText);
            }
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            TextView rightText;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                rightText = v.findViewById(R.id.rightText);
            }
        }

        public void addMessage(ChatMessage c) {
            Log.d(TAG, "addMessage: ");

            currentMessagesArrayList.add(c);

            notifyItemInserted(currentMessagesArrayList.size() - 1);

            scrollRecyclerViewToEnd();

            // Uploading the message to database
            DatabaseReference databaseChats = FirebaseDatabase.getInstance().getReference().child("chats");
            databaseChats.child(chatName).push().setValue(c);

        }

    }

}
package dev.moutamid.sampoorankranti;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashboardActivity extends AppCompatActivity {
    private static final String TAG = "AdminDashboardActivity";

    private TextView totalUsers_tv, totalAdmins_tv;//totalBalance_tv,
    private TextView nonVerifiedUsers_tv, verifiedUsers_tv;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private Utils utils = new Utils();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        initViews();

        // CHECKING FOR ALL THE METHODS TO COMPLETE AND THEN DISMISSING THE DIALOG
        dialog = new ProgressDialog(AdminDashboardActivity.this);
        dialog.setMessage("Loading details...");
        dialog.show();

        // GETTING TOTAL BALANCE, TOTAL WITHDRAW, CURRENT BALANCE,
        // ACCOUNT STATUS, TOTAL REFERRALS AMOUNT, PAID REFERRALS AMOUNT
        getTotalUsersFromDatabase();

    }

    private void getTotalUsersFromDatabase() {
        databaseReference.child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {//.child(mAuth.getCurrentUser().getUid())
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            long count = snapshot.getChildrenCount();

                            totalUsers_tv.setText(String.valueOf(count));

                        } else totalUsers_tv.setText("0");

                        getVerifiedUsersFromDatabase();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "onCancelled: " + error.toException());

                        Toast.makeText(AdminDashboardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        //isDone_getDetailsFromDatabase = true;
                    }
                });

    }

    private void getVerifiedUsersFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").orderByChild("isEmailVerified").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "onDataChange: getVerifiedUsersFromDatabase");
                        if (snapshot.exists()) {

                            Log.d(TAG, "onDataChange: snapshots exist");
                            long count = snapshot.getChildrenCount();
                            Log.d(TAG, "onDataChange: "+count);

                            verifiedUsers_tv.setText(String.valueOf(count));

                        } else verifiedUsers_tv.setText("0");

                        getNonVerifiedUsersFromDatabase();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                        Log.i("TAG", "onCancelled: verify if member exists" + error.toException().getMessage());
                        Toast.makeText(AdminDashboardActivity.this, error.toException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getNonVerifiedUsersFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").orderByChild("isEmailVerified").equalTo(false)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            long count = snapshot.getChildrenCount();

                            nonVerifiedUsers_tv.setText(String.valueOf(count));

                        } else nonVerifiedUsers_tv.setText("0");

                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                        Log.i("TAG", "onCancelled: verify if member exists" + error.toException().getMessage());
                        Toast.makeText(AdminDashboardActivity.this, error.toException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initViews() {
        Log.i(TAG, "initViews: ");

//        totalBalance_tv = findViewById(R.id.total_balance_tv_dashboard);
        totalUsers_tv = findViewById(R.id.total_withdraw_tv_dashboard);
        totalAdmins_tv = findViewById(R.id.current_balance_tv_dashboard);
        //accountStatus_tv = v.findViewById(R.id.account_status_level_tv_dashboard);
        nonVerifiedUsers_tv = findViewById(R.id.total_referrals_tv_dashboard);
        verifiedUsers_tv = findViewById(R.id.paid_members_tv_dashboard);

        findViewById(R.id.logoutImagviewadmindashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogoutDialog();

            }
        });

    }

    private void showLogoutDialog() {

        utils.showDialog(AdminDashboardActivity.this,
                "Are you sure!",
                "You really want to logout?",
                "Yes",
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // LOGOUT
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        utils.removeSharedPref(AdminDashboardActivity.this);

                        finish();
                        Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        Toast.makeText(AdminDashboardActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                },
                true
        );
    }

}
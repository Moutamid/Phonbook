package dev.moutamid.sampoorankranti;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import me.itangqi.waveloadingview.WaveLoadingView;

public class FilterChatActivity extends AppCompatActivity {

    private ArrayList<UserCompleteDetailsModel> userDetailsList = new ArrayList<>();

    private ProgressDialog progressDialog;

    private TextView mNoEt, districtEt, villageEt, wardEt, categoryEt;
    private String mNoStr, districtStr, villageStr, wardStr, categoryStr = null;
    private Button filterButton;

    public void EdittextListenersRegistrationfilterchat(View view) {
        TextView textView = (TextView) view;

        String tag = textView.getTag().toString();

        switch (tag) {

            case "mNo":
                CharSequence[] items1 = {"Something", "Something", "Something", "Something", "Something", "Something", "Something", "Something", "Something", "Something"};
                showVillagesOptionsDialog(textView, items1);
                break;

            case "district":
                CharSequence[] items2 = {"North East Delhi", "Central Delhi", "East Delhi", "Chennai", "Kolkata", "Mumbai Suburban", "Mumbai City", "West Delhi", "Hyderabad", "North Delhi"};
                showVillagesOptionsDialog(textView, items2);
                break;

            case "village":
                CharSequence[] items3 = {"Jammu and Kashmir", "Himachal Pradesh", "Punjab", "Chandigarh", "Uttaranchal", "Haryana", "Arunachal Pradesh", "Rajasthan", "Uttar Pradesh", "Bihar"};
                showVillagesOptionsDialog(textView, items3);
                break;

            case "ward":
                CharSequence[] items4 = {"Ward 1", "Ward 2", "Ward 3", "Ward 4"};
                showVillagesOptionsDialog(textView, items4);
                break;

            case "category":
                CharSequence[] items5 = {"A", "P", "R", "M"};
                showVillagesOptionsDialog(textView, items5);
                break;

        }
    }

    private void showVillagesOptionsDialog(final TextView tv, final CharSequence[] items) {

        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(FilterChatActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                //              Toast.makeText(AdminActivityApproveReferrals.this, key + "\n" + items[position], Toast.LENGTH_LONG).show();
                //uploadFeedbackRemarks(remarksTV, key, String.valueOf(items[position]));
                tv.setText(String.valueOf(items[position]));

                String tag = tv.getTag().toString();

                switch (tag) {

                    case "mNo":
                        mNoStr = String.valueOf(items[position]);
                        break;

                    case "district":
                        districtStr = String.valueOf(items[position]);
                        break;

                    case "village":
                        villageStr = String.valueOf(items[position]);
                        break;

                    case "ward":
                        wardStr = String.valueOf(items[position]);
                        break;

                    case "category":
                        categoryStr = String.valueOf(items[position]);
                        break;

                }

            }
        });

        dialog = builder.create();
        dialog.show();

    }

    private void initViews() {
        mNoEt = findViewById(R.id.editTextmNo_registerfilterchat);
        districtEt = findViewById(R.id.editTextdistrict_registerfilterchat);
        villageEt = findViewById(R.id.editTextvillage_registerfilterchat);
        wardEt = findViewById(R.id.editTextWard_registerfilterchat);
        categoryEt = findViewById(R.id.editTextCategoryfilterchat);
        filterButton = findViewById(R.id.cirUploadDetailsBtn__registerfilterchat);
        filterButton.setOnClickListener(filterButtonClickListener());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        WaveLoadingView loadingView = findViewById(R.id.waveloadingviewfilterchat);
        loadingView.setAnimDuration(10000);

    }

    private View.OnClickListener filterButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fieldsNotExist()) {
                    return;
                }

                progressDialog.show();

                searchUserByQuery("category", categoryStr);

            }
        };
    }

    private boolean fieldsNotExist() {
        if (mNoStr == null) {
            Toast.makeText(this, "Please select M. No!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (districtStr == null) {
            Toast.makeText(this, "Please select a district!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (villageStr == null) {
            Toast.makeText(this, "Please select a Village!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (wardStr == null) {
            Toast.makeText(this, "Please select a ward!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (categoryStr == null) {
            Toast.makeText(this, "Please select a Category!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_chat);

        initViews();


    }

    private void searchUserByQuery(String orderByChild, String equalTo) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").orderByChild(orderByChild).equalTo(equalTo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        userDetailsList.clear();

                        if (snapshot.exists()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                String uid = dataSnapshot.getKey();
                                UserCompleteDetailsModel detailsModel = dataSnapshot.getValue(UserCompleteDetailsModel.class);
                                detailsModel.setUid(uid);

                                userDetailsList.add(detailsModel);

                                //Toast.makeText(getActivity(), key, Toast.LENGTH_SHORT).show();
                            }

                            // FOR FILTERING THE ENTRIES WITH M_NO
                            for (int i = 0; i <= userDetailsList.size() - 1; i++) {

                                if (!userDetailsList.get(i).getmNo().equals(mNoStr)) {
                                    userDetailsList.remove(i);
                                }

                            }
                            // FOR FILTERING THE ENTRIES WITH DISTRICTS
                            for (int i = 0; i <= userDetailsList.size() - 1; i++) {

                                if (!userDetailsList.get(i).getDistrict().equals(districtStr)) {
                                    userDetailsList.remove(i);
                                }

                            }
                            // FOR FILTERING THE ENTRIES WITH VILLAGES
                            for (int i = 0; i <= userDetailsList.size() - 1; i++) {

                                if (!userDetailsList.get(i).getVillage().equals(villageStr)) {
                                    userDetailsList.remove(i);
                                }

                            }
                            // FOR FILTERING THE ENTRIES WITH WARD
                            for (int i = 0; i <= userDetailsList.size() - 1; i++) {

                                if (!userDetailsList.get(i).getWard().equals(wardStr)) {
                                    userDetailsList.remove(i);
                                }

                            }

                            if (userDetailsList.size() == 0) {
                                progressDialog.dismiss();
                                Toast.makeText(FilterChatActivity.this, "No user found with this filter!", Toast.LENGTH_SHORT).show();
                            } else {

                                progressDialog.dismiss();

                                // TRANSFERRING THE LIST TO THE OTHER ACTIVITY
                                Intent intent = new Intent(FilterChatActivity.this, UsersListActivity.class);
                                Bundle args = new Bundle();
                                args.putSerializable("USERSLIST", (Serializable)userDetailsList);
                                intent.putExtra("BUNDLE", args);
                                startActivity(intent);

                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(FilterChatActivity.this, "No user exist!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("TAG", "onCancelled: verify if member exists" + error.toException().getMessage());
                        Toast.makeText(FilterChatActivity.this, error.toException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

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
}
package dev.moutamid.sampoorankranti;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.itangqi.waveloadingview.WaveLoadingView;

public class SecondRegistrationActivity extends AppCompatActivity {
    private static final String TAG = "SecondRegistrationActiv";
    private CircleImageView profileImage;
    private TextView mNoEt, districtEt, villageEt, wardEt, categoryEt;
    private String nickname, mNoStr, districtStr, villageStr, wardStr, categoryStr = null;
    private Button submitButton;
    private Utils utils = new Utils();
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    private void setSubmitButtonClickListener() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadCompleteDetails();

            }
        });
    }

    private static final int GALLERY_REQUEST = 1;
    private String profileImageUrl = null;
    // User Selected Image
//    private Uri imageUri = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            //imageUri = data.getData();
            Uri imageUri = data.getData();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profileImages");

            progressDialog.show();

            final StorageReference filePath = storageReference
                    .child(mAuth.getCurrentUser().getUid() + imageUri.getLastPathSegment());
//            final StorageReference filePath = storageReference.child("sliders")
//                    .child(imageUri.getLastPathSegment());

            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri photoUrl) {

//                            TextView othertxt = findViewById(R.id.othertextregistration);
//                            othertxt.setText(photoUrl.toString());
                            profileImage.setImageURI(data.getData());
                            profileImageUrl = photoUrl.toString();
                            progressDialog.dismiss();
                            Toast.makeText(SecondRegistrationActivity.this, "Upload done!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(SecondRegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void setProfileImageClickListener() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }

    public void EdittextListenersRegistration(View view) {
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

    private static class UserCompleteDetailsModel {
        private String name, email, profileUrl, mNo, district, village, ward, category;
        private boolean isEmailVerified;

        public UserCompleteDetailsModel(String name, String email, String profileUrl, String mNo, String district, String village, String ward, String category, boolean isEmailVerified) {
            this.isEmailVerified = isEmailVerified;
            this.name = name;
            this.email = email;
            this.profileUrl = profileUrl;
            this.mNo = mNo;
            this.district = district;
            this.village = village;
            this.ward = ward;
            this.category = category;
        }

        public boolean isEmailVerified() {
            return isEmailVerified;
        }

        public void setEmailVerified(boolean emailVerified) {
            isEmailVerified = emailVerified;
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

    private void uploadCompleteDetails() {

        if (fieldsNotExist()) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SecondRegistrationActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        UserCompleteDetailsModel details = new UserCompleteDetailsModel();

        details.setName(nickname);
        details.setEmail(auth.getCurrentUser().getEmail());
        details.setProfileUrl(profileImageUrl);
        details.setmNo(mNoStr);
        details.setDistrict(districtStr);
        details.setVillage(villageStr);
        details.setWard(wardStr);
        details.setCategory(categoryStr);
        details.setEmailVerified(false);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            final SharedPreferences sharedPreferences = SecondRegistrationActivity.this.getSharedPreferences("dev.moutamid.sampoorankranti", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("myprofilelink", profileImageUrl).apply();

                            progressDialog.dismiss();

                            utils.storeBoolean(SecondRegistrationActivity.this, "isLoggedIn", true);

                            finish();
                            Intent intent = new Intent(SecondRegistrationActivity.this, DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Toast.makeText(SecondRegistrationActivity.this, "DONE", Toast.LENGTH_LONG).show();

                        } else {
                            Log.i(TAG, "onComplete: task failed " + task.getException().getMessage());
                            progressDialog.dismiss();
                            Toast.makeText(SecondRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private boolean fieldsNotExist() {
        if (nickname == null) {
            Toast.makeText(this, "No username found!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (profileImageUrl == null) {
            Toast.makeText(this, "Please select a picture!", Toast.LENGTH_SHORT).show();
            return true;
        }
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

    private void initViews() {
        profileImage = findViewById(R.id.profileimageview_secondregistration);
        mNoEt = findViewById(R.id.editTextmNo_register);
        districtEt = findViewById(R.id.editTextdistrict_register);
        villageEt = findViewById(R.id.editTextvillage_register);
        wardEt = findViewById(R.id.editTextWard_register);
        categoryEt = findViewById(R.id.editTextCategory);
        submitButton = findViewById(R.id.cirUploadDetailsBtn__register);

        progressDialog = new ProgressDialog(SecondRegistrationActivity.this);
        progressDialog.setMessage("Loading...");

        WaveLoadingView loadingView = findViewById(R.id.waveloadingview_secondregistration);
        loadingView.setAnimDuration(10000);

        mAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPreferences = getSharedPreferences("dev.moutamid.sampoorankranti", Context.MODE_PRIVATE);

        if (!sharedPreferences.getString("myprofilelink", "error").equals("error")) {

            finish();
            Intent intent = new Intent(SecondRegistrationActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

        if (getIntent().hasExtra("username")) {

            nickname = getIntent().getStringExtra("username");

        } else {

            nickname = mAuth.getCurrentUser().getDisplayName();

        }
//TODO: STORE USERNAME BY CUTTING THROUGH THE EMAIL BY SSUBSTRING
    }

    private void showVillagesOptionsDialog(final TextView tv, final CharSequence[] items) {

        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(SecondRegistrationActivity.this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_registration);

        initViews();

        if (checkCurrentActivity()){
            return;
        }

        setProfileImageClickListener();

        setSubmitButtonClickListener();

        //showVillagesOptionsDialog();
    }

    private boolean checkCurrentActivity() {

        //current_activity
        //main
        //second
        //dashboard

        if (utils.getStoredString(SecondRegistrationActivity.this, "current_activity").equals("Error")) {

            finish();
            Intent intent = new Intent(SecondRegistrationActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

        }

        utils.storeString(SecondRegistrationActivity.this, "current_activity", "second");

        if (utils.getStoredString(SecondRegistrationActivity.this, "current_activity").equals("main")) {

            finish();
            Intent intent = new Intent(SecondRegistrationActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

        }

        if (utils.getStoredString(SecondRegistrationActivity.this, "current_activity").equals("dashboard")) {

            finish();
            Intent intent = new Intent(SecondRegistrationActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            return true;

        }

        return false;
    }
}
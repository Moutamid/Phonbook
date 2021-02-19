package dev.moutamid.sampoorankranti;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import me.itangqi.waveloadingview.WaveLoadingView;

public class FilterChatActivity extends AppCompatActivity {

//    private ArrayList<UserCompleteDetailsModel> userDetailsList = new ArrayList<>();

//    private ProgressDialog progressDialog;

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

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);

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

//                progressDialog.show();

                Intent intent = new Intent(FilterChatActivity.this, UsersListActivity.class);
//                private String mNoStr, districtStr, villageStr, wardStr, categoryStr = null;
                intent.putExtra("mNo", mNoStr);
                intent.putExtra("district", districtStr);
                intent.putExtra("village", villageStr);
                intent.putExtra("ward", wardStr);
                intent.putExtra("category", categoryStr);

                startActivity(intent);

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
}
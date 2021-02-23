package dev.moutamid.sampoorankranti;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdvertiseActivity extends AppCompatActivity {

    private String districtStr, villageStr, wardStr, categoryStr, complaintStr, captionStr = null;
    //    private EditText userNumberEditText;
    private EditText editText;

//    private Utils utils = new Utils();
//    private String vehicleStr;
//    private ImageView docImageview;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            //imageUri = data.getData();
            Uri imageUri = data.getData();

            TextView textView = findViewById(R.id.fileChoosenDescription);
            textView.setText(imageUri.getLastPathSegment());

//            docImageview.setImageURI(data.getData());
        }

    }

    private boolean fieldsNotExist() {
        if (editText.getVisibility() == View.VISIBLE && TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Please enter your details!");
            editText.requestFocus();
            return true;
        }
        if (captionStr == null) {
            Toast.makeText(this, "Please select a Caption!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (complaintStr == null || complaintStr.equals("NO")) {
            Toast.makeText(this, "Please select YES on complaint question!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (districtStr == null) {
            Toast.makeText(this, "Please select a State!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (villageStr == null) {
            Toast.makeText(this, "Please select a District!", Toast.LENGTH_SHORT).show();
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

    private void showVillagesOptionsDialog(final TextView tv, final CharSequence[] items) {

        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(AdvertiseActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                //              Toast.makeText(AdminActivityApproveReferrals.this, key + "\n" + items[position], Toast.LENGTH_LONG).show();
                //uploadFeedbackRemarks(remarksTV, key, String.valueOf(items[position]));
                tv.setText(String.valueOf(items[position]));

                String tag = tv.getTag().toString();

                switch (tag) {

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

                    case "complaint":
                        complaintStr = String.valueOf(items[position]);
                        break;

                }

            }
        });

        dialog = builder.create();
        dialog.show();

    }

    private void showCaptionsOptionsDialog(final TextView tv, final CharSequence[] items) {

        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(AdvertiseActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                //              Toast.makeText(AdminActivityApproveReferrals.this, key + "\n" + items[position], Toast.LENGTH_LONG).show();
                //uploadFeedbackRemarks(remarksTV, key, String.valueOf(items[position]));
                tv.setText(String.valueOf(items[position]));
                captionStr = String.valueOf(items[position]);

                switch (position) {

                    case 0:
                    case 1:
                    case 2:
                    case 3:

                        editText.setVisibility(View.GONE);
                        break;

                    case 4:
                        editText.setVisibility(View.VISIBLE);

                        break;

                    case 5:
                        editText.setVisibility(View.VISIBLE);
                        editText.setHint("Please enter place and time");
                        break;

                    case 6:
                        editText.setVisibility(View.VISIBLE);
                        editText.setHint("Please enter mobile number");
                        break;

                }

            }
        });

        dialog = builder.create();
        dialog.show();

    }

    public void EdittextListenersactivityadvertise(View view) {
        TextView textView = (TextView) view;

        String tag = textView.getTag().toString();

        switch (tag) {

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

            case "caption":

                CharSequence[] items6 = {
                        "I have come to expect co-operation",
                        "We will be waiting for you",
                        "Family members are searching for it",
                        "State police are searching for it",
                        "The vehicle of this number has just been stolen",
                        "Address: Enter your address",
                        "Patch immediately when it appears or contact on my number"};
                showCaptionsOptionsDialog(textView, items6);

//                checkIfNetworkIsAvailable();

//                Toast.makeText(this, "Get location", Toast.LENGTH_SHORT).show();
                break;

            case "complaint":

                CharSequence[] items7 = {"NO", "YES"};
                showVillagesOptionsDialog(textView, items7);

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);


        editText = findViewById(R.id.numberEdittextactivityadvertise);

        findViewById(R.id.cirUploadDetailsBtn_activityadvertise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fieldsNotExist()) {
                    return;
                }

                Toast.makeText(AdvertiseActivity.this, "Done", Toast.LENGTH_SHORT).show();


            }
        });

        findViewById(R.id.chooseFileBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }

}
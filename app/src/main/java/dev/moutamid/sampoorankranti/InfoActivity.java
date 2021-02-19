package dev.moutamid.sampoorankranti;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class InfoActivity extends AppCompatActivity {
    private static final String TAG = "InfoActivity";

    //  private TextView mNoEt, districtEt, villageEt, wardEt, categoryEt;
    private String districtStr, villageStr, wardStr, categoryStr, latitudeStr, longitudeStr, userNumberStr = null;
    private Button filterButton;
    private EditText userNumberEditText;
    private boolean isChecked = false;

    private Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Log.d(TAG, "onCreate: ");

        initViews();

    }

    public void EdittextListenersactivityinfo(View view) {
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

            case "getLocation":

                checkIfNetworkIsAvailable();

//                Toast.makeText(this, "Get location", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void checkIfNetworkIsAvailable() {

        int permissionState = ActivityCompat.checkSelfPermission(InfoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionState == PackageManager.PERMISSION_GRANTED){
            return;
        }

        Dexter.withActivity(InfoActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // PERMISSION GRANTED
                        // ACCESSING LOCATION
                        SmartLocation.with(InfoActivity.this).location().oneFix().start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                latitudeStr = String.valueOf(location.getLatitude());
                                longitudeStr = String.valueOf(location.getLongitude());
                                Toast.makeText(InfoActivity.this, location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()){
                            Toast.makeText(InfoActivity.this, "You need to allow location to use this feature", Toast.LENGTH_SHORT).show();
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                });
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SmartLocation.with(InfoActivity.this).location().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmartLocation.with(InfoActivity.this).location().stop();
    }

    private void showVillagesOptionsDialog(final TextView tv, final CharSequence[] items) {

        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
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

                }

            }
        });

        dialog = builder.create();
        dialog.show();

    }

    private View.OnClickListener filterButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fieldsNotExist()) {
                    return;
                }

                utils.storeString(InfoActivity.this, "districtStr", districtStr);
                utils.storeString(InfoActivity.this, "villageStr", villageStr);
                utils.storeString(InfoActivity.this, "wardStr", wardStr);
                utils.storeString(InfoActivity.this, "categoryStr", categoryStr);
                utils.storeString(InfoActivity.this, "latitudeStr", latitudeStr);
                utils.storeString(InfoActivity.this, "longitudeStr", longitudeStr);
                if (isChecked)
                utils.storeString(InfoActivity.this, "userNumberStr", userNumberStr);


                startActivity(new Intent(InfoActivity.this, NeedHelpFormActivity.class));

//                progressDialog.show();

//                Intent intent = new Intent(InfoActivity.this, UsersListActivity.class);
//                private String mNoStr, districtStr, villageStr, wardStr, categoryStr = null;
//                intent.putExtra("mNo", mNoStr);
//                intent.putExtra("district", districtStr);
//                intent.putExtra("village", villageStr);
//                intent.putExtra("ward", wardStr);
//                intent.putExtra("category", categoryStr);
//
//                startActivity(intent);

            }
        };
    }

    private boolean fieldsNotExist() {
        userNumberStr = userNumberEditText.getText().toString();

        if (isChecked && TextUtils.isEmpty(userNumberStr) && userNumberStr.equals("")) {
            Toast.makeText(this, "Please enter your number!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (latitudeStr == null || longitudeStr == null) {
            Toast.makeText(this, "Please select your location!", Toast.LENGTH_SHORT).show();
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

//        districtEt = findViewById(R.id.editTextdistrict_registerfilterchat);
//        villageEt = findViewById(R.id.editTextvillage_registerfilterchat);
//        wardEt = findViewById(R.id.editTextWard_registerfilterchat);
//        categoryEt = findViewById(R.id.editTextCategoryfilterchat);
        userNumberEditText = findViewById(R.id.numberEdittextactivityinfo);
        filterButton = findViewById(R.id.cirUploadDetailsBtn_activityinfo);
        filterButton.setOnClickListener(filterButtonClickListener());

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);

//        WaveLoadingView loadingView = findViewById(R.id.waveloadingviewfilterchat);
//        loadingView.setAnimDuration(10000);

    }

    //
    public void checkboxItemClicked(View view) {
        CheckBox checkbox = (CheckBox) view;
        if (checkbox.isChecked()) {

            findViewById(R.id.numberEdittextactivityinfo).setVisibility(View.VISIBLE);

        } else findViewById(R.id.numberEdittextactivityinfo).setVisibility(View.GONE);

        isChecked = checkbox.isChecked();
    }

}
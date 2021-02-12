package dev.moutamid.sampoorankranti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import me.itangqi.waveloadingview.WaveLoadingView;

public class SecondRegistrationActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView mNoEt, districtEt, villageEt, wardEt, categoryEt;
    private Button submitButton;

    private void setSubmitButtonClickListener() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SecondRegistrationActivity.this, mNoEt.getText().toString() + districtEt.getText().toString() + villageEt.getText().toString() + wardEt.getText().toString() + categoryEt.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private static final int GALLERY_REQUEST = 1;
    // User Selected Image
//    private Uri imageUri = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            //imageUri = data.getData();
            Uri userImage = data.getData();

            profileImage.setImageURI(userImage);
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

    public void EdittextListenersRegistration(View view){
        EditText editText = (EditText) view;
        showVillagesOptionsDialog(editText);
    }

    private void initViews() {
        profileImage = findViewById(R.id.profileimageview_secondregistration);
        mNoEt = findViewById(R.id.editTextmNo_register);
        districtEt = findViewById(R.id.editTextdistrict_register);
        villageEt = findViewById(R.id.editTextvillage_register);
        wardEt = findViewById(R.id.editTextWard_register);
        categoryEt = findViewById(R.id.editTextCategory);
        submitButton = findViewById(R.id.cirUploadDetailsBtn__register);

        WaveLoadingView loadingView = findViewById(R.id.waveloadingview_secondregistration);
        loadingView.setAnimDuration(10000);
    }

    private void showVillagesOptionsDialog(EditText editText) {

        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(SecondRegistrationActivity.this);
        final CharSequence[] items = {"Something", "Something", "Something", "Something", "Something", "Something", "Something", "Something", "Something", "Something"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                //              Toast.makeText(AdminActivityApproveReferrals.this, key + "\n" + items[position], Toast.LENGTH_LONG).show();
                //uploadFeedbackRemarks(remarksTV, key, String.valueOf(items[position]));
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

        setProfileImageClickListener();

        setSubmitButtonClickListener();

        //showVillagesOptionsDialog();
    }
}
package dev.moutamid.sampoorankranti;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdvertiseActivity extends AppCompatActivity {

    private String vehicleStr;
    private ImageView docImageview;
    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            //imageUri = data.getData();
            Uri imageUri = data.getData();

            docImageview.setImageURI(data.getData());
        }

    }

    private void setProfileImageClickListener() {

        docImageview = findViewById(R.id.uploadDocImageview);
        docImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        findViewById(R.id.uploadbtnadvertise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdvertiseActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });

        EdittextListenersAdvertise();

        setProfileImageClickListener();
    }

    private void EdittextListenersAdvertise() {
        LinearLayout linearLayout = findViewById(R.id.textInputPassword1225advertise);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items5 = {"Bike", "Car", "Truck", "Other"};

                final TextView textView = findViewById(R.id.editTextvillage_advertise);

                AlertDialog dialog;

                AlertDialog.Builder builder = new AlertDialog.Builder(AdvertiseActivity.this);
                builder.setItems(items5, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {

                        textView.setText(String.valueOf(items5[position]));


                        vehicleStr = String.valueOf(items5[position]);


                    }
                });

                dialog = builder.create();
                dialog.show();

            }
        });

    }

}
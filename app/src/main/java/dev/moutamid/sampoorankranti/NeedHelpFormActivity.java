package dev.moutamid.sampoorankranti;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class NeedHelpFormActivity extends AppCompatActivity {
    private static final String TAG = "NeedHelpFormActivity";

    private ImageView profileImageview, statusImageview;
    private RelativeLayout numberLayout;
    private Utils utils = new Utils();
    private TextView numberTextview, stateTextview, districtTextview, wardTextview, locationTextview, gotoLocationTextview;

    private void initViews() {

        profileImageview = findViewById(R.id.profileimageneedhelpform);
        statusImageview = findViewById(R.id.verificationStatusImageviewneedhelpform);
        numberLayout = findViewById(R.id.numberlayoutneedhelpform);
        numberTextview = findViewById(R.id.numberTextviewneedhelpform);
        stateTextview = findViewById(R.id.stateTextviewneedhelpform);
        districtTextview = findViewById(R.id.districtTextviewneedhelpform);
        wardTextview = findViewById(R.id.wardTextviewneedhelpform);
        locationTextview = findViewById(R.id.locationTextviewneedhelpform);
        gotoLocationTextview = findViewById(R.id.gotoLocationTextviewneedhelpform);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help_form);

        initViews();

        setvaluesonViews();
    }

    private void setvaluesonViews() {
//    private String , , ,
        //    categoryStr, , , ,;

        if (!utils.getStoredString(NeedHelpFormActivity.this, "userNumberStr").equals("Error")){

            numberLayout.setVisibility(View.VISIBLE);
            numberTextview.setText(utils.getStoredString(NeedHelpFormActivity.this, "userNumberStr"));

        }
        
        Picasso.with(NeedHelpFormActivity.this)
                .load(utils.getStoredString(NeedHelpFormActivity.this, utils.getStoredString(NeedHelpFormActivity.this, "myprofilelink")))
                .placeholder(R.color.lighterGrey)
                .into(profileImageview);

        districtTextview.setText(utils.getStoredString(NeedHelpFormActivity.this, "districtStr"));
        stateTextview.setText(utils.getStoredString(NeedHelpFormActivity.this, "villageStr"));
        wardTextview.setText(utils.getStoredString(NeedHelpFormActivity.this, "wardStr"));

        String latitude = utils.getStoredString(NeedHelpFormActivity.this, "latitudeStr");
        String longitude = utils.getStoredString(NeedHelpFormActivity.this, "longitudeStr");
        locationTextview.setText(latitude+" , "+longitude);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser().isEmailVerified())
            Picasso.with(NeedHelpFormActivity.this).load(R.drawable.verfied).into(statusImageview);
        else
            Picasso.with(NeedHelpFormActivity.this).load(R.drawable.not_verified).into(statusImageview);
        
        gotoLocationTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NeedHelpFormActivity.this, "DONE", Toast.LENGTH_SHORT).show();
            }
        });
        
    }


}
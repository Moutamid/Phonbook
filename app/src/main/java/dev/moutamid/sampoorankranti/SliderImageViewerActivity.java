package dev.moutamid.sampoorankranti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class SliderImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_image_viewer);

        if (getIntent().hasExtra("imageurl")) {

            ImageView imageView = findViewById(R.id.imageviewSliderImageViewerActivity);
            Intent intent = getIntent();

            Picasso.with(SliderImageViewerActivity.this).load(intent.getStringExtra("imageurl"))
                    .into(imageView);

        } else Toast.makeText(this, "No image found!", Toast.LENGTH_SHORT).show();

    }
}
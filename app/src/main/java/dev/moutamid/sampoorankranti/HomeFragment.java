package dev.moutamid.sampoorankranti;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

public class HomeFragment extends Fragment {

    private SliderLayout sliderLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        sliderLayout = view.findViewById(R.id.sliderLayout_fragamenthome);

        DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
        defaultSliderView.image("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Alligator.jpg/220px-Alligator.jpg")
                .setOnSliderClickListener(OnDefaultSliderClickListener("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Alligator.jpg/220px-Alligator.jpg"));

        sliderLayout.addSlider(defaultSliderView);
        sliderLayout.addSlider(defaultSliderView);
        sliderLayout.addSlider(defaultSliderView);
        sliderLayout.addSlider(defaultSliderView);

        return view;
    }

    private BaseSliderView.OnSliderClickListener OnDefaultSliderClickListener(final String link) {
        return new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {

                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                showSliderImageViewDialog(link);

            }
        };
    }
    public void showSliderImageViewDialog(String link) {

//        Button okayBtn;
        ImageView dialogImageView;

        final Dialog dialogOffline = new Dialog(getActivity());
        dialogOffline.setContentView(R.layout.dialog_slider_imageview);

        dialogImageView = dialogOffline.findViewById(R.id.imageview_dialogslider);

        Glide.with(getActivity())
                .load(link)
                .into(dialogImageView);

        dialogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOffline.dismiss();
            }
        });

        dialogOffline.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogOffline.show();

    }
    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();

    }
}

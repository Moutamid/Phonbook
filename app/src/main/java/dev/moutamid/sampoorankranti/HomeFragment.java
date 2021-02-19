package dev.moutamid.sampoorankranti;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private SliderLayout sliderLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        sliderLayout = view.findViewById(R.id.sliderLayout_fragamenthome);

        String link1 = "https://firebasestorage.googleapis.com/v0/b/phonbook-258fd.appspot.com/o/profileImages%2Fsliders%2Fimage%3A2831?alt=media&token=4456ec94-e19a-4447-9cdd-ae4a3e1c913a";
        String link2 = "https://firebasestorage.googleapis.com/v0/b/phonbook-258fd.appspot.com/o/profileImages%2Fsliders%2Fimage%3A2832?alt=media&token=d64b020e-ee3c-49f0-9793-5168cba838ee";
        String link3 = "https://firebasestorage.googleapis.com/v0/b/phonbook-258fd.appspot.com/o/profileImages%2Fsliders%2Fimage%3A2833?alt=media&token=4d2954e7-acdc-4d15-a8ee-01907c0b9ee2";

        DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
        defaultSliderView.image(link1)
                .setOnSliderClickListener(OnDefaultSliderClickListener());
//        DefaultSliderView defaultSliderView1 = new DefaultSliderView(getActivity());
//        defaultSliderView.image(link2)
//                .setOnSliderClickListener(OnDefaultSliderClickListener());
//        DefaultSliderView defaultSliderView3 = new DefaultSliderView(getActivity());
//        defaultSliderView.image(link3)
//                .setOnSliderClickListener(OnDefaultSliderClickListener());

        sliderLayout.addSlider(defaultSliderView);
        sliderLayout.addSlider(defaultSliderView);
        sliderLayout.addSlider(defaultSliderView);


        view.findViewById(R.id.chatbtnfragmenthome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatsListActivity.class));
            }
        });
        view.findViewById(R.id.infobtnfragmenthome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FilterChatActivity.class));
            }
        });
        view.findViewById(R.id.advertisebtnfragmenthome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdvertiseActivity.class));
            }
        });
        view.findViewById(R.id.publicpollfragmenthome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PublicPollActivity.class));
            }
        });

//        getmyusernameandsaveit();

        return view;
    }

    private void getmyusernameandsaveit() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dev.moutamid.sampoorankranti", Context.MODE_PRIVATE);

        if (!sharedPreferences.getString("myprofilelink", "error").equals("error"))
            return;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                sharedPreferences.edit().putString("myprofilelink", snapshot.child("profileUrl").getValue(String.class)).apply();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private BaseSliderView.OnSliderClickListener OnDefaultSliderClickListener() {
        return new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.layout_zoom_image);

                ImageView okayBtn = dialog.findViewById(R.id.zoom_imagevieew);

                Picasso.with(getActivity()).load(slider.getUrl()).into(okayBtn);

                okayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        };
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();

    }

    @Override
    public void onClick(View v) {


    }
}

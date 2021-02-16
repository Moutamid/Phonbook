package dev.moutamid.sampoorankranti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class HomeFragment extends Fragment implements View.OnClickListener {

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

        view.findViewById(R.id.chatbtnfragmenthome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatsListActivity.class));
            }
        });
        view.findViewById(R.id.infobtnfragmenthome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InfoActivity.class));
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

        getmyusernameandsaveit();

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

    private BaseSliderView.OnSliderClickListener OnDefaultSliderClickListener(final String link) {
        return new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {

                Intent intent = new Intent(getActivity(), SliderImageViewerActivity.class);
                intent.putExtra("imageurl", slider.getUrl());
                startActivity(intent);

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

package org.srijaniitism.srijan.Fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.srijaniitism.srijan.AlertDialogManager;
import org.srijaniitism.srijan.ConnectionDetector;
import org.srijaniitism.srijan.Model.User;
import org.srijaniitism.srijan.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment {
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    private static final String TAG_NAME = "Name";
    FloatingActionButton fab1;
    CircleImageView imageView;
    TextView textName, textCollege, internet;
    ListView listView;
    AVLoadingIndicatorView avi;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    long size = 0;
    int i = 0;

    ArrayList<HashMap<String, String>> profileEvents = new ArrayList<>();
    //String[] strings = new String[];

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.profile_image);
        textName = view.findViewById(R.id.username);;
        textCollege = view.findViewById(R.id.college);
        avi = view.findViewById(R.id.avi);
        internet = view.findViewById(R.id.internet);
//        fab1 = view.findViewById(R.id.fab_1);
//        fab1.setVisibility(View.GONE);
        cd = new ConnectionDetector(getApplicationContext());

        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            avi.setVisibility(View.GONE);
            internet.setVisibility(View.VISIBLE);

            return view;
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getProfilePic()).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        avi.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        textName.setVisibility(View.VISIBLE);
                        textCollege.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                textCollege.setText(user.getCollege());
                textName.setText(firebaseUser.getDisplayName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        reference.child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> map = new HashMap<String, String>();
                size = dataSnapshot.getChildrenCount();

                LinearLayout linlay2=view.findViewById(R.id.linlay2);
                linlay2.removeAllViews();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    map.put(TAG_NAME + i + "", snapshot.getKey());
                    Log.d("snapshot", snapshot.getKey());
                    i++;
                    String x=snapshot.getKey();
                    try {
                        TextView tv = new TextView(getActivity());
                        tv.setText(x);
                        tv.setTextSize(20);
                        tv.setTextColor(Color.rgb(255,255,255));
                        tv.setTypeface(Typeface.MONOSPACE);
                        linlay2.addView(tv);
                    }
                    catch (Exception e)
                    {
                        //Toast.makeText(getContext(),"Hold on",Toast.LENGTH_SHORT).show();
                        Log.e("crash","App Crashed");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
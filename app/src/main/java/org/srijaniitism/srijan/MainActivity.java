package org.srijaniitism.srijan;

import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.srijaniitism.srijan.Fragments.DevFragment;
import org.srijaniitism.srijan.Fragments.FeedFragment;
import org.srijaniitism.srijan.Fragments.HomeFragment;
import org.srijaniitism.srijan.Fragments.MerchFragment;
import org.srijaniitism.srijan.Fragments.ProfileFragment;

import org.srijaniitism.srijan.R;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity  implements SimpleGestureFilter.SimpleGestureListener {
    FirebaseUser user;
    DatabaseReference databaseReference;

    private SimpleGestureFilter detector;
    Fragment[] fragments;
    int[] layout_id;
    FrameLayout frameLayout;

    ChipNavigationBar navigation;
    PanoramaImageView panoramaImageView;
    int currfrag = 0;

    final Fragment fragment_home = new HomeFragment();
    final Fragment fragment_feed = new FeedFragment();
    final Fragment fragment_profile = new ProfileFragment();
    final Fragment fragment_dev = new DevFragment();
    final Fragment fragment_merch = new MerchFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment_home;

    private GyroscopeObserver gyroscopeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title

        //getWindow().setStatusBarColor(Color.BLACK);
        setupWindowAnimations();
        setContentView(R.layout.activity_main);

        AppCompatDelegate.getDefaultNightMode();

        frameLayout = findViewById(R.id.container);

       // fragments = new Fragment[]{fragment_home, fragment_feed, fragment_profile, fragment_dev};
        layout_id = new int[]{ R.id.links,R.id.feed, R.id.home,R.id.profile, R.id.dev};

        navigation = findViewById(R.id.navigation);
        panoramaImageView = findViewById(R.id.panorama_image_view);




        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);



        navigation.setItemSelected(R.id.home, true);
        navigation.setZ(100);

        gyroscopeObserver = new GyroscopeObserver();

        gyroscopeObserver.setMaxRotateRadian(Math.PI / 12);

        PanoramaImageView panoramaImageView = findViewById(R.id.panorama_image_view);

        panoramaImageView.setGyroscopeObserver(gyroscopeObserver);
        panoramaImageView.setOnPanoramaScrollListener(new PanoramaImageView.OnPanoramaScrollListener() {
            @Override
            public void onScrolled(PanoramaImageView view, float offsetProgress) {

            }
        });

        detector = new SimpleGestureFilter(this,this);

    }



    private ChipNavigationBar.OnItemSelectedListener mOnNavigationItemSelectedListener = new ChipNavigationBar.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int i) {
           // Toast.makeText(getApplicationContext(),i + " ",Toast.LENGTH_SHORT).show();
            switch (i) {


                case R.id.links:
                    for(Fragment f:fm.getFragments())
                        fm.beginTransaction().remove(f).commit();
                    currfrag = 0;
                    fm.beginTransaction().add(R.id.container,fragment_merch,"link").commit();
                    active = fragment_merch;
                    return;

                case R.id.feed:
                    for(Fragment f:fm.getFragments())
                        fm.beginTransaction().remove(f).commit();
                    currfrag=1;
                    fm.beginTransaction().add(R.id.container,fragment_feed,"feed").commit();
                    active = fragment_feed;
                    return;

                case R.id.home:
                    for(Fragment f:fm.getFragments())
                        fm.beginTransaction().remove(f).commit();
                    currfrag = 2;
                    fm.beginTransaction().add(R.id.container,fragment_home,"home").commit();
                    active = fragment_home;
                    return;

                case R.id.profile:
                    for(Fragment f:fm.getFragments())
                        fm.beginTransaction().remove(f).commit();
                    currfrag=3;
                    fm.beginTransaction().add(R.id.container,fragment_profile,"profile").commit();
                    active = fragment_profile;
                    return;

                case R.id.dev:
                    for(Fragment f:fm.getFragments())
                        fm.beginTransaction().remove(f).commit();
                    currfrag=4;
                    fm.beginTransaction().add(R.id.container,fragment_dev,"dev").commit();
                    active = fragment_dev;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // Register GyroscopeObserver.
        gyroscopeObserver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister GyroscopeObserver.
        gyroscopeObserver.unregister();
    }

    @Override
    public void onSwipe(int direction) {

        /*for(Fragment f:fm.getFragments())
            fm.beginTransaction().remove(f).commit();
        */
        switch (direction) {


            case SimpleGestureFilter.SWIPE_RIGHT:
                currfrag=(currfrag+4)%5;
                navigation.setItemSelected(layout_id[currfrag], true);
                break;

            case SimpleGestureFilter.SWIPE_LEFT:
                currfrag=(currfrag+1)%5;
                navigation.setItemSelected(layout_id[currfrag], true);
                break;

            case SimpleGestureFilter.SWIPE_UP:
                    navigation.setVisibility(View.GONE);
                break;

            case SimpleGestureFilter.SWIPE_DOWN:
                    navigation.setVisibility(View.VISIBLE);
                break;

        }


        /*if(currfrag==0)
        {
            fm.beginTransaction().add(R.id.container,fragment_home,"home").commit();
            active = fragment_home;


        }
        else if(currfrag==1)
        {
            fm.beginTransaction().add(R.id.container,fragment_feed,"feed").commit();
            active=fragment_feed;
        }
        else if(currfrag==2)
        {
            fm.beginTransaction().add(R.id.container,fragment_profile,"profile").commit();
            active=fragment_profile;
        }
        else
        {
            fm.beginTransaction().add(R.id.container,fragment_dev,"dev").commit();
            active=fragment_dev;
        }
        navigation.setItemSelected(layout_id[currfrag], true);

        */



    }

    @Override
    public void onDoubleTap() {

        //Toast.makeText(getApplicationContext(),"Double Tap on screen",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void setupWindowAnimations() {
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setEnterTransition(fade);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigation.setItemSelected(navigation.getSelectedItemId(), true);
    }

}

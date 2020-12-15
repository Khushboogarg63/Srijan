package org.srijaniitism.srijan.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import org.srijaniitism.srijan.EventsView.EventsActivity;
import org.srijaniitism.srijan.EventsView.EventsActivity2;
import org.srijaniitism.srijan.EventsView.EventsActivity3;
import org.srijaniitism.srijan.EventsView.InformalActivity;


import org.srijaniitism.srijan.R;

import com.sanojpunchihewa.glowbutton.GlowButton;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {
    GlowButton day1, day2, day3, informals;

    ImageView logo;
    public static String FACEBOOK_URL = "https://www.facebook.com/ism.srijan";
    public static String FACEBOOK_PAGE_ID = "ism.srijan";

    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        TypeWriter tw = (TypeWriter) view.findViewById(R.id.content);
//        String s=getString(R.string.abouttext);
//        tw.setText(" ");
//        tw.setCharacterDelay(1);
//        tw.animateText(s);
//        tw.setMovementMethod(new ScrollingMovementMethod());
        final PrettyDialog dialog = new PrettyDialog(getContext());

        dialog.setTitle("About")
                .setMessage("The largest socio-cultural fest of its kind in eastern India, SRIJAN, is the platform where the most creative minds of the nation come together and battle it out in the fields of dance, music, art, dramatics, literary arts, fine arts and much more. With a footfall of more than 15000 every year, it is a place where the fine skills and hard work of every participant gets what it deserves - recognition, appreciation and inspiration. And that’s not all! Every night of this three-day spectacular extravaganza is a star-studded one where we have renowned performers from our country making you let go of yourself and inspire you to achieve the impossible. SRIJAN’20 is all set to raise the standards set by its previous editions and make these three nights an unforgettable one and add on to your caravan of memories.")
                .setIcon(R.drawable.ic_reply_black_24dp,
                        R.color.pdlg_color_green,
                        new PrettyDialogCallback() {   // icon OnClick listener
                            @Override
                            public void onClick() {
                                // Do what you gotta do
                                dialog.dismiss();
                            }
                        });

        logo = view.findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        day1 = view.findViewById(R.id.day1);
        day2 = view.findViewById(R.id.day2);
        day3 = view.findViewById(R.id.day3);
        informals = view.findViewById(R.id.informals);

//        fb = view.findViewById(R.id.fb);
//        insta = view.findViewById(R.id.insta);
        final Animation anim;
        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_out);
        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EventsActivity.class));
                day1.startAnimation(anim);
            }
        });
        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EventsActivity2.class));
                day2.startAnimation(anim);
            }
        });
        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EventsActivity3.class));
                day3.startAnimation(anim);
            }
        });

        informals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InformalActivity.class));
                informals.startAnimation(anim);
            }
        });
        ImageView insta=view.findViewById(R.id.insta);
        ImageView fb=view.findViewById(R.id.fb);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("http://instagram.com/_u/srijaniitism");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/srijaniitism")));
                }
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/ism.srijan/";
                Uri uriUrl = Uri.parse(url);
                //Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                PackageManager packageManager = getContext().getPackageManager();
                String a = "";
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        a =  "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    } else { //older versions of fb app
                        a =  "fb://page/" + FACEBOOK_PAGE_ID;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    a =  FACEBOOK_URL; //normal web url
                }
                Intent intent;
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(a));
                } catch (Exception e) {
                    intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(a));
                }
                startActivity(intent);
            }
        });


        return view;
    }

}
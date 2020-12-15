package org.srijaniitism.srijan.EventsView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.devs.readmoreoption.ReadMoreOption;
import org.srijaniitism.srijan.AlertDialogManager;
import org.srijaniitism.srijan.ConnectionDetector;
import org.srijaniitism.srijan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.HashMap;
import java.util.Map;



import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class SingleEventActivity extends Activity {
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();

    private static final String TAG_NAME = "Name";
    private static final String TAG_ABOUT = "About";
    private static final String TAG_RULES = "Rules";
    private static final String TAG_TIMINGS = "Dates";
    private static final String TAG_JUDGING = "Judges";
    private static final String TAG_PRIZES = "Prizes";
    private static final String TAG_CONTACTS = "Contacts";
    private static final String TAG_VENUE = "Venue";
    private static final String TAG_WIN = "Winners";
    ReadMoreOption readMoreOption;
    TextView name, about1, rules1, timing, contact, prizes, venue, timinghead, venuehead;
    ImageView venueimg;
    Button register;
    DatabaseReference events;
    FirebaseUser user;
    KonfettiView konfettiView;
    Animation anim;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);

        cd = new ConnectionDetector(getApplicationContext());

        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
//            alert.showAlertDialog(SingleEventActivity.this, "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            // stop executing code by return
            finishAndRemoveTask();
            return;
        }

        prefs = getSharedPreferences("RegisteredEvents", Context.MODE_PRIVATE);
        name = findViewById(R.id.event_title);
        about1 = findViewById(R.id.about1);
        rules1 = findViewById(R.id.rules1);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            about1.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            rules1.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        timinghead = findViewById(R.id.timinghead);
        timing = findViewById(R.id.timing);
        venuehead = findViewById(R.id.venuehead);
        venue = findViewById(R.id.venue);
        venueimg = findViewById(R.id.venueimg);
        contact = findViewById(R.id.contact);
        prizes = findViewById(R.id.prizes);
        register = findViewById(R.id.register);

        readMoreOption = new ReadMoreOption.Builder(this)
                .textLength(3, ReadMoreOption.TYPE_LINE) // OR
                //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
                .moreLabel("MORE")
                .lessLabel("LESS")
                .moreLabelColor(Color.RED)
                .lessLabelColor(Color.GREEN)
                .labelUnderLine(true)
                .expandAnimation(true)
                .build();

        final Intent i = getIntent();
        name.setText(i.getStringExtra(TAG_NAME));
        readMoreOption.addReadMoreTo(about1, i.getStringExtra(TAG_ABOUT));
        readMoreOption.addReadMoreTo(rules1, i.getStringExtra(TAG_RULES));
        timing.setText(i.getStringExtra(TAG_TIMINGS));
        venue.setText(i.getStringExtra(TAG_VENUE));
        contact.setText(i.getStringExtra(TAG_CONTACTS));
        prizes.setText(i.getStringExtra(TAG_PRIZES));

        final Map<String,String> mapi=new HashMap<>();
        mapi.put("NLHC","NLHC");
        mapi.put("NLHC DRAWING HALL","NLHC");
        mapi.put("SAC","SAC");
        mapi.put("SAC GROUND","SAC");
        mapi.put("PENMAN ACOUSTIC","PENMAN");
        mapi.put("PENMAN","PENMAN");
        mapi.put("PENMAN QUAD","PENMAN");
        mapi.put("DIAMOND PARKING","DIAMOND PARKING");
        mapi.put("OVAL GARDEN","OVAL GARDEN");
        mapi.put("GJLT","GJLT");
        mapi.put("MANAGEMENT HALL","MANAGEMENT HALL");

        final String x=i.getStringExtra(TAG_VENUE);
        final LinearLayout compass= findViewById(R.id.compass);
        compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    compass.startAnimation(anim);

                    if(x.equals("ONLINE"))
                    {
                        Toast.makeText(getApplicationContext(),"Visit facebook page for more information.",Toast.LENGTH_SHORT).show();
                    }
                    else if (mapi.containsKey(x)) {
                        String s =  "http://maps.google.co.in/maps?q=" + "IIT DHANBAD "+mapi.get(x);
                        Log.e("string", s);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(s));
                        startActivity(intent);
                    }
                    else {
                        String mapII = "http://maps.google.co.in/maps?q=" + "IIT DHANBAD "+x;
                        Log.e("string", mapII);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(mapII));
                        startActivity(intent);
                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Invalid location", Toast.LENGTH_SHORT).show();
                }
            }
        });


        PushDownAnim.setPushDownAnimTo(register).setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleEventActivity.this);
                builder.setMessage("Do you want to register?")
                        .setPositiveButton("Yaye🤘", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                events = FirebaseDatabase.getInstance().getReference("Events").child(i.getStringExtra(TAG_NAME));
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                events.child(user.getUid()).setValue(user.getDisplayName());

                                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Events");
                                database.child(i.getStringExtra(TAG_NAME)).setValue("Registered");
                                Toast.makeText(SingleEventActivity.this,"Registered",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Nah🙁", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        }).show();
            }

        } );

        String temp = i.getStringExtra(TAG_WIN);
        Log.d("winner", temp);
        if (!temp.equals("null")) {
            timinghead.setVisibility(View.GONE);
            timing.setVisibility(View.GONE);
            venuehead.setText("Winners");
            venue.setText(i.getStringExtra(TAG_WIN));
            venue.setTextSize(25);
            venueimg.setVisibility(View.GONE);


            konfettiView = findViewById(R.id.viewKonfetti);
            konfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5f))
                    .setPosition(-50f, 3000f, -50f, -50f)
                    .streamFor(300, 5000L);
        }
    }
}
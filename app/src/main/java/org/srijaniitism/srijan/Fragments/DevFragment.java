package org.srijaniitism.srijan.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.srijaniitism.srijan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DevFragment extends Fragment {
    FloatingActionButton fab1;
    public DevFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dev, container, false);
//        fab1 = view.findViewById(R.id.fab_1);
//        fab1.setVisibility(View.GONE);

        TextView tv1,tv2,tv3;
        tv1 = view.findViewById(R.id.tv1);
        tv1.setMovementMethod(LinkMovementMethod.getInstance());

        tv2 = view.findViewById(R.id.tv2);
        tv2.setMovementMethod(LinkMovementMethod.getInstance());

        tv3 = view.findViewById(R.id.tv3);
        tv3.setMovementMethod(LinkMovementMethod.getInstance());

        WebView w = (WebView) view.findViewById(R.id.web);

        // loading http://www.google.com url in the the WebView.
        w.loadUrl("https://www.srijaniitism.org/heads/");

        // this will enable the javascipt.
        w.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        w.setWebViewClient(new WebViewClient());

        Button b=view.findViewById(R.id.bugreport);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mailto = "mailto:ashwanikr07041999@gmail.com" +
                        "?cc=" + "pathak0311@gmail.com" +
                        "&subject=" + Uri.encode("Bug Report") +
                        "&body=" + Uri.encode("");

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(),"No email app found", Toast.LENGTH_SHORT).show();

                }
            }
        });




        return view;
    }
}

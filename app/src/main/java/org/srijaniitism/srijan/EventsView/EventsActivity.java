package org.srijaniitism.srijan.EventsView;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.srijaniitism.srijan.AlertDialogManager;
import org.srijaniitism.srijan.ConnectionDetector;
import org.srijaniitism.srijan.GIFView;
import org.srijaniitism.srijan.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class EventsActivity extends ListActivity {
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();

    // Progress Dialog
    AVLoadingIndicatorView avi;
    ListView lv;
    LinearLayout linearLayout;
    GIFView imageView;



    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> eventsList;


    JSONArray events = null;
    JSONObject jsonObject;


    String event_name, event_timing;
    private static final String TAG_NAME = "Name";
    private static final String TAG_ABOUT = "About";
    private static final String TAG_RULES = "Rules";
    private static final String TAG_TIMINGS = "Dates";
    private static final String TAG_JUDGING = "Judges";
    private static final String TAG_PRIZES = "Prizes";
    private static final String TAG_CONTACTS = "Contacts";
    private static final String TAG_VENUE = "Venue";
    private static final String TAG_WIN = "Winners";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getWindow().setStatusBarColor(Color.BLACK);

        imageView = findViewById(R.id.loader);

        cd = new ConnectionDetector(getApplicationContext());


        if (!cd.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            // stop executing code by return
            finishAndRemoveTask();
            return;
        }

        linearLayout = findViewById(R.id.activity);



        eventsList = new ArrayList<HashMap<String, String>>();
                new LoadEvents(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                jsonObject = object;
                //new LoadEvents().execute();
            }
        }).execute("https://spreadsheets.google.com/tq?key=");

        // Loading tracks in Background Thread

        // get listview
        lv = getListView();


        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                // On selecting single track get song information
                Intent i = new Intent(getApplicationContext(), SingleEventActivity.class);


                HashMap<String, String> event_map = eventsList.get(position);
                //Log.d("qwerty", event_map.get(TAG_NAME));
                i.putExtra(TAG_NAME, event_map.get(TAG_NAME));
                i.putExtra(TAG_ABOUT, event_map.get(TAG_ABOUT));
                i.putExtra(TAG_CONTACTS, event_map.get(TAG_CONTACTS));
                i.putExtra(TAG_JUDGING, event_map.get(TAG_JUDGING));
                i.putExtra(TAG_PRIZES, event_map.get(TAG_PRIZES));
                i.putExtra(TAG_RULES, event_map.get(TAG_RULES));
                i.putExtra(TAG_VENUE, event_map.get(TAG_VENUE));
                i.putExtra(TAG_TIMINGS, event_map.get(TAG_TIMINGS));
                i.putExtra(TAG_WIN, event_map.get(TAG_WIN));

                //i.putExtra("song_id", song_id);

                startActivity(i);
            }
        });
    }

    class LoadEvents extends AsyncTask<String, String, String> {
        AsyncResult callback;
        public LoadEvents(AsyncResult callback) {
            this.callback = callback;
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //avi.show();
            imageView.setVisibility(View.VISIBLE);
            linearLayout.setBackgroundColor(Color.BLACK);
        }

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to download the requested page.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            pDialog.dismiss();
            //avi.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            linearLayout.setBackground(getDrawable(R.drawable.eventsbg));
            // remove the unnecessary parts from the response and construct a JSON
            int start = result.indexOf("{", result.indexOf("{") + 1);
            int end = result.lastIndexOf("}");
            String jsonResponse = result.substring(start, end);
            try {
                JSONObject table = new JSONObject(jsonResponse);
                callback.onResult(table);
                updateUI();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private String downloadUrl(String urlString) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int responseCode = conn.getResponseCode();
                is = conn.getInputStream();

                String contentAsString = convertStreamToString(is);
                return contentAsString;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        private void updateUI(){
            try {
                JSONArray rows = jsonObject.getJSONArray("rows");
                Log.d("rows", rows.length() + "");
                //Log.d("paridhan", rows.getJSONObject(13).getJSONArray("c").getJSONObject(0).getString("v"));

                for (int r = 0; r < rows.length(); r++) {
                    Log.d("valuer", r + "");
                    JSONObject row = rows.getJSONObject(r);
                    JSONArray columns = row.getJSONArray("c");

                    //int position = columns.getJSONObject(0).getString("v");

                    String name = columns.getJSONObject(0).getString("v");
                    String about = columns.getJSONObject(1).getString("v");
                    String rules = columns.getJSONObject(2).getString("v");
                    String timings = columns.getJSONObject(3).getString("v");
                    String judging = columns.getJSONObject(4).getString("v");
                    String prizes = columns.getJSONObject(5).getString("v");
                    String contacts = columns.getJSONObject(6).getString("v");
                    String venue = columns.getJSONObject(7).getString("v");
                    String winners = columns.getJSONObject(8).getString("v");
                    Log.d("Name", name);
                    Log.d("winners", winners);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_NAME, name);
                    map.put(TAG_ABOUT, about);
                    map.put(TAG_RULES, rules);
                    map.put(TAG_TIMINGS, timings);
                    map.put(TAG_JUDGING, judging);
                    map.put(TAG_PRIZES, prizes);
                    map.put(TAG_CONTACTS, contacts);
                    map.put(TAG_VENUE, venue);
                    map.put(TAG_WIN, winners);

                    eventsList.add(map);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListAdapter adapter = new SimpleAdapter(
                    EventsActivity.this, eventsList,
                    R.layout.list_item_events, new String[] {TAG_NAME, TAG_TIMINGS, TAG_VENUE }, new int[] {
                    R.id.event_name, R.id.event_timing, R.id.venue});
            // updating listview
            setListAdapter(adapter);

            // Change Activity Title with Album name
            setTitle("DAY 1");

        }

    }
}

package org.srijaniitism.srijan.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.srijaniitism.srijan.AlertDialogManager;
import org.srijaniitism.srijan.ConnectionDetector;
import org.srijaniitism.srijan.FBFeed.CustomAdapter;
import org.srijaniitism.srijan.FBFeed.DataModel;
import org.srijaniitism.srijan.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    TextView internet;
    FloatingActionButton fab1;
    AccessToken accessToken;
    AVLoadingIndicatorView avi;

    String token = "";

    ArrayList<HashMap<String, String>> feedList;
    ListView listView;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_feed, container, false);
        accessToken = new AccessToken(token, "", "", null, null, null, null, null, null, null);

        //final ImageView img = view.findViewById(R.id.temp);
        feedList = new ArrayList<HashMap<String, String>>();
        //listView = (ListView) view.findViewById(R.id.list);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
//        fab1 = view.findViewById(R.id.fab_1);
//        fab1.setVisibility(View.GONE);
        avi = view.findViewById(R.id.avi);
        internet = view.findViewById(R.id.internet);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();

        cd = new ConnectionDetector(getApplicationContext());

        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            avi.setVisibility(View.GONE);
            internet.setVisibility(View.VISIBLE);
//            alert.showAlertDialog(getActivity(), "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
            // stop executing code by return
            //finishAndRemoveTask();
            return view;
        }

        //final ListView lv = view.findViewById(R.id.listView);

        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/ism.srijan/feed?fields=full_picture,message,attachments{media_type}",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d("response", response.getRawResponse());
                        JSONObject jsonObject = response.getJSONObject();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject obj;
                        try {
                            jsonArray = jsonObject.getJSONArray("data");
                            //Log.d("face", jsonArray.length() + " ");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                obj = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                //map.put("Image", obj.getString("full_picture"));
                                //Log.d("mssg", jsonArray.getJSONObject(i).optString("message"));
                                //map.put("Message", obj.getString("message"));
                                Log.d("tempo", obj.getJSONObject("attachments").getJSONArray("data").getJSONObject(0).optString("media_type"));
                                if (obj.getJSONObject("attachments").getJSONArray("data").getJSONObject(0).optString("media_type").equals("photo")) {
                                    data.add(new DataModel(obj.optString("message"), obj.optString("full_picture")));
                                }
                                //feedList.add(map);
                                //Log.d("message", obj.getString("message"));
                                /*ListAdapter adapter = new SimpleAdapter(getContext(), feedList,
                                        R.layout.cards_layout, new String[] {"Message", "Image"}, new int[] {
                                        R.id.textViewName, R.id.imageView});
                                listView.setAdapter(adapter);*/
                            }


                            adapter = new CustomAdapter(data);
                            avi.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();
//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        //Log.d("here", "what up");
        request.executeAsync();
        return view;
    }
}
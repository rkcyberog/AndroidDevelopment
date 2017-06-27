package com.example.hp_06.foregroundmediaplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String url = "http://api.soundcloud.com/users/eminem/tracks?client_id=e8cbc9cd5537a4b122a342278dd857a7";
    private ProgressDialog pDialog;
    private List<Track> mListItems;
    private ImageView artistPic;
    private static final String TAG = MainActivity.class.getSimpleName();
    private APAdapter mAdapter;
    private TextView mSelectedTrackTitle;
    private TextView artistName;
    private ImageView mSelectedTrackImage;
    private MediaPlayer mMediaPlayer;
    public ImageView mPlayerControl;
    private TextView description;
    private ForegroundService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListItems = new ArrayList<Track>();
        ListView listView = (ListView) findViewById(R.id.track_list_view);
        mAdapter = new APAdapter(this, mListItems);
        listView.setAdapter(mAdapter);

        mSelectedTrackTitle = (TextView) findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView) findViewById(R.id.selected_track_image);
        mPlayerControl = (ImageView) findViewById(R.id.player_control);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Track track = mListItems.get(position);
                Intent service = new Intent(MainActivity.this, ForegroundService.class);
                service.putExtra(Constants.ACTION.Title, track.getTitle());
                service.putExtra(Constants.ACTION.StreamUrl, track.getStreamUrl());
                service.putExtra(Constants.ACTION.ArtWorkUrl, track.getArtworkURL());
                service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                ForegroundService.IS_SERVICE_RUNNING = true;
                mPlayerControl.setImageResource(R.drawable.ic_pause);
                startService(service);
                mSelectedTrackTitle.setText(track.getTitle());
                Picasso.with(MainActivity.this).load(track.getArtworkURL()).into(mSelectedTrackImage);
                // mPlayerControl.setImageResource(R.drawable.ic_play);
                mPlayerControl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int icon = ForegroundService.togglePlayPause();
                        if (icon == 1)
                            mPlayerControl.setImageResource(R.drawable.ic_play);
                        else if (icon == 0)
                            mPlayerControl.setImageResource(R.drawable.ic_pause);
                    }


                });
            }

        });


        JsonArrayRequest trackReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Track track = new Track();
                                track.setTitle(obj.getString("title"));

                                track.setStreamUrl(obj.getString("stream_url"));
                                track.setArtworkURL(obj.getString("artwork_url"));

                                //  JSONObject userpic =obj.getJSONObject("user");

                                mListItems.add(track);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });


        AppController.getInstance().addToRequestQueue(trackReq);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);
                finish();
            return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String artwrk = intent.getStringExtra(Constants.ACTION.ArtWorkUrl);
        String title = intent.getStringExtra(Constants.ACTION.Title);
        mSelectedTrackTitle.setText(title);
        Picasso.with(MainActivity.this).load(artwrk).into(mSelectedTrackImage);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}


package com.client.instagram.josh.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotoActivity extends ActionBarActivity {

    public static final String CLIENT_ID="c9406341e98842bab1fae633550da314";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        // SENT OUT API REQUEST to POPULAR PHOTOS
        photos = new ArrayList<>();
        // 1. Create the adapter linking it to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // 2. Find the ListView from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // 3. Set the adapter binding it the ListView
        lvPhotos.setAdapter(aPhotos);
        // 4. Fetch the popular photos
        fetchPopularPhotos();

    }

    // Trigger API request
    public void fetchPopularPhotos() {
/*
  - Popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
    - CLIENT ID       c9406341e98842bab1fae633550da314

 */
        String url = "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
        // Create network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess (worked, 2000)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Expecting a JSON object
             /*
             - Response:
             - Type: { "data" => [X] => "type" } ("image" or "vedio")
             - URL: { "data" => [X] => "images" => "standard_resolution" => "url" }
             - Iterate each of the photo items and decode the item into a java object
             */
                // Log.i("DEBUG", response.toString());
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); // array of photo
                    // iterate array of photos
                    for (int i = 0; i < photosJSON.length(); i++) {
                        // get the json object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        // decode the attributes of the json into a data object
                        InstagramPhoto photo = new InstagramPhoto();
                        // - Author Name: { "data" => [X] => "user" => "username" }
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.profile_picture = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.create_time = photoJSON.getInt("created_time");
                        // - Caption: { "data" => [X] => "caption" => "text" }

                        if (!photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }

                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // Height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        // likes count
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photos.add(photo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // callback
                aPhotos.notifyDataSetChanged();
            }

            // onfailure (fail)
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                // Do SOMETHING
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

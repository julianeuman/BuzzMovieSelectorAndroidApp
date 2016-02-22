package edu.gatech.cs2340.buzzmovieselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewDVDs extends AppCompatActivity {

    private RequestQueue queue;

    /**
     * hold whatever result comes back from the REST call.  Not required in this case really, but comes
     * in handy for debugging.
     */
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_item);

        //create the queue.  You should only have one of these in your entire application, so you might
        //need to create a singleton to hold it if you are making REST requests throughout the app.
        queue = Volley.newRequestQueue(this);
    }

    /**
     *
     * @param view app view
     */
    public void onGetStateCodePress(View view) {

        //this is the URL for our REST service
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?page_limit=16&page=1&country=us&apikey=yedukp76ffytfuy24zsqk7f5";
        /*
            We expect to get back a JSON response.  Volley also has String responses.
            This is an async call, but all the threading is handled for us in the background
            We just need 2 callback functions.
                onResponse = this is called when the response actually comes back from server
                onError = this is called if there is any error in the response
         */
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject resp) {
                        //handle a valid response coming back.  Getting this string mainly for debug
                        response = resp.toString();
                        //printing first 500 chars of the response.  Only want to do this for debug
//                        TextView view = (TextView) findViewById(R.id.movieTitle);
//                        view.setText(response.substring(0, 500));


                        //Now we parse the information.  Looking at the format, everything encapsulated in RestResponse object
                        JSONArray obj1 = null;
                        try {
                            obj1 = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        assert obj1 != null;





//
                        ArrayList<Movie> movies = new ArrayList<>();
                        for(int i=0; i < obj1.length(); i++) {

                            try {
                                //for each array element, we have to create an object
                                JSONObject jsonObject = obj1.getJSONObject(i);
                                Movie m = new Movie();

                                assert jsonObject != null;
                                m.setMovieName(jsonObject.optString("title"));
                                m.setMovieMpaRating(jsonObject.optString("mpaa_rating"));
                                m.setMovieYear(jsonObject.optString("year"));
                                m.setMovieLength(jsonObject.optString("runtime"));

////                                //save the object for later
                                movies.add(m);
////
////
                            } catch (JSONException e) {
                                Log.d("VolleyApp", "Failed to get JSON object");
                                e.printStackTrace();
                            }
                        }
                        //once we have all data, then go to list screen
                        changeView(movies);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        response = "JSon Request Failed!!";
                        //show error on phone
                        TextView view = (TextView) findViewById(R.id.movieTitle);
                        view.setText(response);
                    }
                });
        //this actually queues up the async response with Volley
        queue.add(jsObjRequest);
    }

    /**
     * This method changes to our new list view of the states, but we have to pass the
     * state array into the intent so the new screen gets the data.
     *
     * @param movies the list of state objects we created from the JSon response
     */
    private void changeView(ArrayList<Movie> movies) {
        Intent intent = new Intent(NewDVDs.this, ItemListActivity.class);
        //this is where we save the info.  note the State object must be Serializable
        intent.putExtra("movies", movies);
        startActivity(intent);
    }

}

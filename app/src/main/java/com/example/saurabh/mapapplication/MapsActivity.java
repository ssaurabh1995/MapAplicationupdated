package com.example.saurabh.mapapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        volley();
        Gotolocation(18.9558741, 72.8390562, 11);
        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.json_layout, null);
                    TextView header = (TextView) v.findViewById(R.id.header);
                    TextView description = (TextView) v.findViewById(R.id.description);
                    ImageView image = (ImageView) v.findViewById(R.id.image);
                    header.setText(marker.getTitle());
                    String snippet = marker.getSnippet();
                    description.setText(snippet.substring(0, snippet.indexOf("#")));
                    Picasso.with(MapsActivity.this).load(snippet.substring(snippet.indexOf("#")+1, snippet.length())).into(image);
                    return v;
                }

            });

        }
    }

    private void volley() {
        StringRequest request=new StringRequest(Request.Method.GET, "http://dev.citrans.net:8888/skymeet/poi/list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray arr= null;
                        try {
                            arr = new JSONArray(response);
                            Parcejason(arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void Parcejason(JSONArray arr) throws JSONException {
        for(int i=0;i<arr.length();i++) {
            JSONObject obj = arr.getJSONObject(i);
            String id=obj.getString("id");
            String title=obj.getString("title");
            String description=obj.getString("description");
            String url=obj.getString("imageUrl");
            mMap.addMarker(new MarkerOptions()
            .title(title)
            .snippet(description + "#" + url).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_android_black_24dp))
            .position(new LatLng(obj.getJSONArray("location").getDouble(0),obj.getJSONArray("location").getDouble(1)))
            );
        }


    }

    private void Gotolocation(double lat,double lng,float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.animateCamera(cameraUpdate);
    }
}





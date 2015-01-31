package com.example.environmentwatch;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

	private GoogleMap map;
	private MarkerData[] points = {new MarkerData(new LatLng(34.413963, -119.848947)),
			new MarkerData(new LatLng(34.413329, -119.860972)),
			new MarkerData(new LatLng(34.411516, -119.844768))};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.show();
        System.out.println("begin");
        
        createMap();
        plotPoints();
    }
    
    public void createMap() {
    	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        
        
//        Marker marker = map.addMarker(new MarkerOptions()
//        .position(new LatLng(34.413963, -119.848947))
//        .title("hi"));
        
        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
        	
        	private boolean show = true;
        	
        	@Override
        	public void onInfoWindowClick(Marker marker) {
        		if (show){
        			map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        // Use default InfoWindow frame
                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        // Defines the contents of the InfoWindow
                        @Override
                        public View getInfoContents(Marker arg0) {

                            // Getting view from the layout file info_window_layout
                            View v = getLayoutInflater().inflate(R.layout.info_window, null);
                            
                            ImageView mImg = (ImageView) findViewById(R.id.imageView1);
                            
                            Bitmap b = getBitmapFromMarker(arg0);
                            if(b != null){
                            	mImg.setImageBitmap(getBitmapFromMarker(arg0));
                            }

                            // Returning the view containing InfoWindow contents
                            return v;

                        }
                    });
        		}
        		else {
        			map.setInfoWindowAdapter(null);
        		}
        		
        		show = !show;
        		marker.showInfoWindow();
        	}
        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    		case R.id.activity_camera:
    			Intent intent1 = new Intent(this,Camera.class);
    			startActivity(intent1);
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    	
    }
    
    public void plotPoints() {
    	for (MarkerData p : points){
    		Marker mark = map.addMarker(new MarkerOptions()
            .position(p.getLatLng())
            .title(p.getComments()));
    		
    		p.setMarker(mark);
    	}
    }
    
    //TODO: get location, comments, image from interwebs to make markerdata object and save in 'points' array
    public void setPoints() {

    }
    
    public Bitmap getBitmapFromMarker(Marker m) {
    	for (MarkerData p : points){
    		if(m == p.getMarker()){
    			return p.getImage();
    		}
    	}
    	return null;
    }

}

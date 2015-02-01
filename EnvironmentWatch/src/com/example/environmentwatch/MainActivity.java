package com.example.environmentwatch;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener{

	private GoogleMap map;
	private MarkerData[] points = {new MarkerData(new LatLng(34.413963, -119.848947)),
			new MarkerData(new LatLng(34.413329, -119.860972)),
			new MarkerData(new LatLng(34.411516, -119.844768))};
	
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	public static TextView mLatitudeText;
	public static TextView mLongitudeText;
	ArrayList<Float> latitudeList,longitudeList;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.show();
        
        mLatitudeText = (TextView)findViewById(R.id.textView1);
        mLongitudeText = (TextView)findViewById(R.id.textView2);

        latitudeList = new ArrayList<Float>();
        longitudeList = new ArrayList<Float>();
        
        createMap();
        plotPoints();
        buildGoogleApiClient();
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
        mGoogleApiClient.connect();
    }
    public void createMap() {
    	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        
        
//        Marker marker = map.addMarker(new MarkerOptions()
//        .position(new LatLng(34.413963, -119.848947))
//        .title("hi"));
        
        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
        	
        	private boolean show = true;
        	
        	public void onInfoWindowClick(Marker marker) {
        		if (show){
        			map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        // Use default InfoWindow frame
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        // Defines the contents of the InfoWindow
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
    			intent1.putExtra("latitude",mLatitudeText.getText().toString());
    			intent1.putExtra("longitude",mLongitudeText.getText().toString());
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
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}
	public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}
	
public void getPictures() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Snap");
		query.orderByAscending("createdAt");

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (objects != null && e == null) {				
					//files = objects.getParseFile("imageFile");
					
					
					
					try {
						byte [] bytesarray;
						for(int i = 0;i<objects.size();i++)
						{
							bytesarray = objects.get(i).getParseFile("latitude").getData();
							latitudeList.add(ByteBuffer.wrap(bytesarray).order(ByteOrder.LITTLE_ENDIAN).getFloat());
							bytesarray = objects.get(i).getParseFile("longitude").getData();
							longitudeList.add(ByteBuffer.wrap(bytesarray).order(ByteOrder.LITTLE_ENDIAN).getFloat());
						}
						/*
						byte[] bytes = file.getData();
						Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
						Matrix rotationMatrix = new Matrix();
						rotationMatrix.postRotate(0);
						Bitmap rotatedScaledSnapImageBitmap = Bitmap.createBitmap(bmp, 0, 0, 
								bmp.getWidth(), bmp.getHeight(), rotationMatrix, true);
						object.saveInBackground();*/

					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					// alert the user that there are no more images to read!

					return;
					// something went wrong
				}
			}
		});
		
		
	}

}

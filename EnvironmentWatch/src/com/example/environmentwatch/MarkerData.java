package com.example.environmentwatch;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerData {
	private LatLng coord;
	private String comments;
	private Bitmap image;
	private Marker marker;
	
	public MarkerData(LatLng coord, String comments, Bitmap image) {
		this.coord = coord;
		this.comments = comments;
		this.image = image;
	}
	
	public MarkerData(LatLng coord) {
		this(coord, coord.toString(), null);
	}
	
	public MarkerData(LatLng coord, Bitmap image) {
		this(coord, coord.toString(), image);
	}
	
	public LatLng getLatLng() {
		return coord;
	}
	
	public String getComments() {
		return comments;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public void setMarker(Marker marker){
		this.marker = marker;
	}
	
	public Marker getMarker() {
		return marker;
	}
	
	public String toString() {
		String s = "";
		s += "LatLng: " + getLatLng() + "\n";
		s += "Comment: " + getComments() + "\n";
		if(getImage() != null){
			s += "Image: " + getImage() + "\n";
		}
		if(getMarker() != null){
			s += "Marker: " + getMarker() + "\n";
		}
		return s;
	}
}

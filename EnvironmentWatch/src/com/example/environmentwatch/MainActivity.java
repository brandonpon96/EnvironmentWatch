package com.example.environmentwatch;


import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.show();
        System.out.println("begin");
        
        
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
    
}

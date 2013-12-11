package cmu.hci.maintenaid;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class ViewMapIncompleteRequestsBuildingsActivity extends MapActivity {
	
 private MapView map;
 private MyLocationOverlay me;
 private MyItemizedOverlay overlay;
	
 @Override
 protected void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.view_map_requests_buildings);
    
//    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//    if(cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnectedOrConnecting())
//    {
//    	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
//    	 
//        // set the message to display
//        alertbox.setMessage("No Internet Available, Map Interface will not be usable");
//
//        // add a neutral button to the alert box and assign a click listener
//        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//
//            // click listener on the alert box
//            public void onClick(DialogInterface arg0, int arg1) {
//                // the button was clicked
//                finish();
//            }
//        });
//
//        // show it
//        alertbox.show();
//    }
    	
    
	map = (MapView) findViewById(R.id.mapview);
	MapController mapControl = map.getController();
	map.setBuiltInZoomControls(true);
	map.setClickable(true);
	
    me=new MyLocationOverlay(this, map);
    me.enableMyLocation();
    me.enableCompass();
	map.getOverlays().add(me);
	
	GeoPoint point = new GeoPoint ((int)(40.448725*1E6),(int)(-79.946006*1E6));
	mapControl.setZoom(16);
	mapControl.setCenter(point);
	
	overlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.marker), map, this);
		
	overlay.addOverlay(
			new OverlayItem(
					new GeoPoint ((int)(40.448725*1E6),(int)(-79.946006*1E6)),
					"Shady Oak"
					,"Press to view requests")
			);
	
	overlay.addOverlay(
			new OverlayItem(
					new GeoPoint ((int)(40.452923*1E6),(int)(-79.942682*1E6)),
					"Amberson"
					,"Press to view requests")
			);
	
	overlay.addOverlay(
			new OverlayItem(
					new GeoPoint ((int)(40.443504*1E6),(int)(-79.941571*1E6)),
					"Resnik"
					,"Press to view requests")
			);
	
	map.getOverlays().add(overlay);
	
    PopupPanel panel = new PopupPanel(R.layout.popup);
    panel.show(false);
    

  }
 
 @Override
 protected void onPause(){
	 super.onPause();
	 me.disableMyLocation();
	 me.disableCompass();
 }
 
 @Override
 protected void onResume(){
	 super.onResume();
	 me.enableCompass();
	 me.enableMyLocation();
 }
  
 public void startActivity(String buildingName) {
	 Intent intent = new Intent(this, ViewListIncompleteRequestsForBuildingActivity.class);
     intent.putExtra("building", buildingName);
	 startActivity(intent);
 }

  @Override
  protected boolean isRouteDisplayed() {
    return(false);
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_S) {
      map.setSatellite(!map.isSatellite());
      return(true);
    }
    else if (keyCode == KeyEvent.KEYCODE_Z) {
      map.displayZoomControls(true);
      return(true);
    }
    
    return(super.onKeyDown(keyCode, event));
  }

  class PopupPanel {
    View popup;
    boolean isVisible=false;
    
    PopupPanel(int layout) {
      ViewGroup parent=(ViewGroup)map.getParent();

      popup=getLayoutInflater().inflate(layout, parent, false);
      Button button = (Button)popup.findViewById(R.id.listbutton);
      button.setText("View List");
      button.setOnClickListener(new OnClickListener() {
    	  
    	  public void onClick(View v) {
    	  	finish();
    	  }
    });
                  
    }
    
    View getView() {
      return(popup);
    }
    
    void show(boolean alignTop) {
      RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
      );
      
      if (alignTop) {
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.setMargins(0, 20, 0, 0);
      }
      else {
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.setMargins(0, 0, 0, 60);
      }
      
      hide();
      
      ((ViewGroup)map.getParent()).addView(popup, lp);
      isVisible=true;
    }
    
    void hide() {
      if (isVisible) {
        isVisible=false;
        ((ViewGroup)popup.getParent()).removeView(popup);
      }
    }
  }
  
  
  private class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
		private ViewMapIncompleteRequestsBuildingsActivity viewMapIncompleteRequestsBuildingsActivity;

		public MyItemizedOverlay(Drawable defaultMarker, MapView mapView, ViewMapIncompleteRequestsBuildingsActivity mapTabView) {
			super(boundCenter(defaultMarker), mapView, mapView.getContext());
			this.viewMapIncompleteRequestsBuildingsActivity = mapTabView;
		}

		public void addOverlay(OverlayItem overlay) {
		    m_overlays.add(overlay);
		    populate();
			createAndDisplayBalloonOverlay(overlay);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return m_overlays.get(i);
		}

		@Override
		public int size() {
			return m_overlays.size();
		}

		@Override
		protected boolean onBalloonTap(int index, OverlayItem item) {
			viewMapIncompleteRequestsBuildingsActivity.startActivity(item.getTitle());
			return true;
		}
	}
}

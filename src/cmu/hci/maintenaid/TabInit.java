package cmu.hci.maintenaid;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabInit extends TabActivity
{
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabs);

	   
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ViewListIncompleteRequestsActivity.class);
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("incomplete").setIndicator("Incomplete",
	                      res.getDrawable(R.drawable.ic_incomplete))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, ViewListInProgressRequestsActivity.class);
	    spec = tabHost.newTabSpec("inprogress").setIndicator("In Progress",
	                      res.getDrawable(R.drawable.ic_inprogress))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    intent = new Intent().setClass(this, CompleteRequestsSearchFormActivity.class);
	    spec = tabHost.newTabSpec("complete").setIndicator("Complete",
	                      res.getDrawable(R.drawable.ic_complete))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    tabHost.setCurrentTab(0);
	}
}

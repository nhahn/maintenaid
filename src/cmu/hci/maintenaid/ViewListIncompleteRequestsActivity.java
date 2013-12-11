package cmu.hci.maintenaid;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ViewListIncompleteRequestsActivity extends ListActivity {
	private RequestsDbAdapter dbAdapter;
	private Cursor cursor;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_incomplete_requests);
        
        dbAdapter = new RequestsDbAdapter(this);
        dbAdapter.open();
        fillData();
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                onListItemClick(v,pos,id);
            }
        });
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		this, R.array.sorting_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

    }
    
    protected void onListItemClick(View v, int pos, long id) {
    	int myId = cursor.getInt(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestID));
    	String name = cursor.getString(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestName));
    	int priority = cursor.getInt(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestPriority));
    	String dateAdded = cursor.getString(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestDateAdded));
		String dateCompleted = cursor.getString(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestDateCompleted));
		String building = cursor.getString(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestBuilding));
		String apartment = cursor.getString(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestApartment));
		String details = cursor.getString(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestDetails));
		String comments = cursor.getString(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestComments));
		int status = cursor.getInt(cursor.getColumnIndex(RequestsDatabaseHelper.colRequestStatus));
        Intent i = new Intent(this, ViewRequestDetailsActivity.class);
        i.putExtra("id", myId);
        i.putExtra("name", name);
        i.putExtra("priority", priority);
        i.putExtra("dateAdded", dateAdded);
        i.putExtra("dateCompleted", dateCompleted);
        i.putExtra("building", building);
        i.putExtra("apartment", apartment);
        i.putExtra("details", details);
        i.putExtra("comments", comments);
        i.putExtra("status", status);
        startActivityForResult(i, 0);
    }
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {     
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
//    		onSortByPriorityButtonClick(null);
    	}
    }
    
    private void fillData() {
		// Clear out existing data
    	if(!dbAdapter.deleteAllRequests()) { // no rows were deleted
			Log.v("WARNING", "fillData() -- no rows were deleted.");
		}
		
		// Add data
    	dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-06", "2011-07-10", "Shady Oak",
    			"101", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-12", "2011-10-17",
    			"Amberson", "401", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-08", "2011-09-14",
    			"Resnik", "310", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Bulb Burned Out",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-16", "2011-05-19",
    			"Amberson", "905", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-03-17", "2011-03-20",
    			"Resnik", "701", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-11", "2011-10-19",
    			"Resnik", "405", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-07-11", "2011-07-16", "Resnik",
    			"906", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-10", "2011-08-17",
    			"Resnik", "315", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Bulb Burned Out",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-01-03", "2011-01-09",
    			"Amberson", "512", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-05-20", "2011-05-27", "Shady Oak", 
    			"812", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-20", "2011-05-25",
    			"Resnik", "414", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-06-02", "2011-06-05",
    			"Resnik", "312", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Bulb Burned Out",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-06", "2011-04-12",
    			"Amberson", "105", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-17", "2011-02-21", "Shady Oak", 
    			"413", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-16", "2011-05-18",
    			"Resnik", "211", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-16", "2011-02-17",
    			"Resnik", "412", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-12", "2011-02-19",
    			"Amberson", "106", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-07", "2011-02-11", "Shady Oak", 
    			"408", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-11", "2011-07-19",
    			"Resnik", "808", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-16", "2011-02-24", "Shady Oak", 
    			"612", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-14", "2011-07-16", "Shady Oak", 
    			"812", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-05", "2011-02-09",
    			"Resnik", "709", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-01", "2011-07-09",
    			"Amberson", "112", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-06-01", "2011-06-06", "Shady Oak", 
    			"305", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-08", "2011-04-10",
    			"Amberson", "311", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-15", "2011-04-21",
    			"Resnik", "407", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-02-19", "2011-02-21",
    			"Amberson", "502", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-12", "2011-04-18",
    			"Amberson", "512", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-09", "2011-08-10",
    			"Amberson", "712", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-14", "2011-02-21",
    			"Resnik", "213", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-11", "2011-08-13",
    			"Amberson", "908", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-06-05", "2011-06-10", "Shady Oak", 
    			"603", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-06", "2011-01-07",
    			"Resnik", "901", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Bulb Burned Out",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-06", "2011-05-09",
    			"Resnik", "805", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-06", "2011-10-07", "Shady Oak", 
    			"612", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-06", "2011-10-12", "Shady Oak", 
    			"709", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-20", "2011-04-25",
    			"Amberson", "407", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-03", "2011-09-04", "Shady Oak", "606", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-05-03", "2011-05-08",
    			"Amberson", "313", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-02-19", "2011-02-24", "Shady Oak", "411", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-08-14", "2011-08-22",
    			"Amberson", "515", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-02", "2011-09-06",
    			"Amberson", "210", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-18", "2011-01-23", "Shady Oak", "115", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-02", "2011-01-10", "Shady Oak", "204", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-08-10", "2011-08-14",
    			"Amberson", "907", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-12", "2011-09-15",
    			"Amberson", "301", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-20", "2011-05-21",
    			"Resnik", "810", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-12", "2011-10-14",
    			"Resnik", "812", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-01", "2011-09-03",
    			"Resnik", "501", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-06-06", "2011-06-10", "Shady Oak", "301", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-09-13", "2011-09-19", "Resnik",
    			"710", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-06", "2011-08-09",
    			"Resnik", "505", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-01-14", "2011-01-17", "Resnik",
    			"512", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-01", "2011-05-08",
    			"Resnik", "215", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-20", "2011-01-21", "Shady Oak", "411", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-07", "2011-10-13",
    			"Amberson", "507", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-04", "2011-09-11",
    			"Resnik", "502", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-11", "2011-10-18",
    			"Amberson", "314", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-03-01", "2011-03-07", "Resnik",
    			"315", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-02", "2011-07-04", "Shady Oak", "311", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Bulb Burned Out",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-02-11", "2011-02-19", "Shady Oak", "114", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-01-07", "2011-01-15", "Shady Oak", "812", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-06-14", "2011-06-16", "Shady Oak", "301", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-20", "2011-09-23",
    			"Resnik", "810", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-20", "2011-09-26",
    			"Resnik", "802", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-05-17", "2011-05-21", "Resnik",
    			"810", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-09", "2011-07-10", "Shady Oak", "911", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-15", "2011-08-19",
    			"Amberson", "914", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-03-09", "2011-03-15",
    			"Amberson", "411", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-08", "2011-10-16",
    			"Resnik", "811", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-03-18", "2011-03-19",
    			"Resnik", "501", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-03", "2011-01-09",
    			"Amberson", "905", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-16", "2011-05-23",
    			"Amberson", "511", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-18", "2011-02-24",
    			"Amberson", "812", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-14", "2011-10-20",
    			"Amberson", "311", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-11", "2011-10-18", "Shady Oak", "602", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-14", "2011-10-19",
    			"Resnik", "711", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-01", "2011-10-07",
    			"Resnik", "904", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-14", "2011-01-22",
    			"Resnik", "707", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-03-03", "2011-03-07",
    			"Amberson", "113", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-03-13", "2011-03-21",
    			"Amberson", "605", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-12", "2011-02-19", "Shady Oak", "501", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-07", "2011-07-14",
    			"Resnik", "311", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-05-01", "2011-05-08",
    			"Amberson", "815", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-07", "2011-05-10",
    			"Amberson", "309", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-11", "2011-09-19",
    			"Resnik", "604", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-05-06", "2011-05-14", "Shady Oak", "514", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-06-13", "2011-06-19", "Shady Oak", "715", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-05", "2011-09-10", "Shady Oak", "713", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-08-16", "2011-08-20",
    			"Amberson", "803", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-10-04", "2011-10-06", "Resnik",
    			"312", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-03", "2011-05-09",
    			"Amberson", "115", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-06", "2011-04-13",
    			"Resnik", "809", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-06-04", "2011-06-10",
    			"Resnik", "615", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-13", "2011-01-19",
    			"Resnik", "208", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-08-08", "2011-08-13", "Resnik",
    			"103", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-12", "2011-04-20",
    			"Resnik", "512", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-15", "2011-04-18",
    			"Amberson", "214", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-18", "2011-07-23", "Shady Oak", "908", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-09", "2011-04-17",
    			"Amberson", "410", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-18", "2011-04-21",
    			"Amberson", "314", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-03-16", "2011-03-19", "Shady Oak", "811", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-02", "2011-01-03", "Shady Oak", "902", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-05-08", "2011-05-12", "Shady Oak", "612", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-14", "2011-01-15",
    			"Amberson", "207", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-09", "2011-07-15", "Shady Oak", "115", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-09", "2011-09-15",
    			"Amberson", "309", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-17", "2011-07-18",
    			"Amberson", "108", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-08-06", "2011-08-08", "Resnik",
    			"609", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-03-14", "2011-03-20",
    			"Amberson", "613", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-03-19", "2011-03-21", "Resnik",
    			"906", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Space Heater Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-09", "2011-02-16",
    			"Amberson", "109", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-02", "2011-01-09",
    			"Resnik", "206", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-17", "2011-04-23",
    			"Amberson", "501", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-06-08", "2011-06-14",
    			"Resnik", "111", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-10", "2011-04-16",
    			"Resnik", "509", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-09-20", "2011-09-21", "Resnik",
    			"811", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-19", "2011-10-24",
    			"Resnik", "102", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-13", "2011-09-18",
    			"Amberson", "401", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-03-07", "2011-03-11", "Shady Oak", "601", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-20", "2011-02-25",
    			"Amberson", "604", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-18", "2011-05-20", "Shady Oak", "806", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-12", "2011-08-17",
    			"Resnik", "815", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-07-15", "2011-07-22", "Shady Oak", "301", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-07", "2011-05-11",
    			"Amberson", "602", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-03", "2011-01-05", "Shady Oak", "604", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-10", "2011-01-14",
    			"Amberson", "909", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-03-10", "2011-03-12", "Shady Oak", "513", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-10-15", "2011-10-19", "Resnik",
    			"115", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Handle Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-03-18", "2011-03-24", "Shady Oak", "906", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Bulb Burned Out",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-17", "2011-02-19",
    			"Amberson", "601", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-07-06", "2011-07-08",
    			"Amberson", "802", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-10", "2011-05-11",
    			"Resnik", "113", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Door Lock Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-17", "2011-08-20",
    			"Resnik", "209", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-04", "2011-08-06",
    			"Resnik", "605", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-06-13", "2011-06-21", "Shady Oak", "915", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-03", "2011-01-07", "Shady Oak", "915", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Pipe Rusted",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-06", "2011-02-13",
    			"Resnik", "508", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-01-06", "2011-01-14", "Resnik",
    			"304", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Bulb Burned Out",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-09-15", "2011-09-19", "Resnik",
    			"314", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-05-14", "2011-05-22",
    			"Resnik", "803", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-06-05", "2011-06-11", "Resnik",
    			"414", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-03-12", "2011-03-13",
    			"Resnik", "902", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Paint Peeling",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-19", "2011-01-26",
    			"Resnik", "108", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Leaking",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-01-12", "2011-01-19", "Shady Oak", "515", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Dent in Wall",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-04-06", "2011-04-07",
    			"Resnik", "104", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Light Switch Broken",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-17", "2011-09-25",
    			"Resnik", "109", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-06-05", "2011-06-12", "Shady Oak", "914", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Sink Drain Clogged",
    			RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-02-19", "2011-02-27",
    			"Amberson", "810", "In bathroom.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    			dbAdapter.createRequest("Carpet Torn",
    			RequestsDatabaseHelper.PRIORITY_HIGH, "2011-08-09", "2011-08-13", "Shady Oak", "111", "In kitchen.", "Successfully replaced part",
    			RequestsDatabaseHelper.STATUS_COMPLETE);
    	
    	dbAdapter.createRequest("Space Heater Broken", RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-09-29", "N/A", 
    			"Amberson", "B4", "Living Room", null, RequestsDatabaseHelper.STATUS_INCOMPLETE);
    	dbAdapter.createRequest("Door Lock Broken", RequestsDatabaseHelper.PRIORITY_HIGH, "2011-10-19", "N/A", 
    			"Amberson", "C1", "Fix ASAP!", null, RequestsDatabaseHelper.STATUS_INCOMPLETE);
    	dbAdapter.createRequest("Light Bulb Burned Out", RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-01", "N/A", 
    			"Shady Oak", "101", "Kitchen Lamp", null, RequestsDatabaseHelper.STATUS_INCOMPLETE);
    	dbAdapter.createRequest("Dent in Wall", RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-03", "N/A", 
    			"Shady Oak", "103", "In living room.", null, RequestsDatabaseHelper.STATUS_INCOMPLETE);
    	dbAdapter.createRequest("Sink Leaking", RequestsDatabaseHelper.PRIORITY_HIGH, "2011-11-03", "N/A", 
    			"Resnik", "310", "Bathroom sink. Tenant is about to terminate contract, must be fixed now!", null, RequestsDatabaseHelper.STATUS_INCOMPLETE);
    	dbAdapter.createRequest("Paint Peeling", RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-08-29", "N/A", 
    			"Resnik", "108", "Bathroom wall.", null, RequestsDatabaseHelper.STATUS_INCOMPLETE);
    	dbAdapter.createRequest("Pipe Rusted", RequestsDatabaseHelper.PRIORITY_NORMAL, "2011-10-09", "N/A", 
    			"Resnik", "405", "Under kitchen sink.", null, RequestsDatabaseHelper.STATUS_INCOMPLETE);
    	cursor = dbAdapter.fetchAllIncompleteRequestsOrderByPriority();
    	Log.v("DEBUG", cursor.getCount() + "");
		startManagingCursor(cursor);
		setListAdapterWithCursor();
	}
    
    
    public void onSortByPriorityButtonClick(View view)
    {
    	cursor.close();
    	cursor = dbAdapter.fetchAllIncompleteRequestsOrderByPriority();
    	Log.v("DEBUG", cursor.getCount() + "");
		startManagingCursor(cursor);
		setListAdapterWithCursor();
    }
    
    public void onSortByDateButtonClick(View view)
    {
    	cursor.close();
    	cursor = dbAdapter.fetchAllIncompleteRequestsOrderByDate();
    	Log.v("DEBUG", cursor.getCount() + "");
		startManagingCursor(cursor);
		setListAdapterWithCursor();
    }
    
    public void onViewMapButtonClick(View view) {
    	Intent intent = new Intent(this, ViewMapIncompleteRequestsBuildingsActivity.class);
    	startActivity(intent);
    }
    
    public void handleHelpButton(View view)
    {
    	 //set up dialog
        final Dialog dialog = new Dialog(ViewListIncompleteRequestsActivity.this);
        dialog.setContentView(R.layout.incomplete_requests_help_dialog);
        dialog.setTitle("About this page");
        dialog.setCancelable(true);

        //set up text
        TextView text = (TextView) dialog.findViewById(R.id.TextView01);
        text.setText("Marks a task as high priority");

        //set up image view
        ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
        img.setImageResource(R.drawable.priority);

        //set up button
        Button button = (Button) dialog.findViewById(R.id.Button01);
        button.setOnClickListener(new OnClickListener() 
        	{
        		public void onClick(View v) 
        		{
        			dialog.dismiss();
        		}
        	}); 
        dialog.show();
    	
    }
    
    
    
    public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
            View view, int pos, long id) 
        {
        	switch(pos)
        	{
        	case 0: onSortByPriorityButtonClick(view);break;
        	case 1: onSortByDateButtonClick(view);break;
        	 default: break;
        	}
        }

        public void onNothingSelected(AdapterView<?> parent) 
        {
          // Do nothing.
        }
    }
    
    
    public void setListAdapterWithCursor() {
//    	String[] from = new String[] { RequestsDatabaseHelper.colRequestName, RequestsDatabaseHelper.colRequestDateAdded, RequestsDatabaseHelper.colRequestBuilding };
//		int[] to = new int[] { R.id.request_row_name_label, R.id.request_row_date_added_label, R.id.request_row_building_label };

		// Now create an array adapter and set it to display using our row
		RowAdapter requests = new RowAdapter(this, cursor);
		setListAdapter(requests);
    }
    
    private static class RowAdapter extends CursorAdapter {

        public RowAdapter(Context context, Cursor c) {
            super(context, c);
        }

        public void bindView(View view, Context context, Cursor c) {
        	String firstRowText = "";
        	String secondRowText = "";
        	String thirdRowText = "";
        	String apartmentVal = c.getString(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestApartment));
            String buildingVal = c.getString(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestBuilding));
            String nameVal= c.getString(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestName));
            String dateAddedVal = c.getString(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestDateAdded));
            int priorityVal = c.getInt(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestPriority));
            ImageView imageView = (ImageView) view.findViewById(R.id.request_row_exclamation_iv);
            if(priorityVal == RequestsDatabaseHelper.PRIORITY_HIGH) {
            	imageView.setVisibility(View.VISIBLE);
            }
            else if(priorityVal == RequestsDatabaseHelper.PRIORITY_NORMAL) {
            	imageView.setVisibility(View.INVISIBLE);
            }
            firstRowText += buildingVal + " " + apartmentVal;
            secondRowText += firstRowText + " - " +nameVal;
            thirdRowText += "Added " + dateAddedVal;
            
            
            TextView secondRowTV = (TextView) view.findViewById(R.id.request_row_second_row_tv);
            TextView thirdRowTV = (TextView) view.findViewById(R.id.request_row_third_row_tv);
            secondRowTV.setText(secondRowText);
            thirdRowTV.setText(thirdRowText);
            
        }

        public View newView(Context context, Cursor c, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.request_row, parent, false);
            bindView(view, context, c);
            return view;
        }
    }

}
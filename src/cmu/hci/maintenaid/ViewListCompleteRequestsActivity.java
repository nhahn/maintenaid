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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewListCompleteRequestsActivity extends ListActivity {
	private RequestsDbAdapter dbAdapter;
	private Cursor cursor;
	private String refineType;
	private String building = "";
	private String floor = "";
	private String apartment = "";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_complete_requests);
        
        dbAdapter = new RequestsDbAdapter(this);
        dbAdapter.open();
        
        // Unpack bundle
        Bundle b = getIntent().getExtras();
        refineType = b.getString("TYPE");
        TextView titleTV = (TextView) findViewById(R.id.view_list_complete_requests_title_tv);
        if(refineType.equals("BUILDING")) {
        	building = b.getString("building");
        	cursor = dbAdapter.fetchAllCompleteRequestsForBuildingOrderByApartment(building);
        	Log.v("DEBUG", cursor.getCount() + "");
    		startManagingCursor(cursor);
    		setListAdapterWithCursor();
    		titleTV.setText("Completed Requests for " + building+":");
        }
        else if(refineType.equals("BUILDING_AND_FLOOR")) {
        	building = b.getString("building");
        	floor = b.getString("floor");
        	cursor = dbAdapter.fetchAllCompleteRequestsForBuildingAndFloorOrderByApartment(building, floor);
        	startManagingCursor(cursor);
        	setListAdapterWithCursor();
        	titleTV.setText("Completed Requests for " + building+" Floor " + floor + ":");
        }
        else if(refineType.equals("BUILDING_AND_APARTMENT")) {
        	building = b.getString("building");
        	apartment = b.getString("apartment");
        	cursor = dbAdapter.fetchAllCompleteRequestsForBuildingAndApartmentOrderByDate(building, apartment);
        	startManagingCursor(cursor);
        	setListAdapterWithCursor();
        	titleTV.setText("Completed Requests for " + building + " " + apartment + ":");
        }
        else {
        	cursor = dbAdapter.fetchAllCompleteRequestsOrderByApartment();
        	Log.v("DEBUG", cursor.getCount() + "");
    		startManagingCursor(cursor);
    		setListAdapterWithCursor();
    		titleTV.setText("All Completed Requests:");
        }
        
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                onListItemClick(v,pos,id);
            }
        });
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        if(refineType.equals("BUILDING_AND_APARTMENT")) {
        	spinner.setVisibility(View.INVISIBLE);
        	TextView tv = (TextView) findViewById(R.id.title_tv);
        	tv.setVisibility(View.INVISIBLE);
        }
        else {
        	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            		this, R.array.complete_sorting_options, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        }
    }
    
    public void handleHelpButton(View view)
    {
    	//set up dialog
        final Dialog dialog = new Dialog(ViewListCompleteRequestsActivity.this);
        dialog.setContentView(R.layout.complete_requests_help_dialog);
        dialog.setTitle("About this page");
        dialog.setCancelable(true);

        //set up button
        Button button = (Button) dialog.findViewById(R.id.Button02);
        button.setOnClickListener(new OnClickListener() {
        
            public void onClick(View v) {
                dialog.dismiss();
            }
        });   
        dialog.show();
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
    
    public void setListAdapterWithCursor() {
		RowAdapter requests = new RowAdapter(this, cursor, refineType);
		setListAdapter(requests);
    }
    
    private static class RowAdapter extends CursorAdapter {

    	private String refineType;
    	
        public RowAdapter(Context context, Cursor c, String refineType) {
            super(context, c);
            this.refineType = refineType;
        }

        public void bindView(View view, Context context, Cursor c) {
        	String firstRowText = "";
        	String secondRowText = "";
        	String thirdRowText = "";

        	String apartmentVal = c.getString(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestApartment));
            String nameVal= c.getString(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestName));
            String dateCompletedVal = c.getString(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestDateCompleted));
            int priorityVal = c.getInt(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestPriority));

            thirdRowText += "Completed " + dateCompletedVal;

            ImageView imageView = (ImageView) view.findViewById(R.id.request_row_exclamation_iv);
            if(priorityVal == RequestsDatabaseHelper.PRIORITY_HIGH) {
            	imageView.setVisibility(View.VISIBLE);
            }
            else if(priorityVal == RequestsDatabaseHelper.PRIORITY_NORMAL) {
            	imageView.setVisibility(View.INVISIBLE);
            }
            
            if(refineType.equals("ALL")) {
            	String buildingVal = c.getString(c.getColumnIndexOrThrow(RequestsDatabaseHelper.colRequestBuilding));
            	firstRowText += buildingVal + " " + apartmentVal + " - ";
            }
            else if(refineType.equals("BUILDING") || 
            		refineType.equals("BUILDING_AND_FLOOR")) {
            	firstRowText += "Apt. " + apartmentVal + " - ";
            }
            
            secondRowText += firstRowText + nameVal;
            
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
    
    public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
            View view, int pos, long id) 
        {
        	switch(pos)
        	{
        	 case 1: onSortByDateSpinnerSelection(view);break;
        	 case 0: onSortByBuildingAndApartmentSpinnerSelection(view);break;
        	 default: break;
        	}
        }

        public void onNothingSelected(AdapterView<?> parent) 
        {
          // Do nothing.
        }
    }
    
    public void onSortByDateSpinnerSelection(View view)
    {
    	cursor.close();
    	if(building.equals("")) {
    		cursor = dbAdapter.fetchAllCompleteRequestsOrderByDate();
    	}
    	else if(refineType.equals("BUILDING_AND_FLOOR")) {
    		cursor = dbAdapter.fetchAllCompleteRequestsForBuildingAndFloorOrderByDate(building, floor);
    	}
    	else {
    		cursor = dbAdapter.fetchAllCompleteRequestsForBuildingOrderByDate(building);
    	}
		startManagingCursor(cursor);
		setListAdapterWithCursor();
    }
    
    public void onSortByBuildingAndApartmentSpinnerSelection(View view)
    {
    	cursor.close();
    	if(building.equals("")) {
    		cursor = dbAdapter.fetchAllCompleteRequestsOrderByApartment();
    	}
    	else if(refineType.equals("BUILDING_AND_FLOOR")) {
    		cursor = dbAdapter.fetchAllCompleteRequestsForBuildingAndFloorOrderByApartment(building, floor);
    	}
    	else {
    		cursor = dbAdapter.fetchAllCompleteRequestsForBuildingOrderByApartment(building);
    	}
		startManagingCursor(cursor);
		setListAdapterWithCursor();
    }
}
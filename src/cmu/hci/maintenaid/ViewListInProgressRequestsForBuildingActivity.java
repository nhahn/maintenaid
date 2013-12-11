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

public class ViewListInProgressRequestsForBuildingActivity extends ListActivity {
	private RequestsDbAdapter dbAdapter;
	private Cursor cursor;
	private String building;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_incomplete_requests_for_building);
        
        // Unpack bundle
        Bundle b = getIntent().getExtras();
        TextView titleTV = (TextView) findViewById(R.id.textView2);
    	building = b.getString("building");
    	titleTV.setText("Requests in Progress for " + building + ":");
    	dbAdapter = new RequestsDbAdapter(this);
        dbAdapter.open();
        cursor = dbAdapter.fetchAllInProgressRequestsForBuildingOrderByPriority(building);
		startManagingCursor(cursor);
		setListAdapterWithCursor();
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
    
    public void onSortByPriorityButtonClick(View view)
    {
    	cursor.close();
    	cursor = dbAdapter.fetchAllInProgressRequestsForBuildingOrderByPriority(building);
    	Log.v("DEBUG", cursor.getCount() + "");
		startManagingCursor(cursor);
		setListAdapterWithCursor();
    }
    
    public void onSortByDateButtonClick(View view)
    {
    	cursor.close();
    	cursor = dbAdapter.fetchAllInProgressRequestsForBuildingOrderByDate(building);
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
        final Dialog dialog = new Dialog(ViewListInProgressRequestsForBuildingActivity.this);
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
        button.setOnClickListener(new OnClickListener() {
        
            public void onClick(View v) {
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
        	 case 1: onSortByDateButtonClick(view);break;
        	 case 0: onSortByPriorityButtonClick(view);break;
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
            firstRowText += "Apt. " + apartmentVal;
            secondRowText += firstRowText + " - " + nameVal;
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
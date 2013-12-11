package cmu.hci.maintenaid;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ViewRequestDetailsActivity extends Activity {
	private int id;
	private String name;
	private String building;
	private String apartment;
	private String dateAdded;
	private String dateCompleted;
	private String details;
	private String comments;
	private int priority;
	private int status;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_request_details_new);
        
        // Unpack bundle
        Bundle b = getIntent().getExtras();
        TextView tv;
        EditText et;

        id = b.getInt("id");
        //Get the Status, we will need this through out the view
        status = b.getInt("status");
        
        name = b.getString("name");
        tv = (TextView) findViewById(R.id.request_details_name_tv);
        tv.setText(name);
        
        building = b.getString("building");
        tv = (TextView) findViewById(R.id.request_details_building_tv);
        tv.setText(building);
        

        apartment = b.getString("apartment");
        tv = (TextView) findViewById(R.id.request_details_apartment_tv);
        tv.setText("Apt.:" + apartment);
        
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout5);
        linearLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+building+"+Pittsburgh+PA")); 
				startActivity(i);
			}
		});
        
        priority= b.getInt("priority");
        ImageView imageView = (ImageView) findViewById(R.id.request_row_exclamation_iv);
        if(priority == RequestsDatabaseHelper.PRIORITY_HIGH) {
        	imageView.setVisibility(View.VISIBLE);
        }
        else if(priority == RequestsDatabaseHelper.PRIORITY_NORMAL) {
        	imageView.setVisibility(View.INVISIBLE);
        }
        
        tv = (TextView) findViewById(R.id.request_details_date_added_tv);
        TextView dv = (TextView) findViewById(R.id.request_details_date_type);
        
        dateAdded = b.getString("dateAdded");        
        dateCompleted = b.getString("dateCompleted");

        
        switch (status)
        {
        	case RequestsDatabaseHelper.STATUS_INCOMPLETE: 
        		tv.setText(dateAdded);
        		dv.setText("Date Added:");
        		break;
        	case RequestsDatabaseHelper.STATUS_IN_PROGRESS: 
        		tv.setText(dateAdded); 
        		dv.setText("Date Added:");
        		break;
        	case RequestsDatabaseHelper.STATUS_COMPLETE: 
        		tv.setText(dateCompleted);
        		dv.setText("Date Completed:");
        		break;
        	default: break;
        }
        
        details = b.getString("details");
        tv = (TextView) findViewById(R.id.request_details_details_tv);
        tv.setText(details);
        
        Button moreButton = (Button)findViewById(R.id.imageButton1);
        moreButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button moreButton = (Button)v;
				TextView tv = (TextView)((View)v.getParent().getParent()).findViewById(R.id.request_details_details_tv);
				LayoutParams layout = (LayoutParams) tv.getLayoutParams();
				if (moreButton.getText().equals("<< Less"))
				{
					layout.height = 60;
					moreButton.setText("More >>");
				}
				else
				{
					layout.height = -2;
					moreButton.setText("<< Less");
				}
				tv.setLayoutParams(layout);
				
			}
		});
        
        comments = b.getString("comments");
        et = (EditText) findViewById(R.id.request_details_notes_edit_text);
        et.setText(comments);
        et.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
                
        et.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                	EditText et = (EditText) findViewById(R.id.request_details_notes_edit_text);
                    comments = et.getText().toString();
                   	RequestsDbAdapter dbAdapter = new RequestsDbAdapter(v.getContext());
                    dbAdapter.open();
                	dbAdapter.updateRequest(id, name, priority, dateAdded, dateCompleted, building, apartment, details, comments, status);
                	dbAdapter.close();
                	Toast toast = Toast.makeText(v.getContext(), "Updated Report", Toast.LENGTH_LONG);
                	toast.show();
                    return true;
                }
                return false;
			}
        });
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		this, R.array.progress_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        switch (status)
        {
        	case RequestsDatabaseHelper.STATUS_INCOMPLETE: spinner.setSelection(0); break;
        	case RequestsDatabaseHelper.STATUS_IN_PROGRESS: spinner.setSelection(1); break;
        	case RequestsDatabaseHelper.STATUS_COMPLETE: spinner.setSelection(2); break;
        	default: break;
        }

        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
    }
    
    @Override
    public void onBackPressed() {
    	EditText et = (EditText) findViewById(R.id.request_details_notes_edit_text);
        comments = et.getText().toString();
       	RequestsDbAdapter dbAdapter = new RequestsDbAdapter(this);
        dbAdapter.open();
    	dbAdapter.updateRequest(id, name, priority, dateAdded, dateCompleted, building, apartment, details, comments, status);
    	dbAdapter.close();
    	Toast toast = Toast.makeText(this, "Updated Report", Toast.LENGTH_LONG);
    	toast.show();
    	super.onBackPressed();
    }

    
    private class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
            View view, int pos, long id) 
        {
        	int newStatus = -1;
        	Toast toast = Toast.makeText(parent.getContext(), "No Changes Made", Toast.LENGTH_LONG);

        	switch(pos)
        	{
        	 case 0: newStatus = RequestsDatabaseHelper.STATUS_INCOMPLETE;
     	  			 dateCompleted = "N/A";
     	        	 toast.setText("Task now in Incomplete");
     	  			 break;
        	 case 1: newStatus = RequestsDatabaseHelper.STATUS_IN_PROGRESS;
	  			 	 dateCompleted = "N/A";
     	        	 toast.setText("Task now in In Progress");
	  			 	 break;
        	 case 2: newStatus = RequestsDatabaseHelper.STATUS_COMPLETE; 
     				 if(status != newStatus) { // change in status -- request marked as completed for first time -- change date completed
     				   Calendar currentDate = Calendar.getInstance();
     				   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     				   dateCompleted = formatter.format(currentDate.getTime()); 
     				 }
     	        	 toast.setText("Task now in Complete");
     				 break;
        	 default: break;
        	}
           	RequestsDbAdapter dbAdapter = new RequestsDbAdapter(parent.getContext());
            dbAdapter.open();
        	dbAdapter.updateRequest(id, name, priority, dateAdded, dateCompleted, building, apartment, details, comments, newStatus);
        	status = newStatus;
        	dbAdapter.close();
        	toast.show();
        }

        public void onNothingSelected(AdapterView<?> parent) 
        {
          // Do nothing.
        }
    }
    
    public void handleHelpButton(View view)
    {
    	//set up dialog
        final Dialog dialog = new Dialog(ViewRequestDetailsActivity.this);
        dialog.setContentView(R.layout.request_details_help_dialog);
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
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {     
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
    		Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
    	}
    }
}


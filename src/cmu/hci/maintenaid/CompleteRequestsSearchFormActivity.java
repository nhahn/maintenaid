package cmu.hci.maintenaid;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CompleteRequestsSearchFormActivity extends Activity
{
	private Spinner buildingSpinner;
	private Spinner floorSpinner;
	private Spinner apartmentSpinner;
	private RequestsDbAdapter dbAdapter;
	private String allFloorsString = "All Floors";
	private String allApartmentsString = "All Apartments";
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_requests_search_form);
        dbAdapter = new RequestsDbAdapter(this);
        dbAdapter.open();
        initSpinners();
	}
	
	protected void onStart() {
		super.onStart();
		if(apartmentSpinner.isEnabled()) { // apartment was selected before; might be gone now if only completed request was marked as no longer completed
			String previouslySelectedApartment = apartmentSpinner.getSelectedItem().toString();
			String previouslySelectedFloor = floorSpinner.getSelectedItem().toString();
			Cursor cursor = dbAdapter.fetchAllCompleteApartmentsForBuildingAndFloor(buildingSpinner.getSelectedItem().toString(), previouslySelectedFloor);
			startManagingCursor(cursor);
			ArrayList<String> apartmentSpinnerArray = new ArrayList<String>();
			apartmentSpinnerArray.add(allApartmentsString);
			cursor.moveToFirst();
			int previouslySelectedIndex = -1;
			int i = 1;
	        while (cursor.isAfterLast() == false) {
	        	String currentApartment = cursor.getString(0);
	        	if(currentApartment.equals(previouslySelectedApartment)) {
	        		previouslySelectedIndex = i;
	        	}
	            apartmentSpinnerArray.add(currentApartment);
	       	    cursor.moveToNext();
	       	    i++;
	        }
	        cursor.close();
	        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
	        		this, android.R.layout.simple_spinner_item, apartmentSpinnerArray);
	        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
	        apartmentSpinner.setAdapter(spinnerArrayAdapter);
	        apartmentSpinner.setEnabled(true);
	        if(previouslySelectedIndex != -1) {
	        	apartmentSpinner.setSelection(previouslySelectedIndex);
	        	return;
	        }
		}
		if(floorSpinner.isEnabled()) { // floor was selected before; might be gone now if all completed requests were marked as no longer completed
			String previouslySelectedBuilding = buildingSpinner.getSelectedItem().toString();
			String previouslySelectedFloor = floorSpinner.getSelectedItem().toString();
			Cursor cursor = dbAdapter.fetchAllCompleteApartmentFloorsForBuilding(previouslySelectedBuilding);
			startManagingCursor(cursor);
			ArrayList<String> floorSpinnerArray = new ArrayList<String>();
			floorSpinnerArray.add(allFloorsString);
			cursor.moveToFirst();
			int previouslySelectedIndex = -1;
			int i = 1;
	        while (cursor.isAfterLast() == false) {
	        	String currentFloor = cursor.getString(0);
	        	if(currentFloor.equals(previouslySelectedFloor)) {
	        		previouslySelectedIndex = i; // don't change what's in the apartment spinner since old one is still there
	        	}
	            floorSpinnerArray.add(currentFloor);
	       	    cursor.moveToNext();
	       	    i++;
	        }
	        cursor.close();
	        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
	        		this, android.R.layout.simple_spinner_item, floorSpinnerArray);
	        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
	        floorSpinner.setAdapter(spinnerArrayAdapter);
	        floorSpinner.setEnabled(true);
	        if(previouslySelectedIndex != -1)
	        	floorSpinner.setSelection(previouslySelectedIndex);
		}
	}
	
	private void initSpinners()
	{
		buildingSpinner = (Spinner) findViewById(R.id.building_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		this, R.array.building_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(adapter);
        buildingSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        floorSpinner = (Spinner) findViewById(R.id.floor_spinner);
        ArrayList<String> floorSpinnerInitialArray = new ArrayList<String>();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_dropdown_item, floorSpinnerInitialArray);
        floorSpinner.setAdapter(spinnerArrayAdapter);
        floorSpinner.setEnabled(false);
        floorSpinner.setOnItemSelectedListener(new FloorOnItemSelectedListener());
        apartmentSpinner = (Spinner) findViewById(R.id.apartment_spinner);
        ArrayList<String> apartmentSpinnerInitialArray = new ArrayList<String>();
        spinnerArrayAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_dropdown_item, apartmentSpinnerInitialArray);
        apartmentSpinner.setAdapter(spinnerArrayAdapter);
        apartmentSpinner.setEnabled(false);
	}
	
	public void handleHelpButton(View view)
	{
		//set up dialog
        final Dialog dialog = new Dialog(CompleteRequestsSearchFormActivity.this);
        dialog.setContentView(R.layout.search_form_help_dialog);
        dialog.setTitle("About this page");
        dialog.setCancelable(true);

        //set up button
        Button button = (Button) dialog.findViewById(R.id.Button03);
        button.setOnClickListener(new OnClickListener() {
        
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
	}
	
    public void onViewMapButtonClick(View view) {
    	Intent intent = new Intent(this, ViewMapCompleteRequestsBuildingsActivity.class);
    	startActivity(intent);
    }
    
	public void handleSearchButton(View view)
	{
    	Intent i = new Intent(this, ViewListCompleteRequestsActivity.class);

    	if(!buildingSpinner.getSelectedItem().equals("All buildings"))
    	{
    		if(floorSpinner.getSelectedItem().equals(allFloorsString)) {
    			i.putExtra("TYPE", "BUILDING");
    			i.putExtra("building", (String)buildingSpinner.getSelectedItem());
    		}
    		else if(!floorSpinner.getSelectedItem().equals(allFloorsString)) {
    			if(apartmentSpinner.getSelectedItem().equals(allApartmentsString)) {
    				i.putExtra("TYPE", "BUILDING_AND_FLOOR");
        			i.putExtra("building", (String)buildingSpinner.getSelectedItem());
        			i.putExtra("floor", (String)floorSpinner.getSelectedItem());
    			}
    			else if(!apartmentSpinner.getSelectedItem().equals(allApartmentsString)) {
    				i.putExtra("TYPE", "BUILDING_AND_APARTMENT");
        			i.putExtra("building", (String)buildingSpinner.getSelectedItem());
        			i.putExtra("apartment", (String)apartmentSpinner.getSelectedItem());
    			}
    		}
    	}
    	else i.putExtra("TYPE", "ALL");
        startActivityForResult(i, 0);
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
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
            View view, int pos, long id) 
        {
        	switch(pos)
        	{
        	 case 0: allBuildingsSpinnerSelection(view);break;
        	 case 1: AmbersonSpinnerSelection(view);break;
        	 case 2: ResnikSpinnerSelection(view);break;
        	 case 3: ShadyOakSpinnerSelection(view);break;
        	 default: break;
        	}
        }

        public void onNothingSelected(AdapterView<?> parent) 
        {
          // Do nothing.
        }
    }
	
	public class FloorOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
        		View view, int pos, long id) {
        	String selectedFloor = parent.getItemAtPosition(pos).toString();
        	if(selectedFloor.equals(allFloorsString)) {
        		ArrayList<String> apartmentSpinnerInitialArray = new ArrayList<String>();
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                		CompleteRequestsSearchFormActivity.this, android.R.layout.simple_spinner_dropdown_item, apartmentSpinnerInitialArray);
                apartmentSpinner.setAdapter(spinnerArrayAdapter);
        		apartmentSpinner.setEnabled(false);
        	}
        	else {
        		Cursor cursor = dbAdapter.fetchAllCompleteApartmentsForBuildingAndFloor(buildingSpinner.getSelectedItem().toString(), selectedFloor);
        		startManagingCursor(cursor);
        		setApartmentSpinnerAdapterWithCursor(cursor);
        	}
        }

        public void onNothingSelected(AdapterView<?> parent) 
        {
          // Do nothing.
        }
    }
	
	private void allBuildingsSpinnerSelection(View view) {
		ArrayList<String> floorSpinnerInitialArray = new ArrayList<String>();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_dropdown_item, floorSpinnerInitialArray);
        floorSpinner.setAdapter(spinnerArrayAdapter);
		floorSpinner.setEnabled(false);
		ArrayList<String> apartmentSpinnerInitialArray = new ArrayList<String>();
        spinnerArrayAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_dropdown_item, apartmentSpinnerInitialArray);
        apartmentSpinner.setAdapter(spinnerArrayAdapter);
		apartmentSpinner.setEnabled(false);
	}
	
	private void AmbersonSpinnerSelection(View view)
    {
    	Cursor cursor = dbAdapter.fetchAllCompleteApartmentFloorsForBuilding("Amberson");
		startManagingCursor(cursor);
		setFloorSpinnerAdapterWithCursor(cursor);
		Log.v("HEYHEY", "" + cursor.getCount());
    }
	
	private void ResnikSpinnerSelection(View view)
    {
    	Cursor cursor = dbAdapter.fetchAllCompleteApartmentFloorsForBuilding("Resnik");
		startManagingCursor(cursor);
		setFloorSpinnerAdapterWithCursor(cursor);
    }
	
	private void ShadyOakSpinnerSelection(View view)
    {
    	Cursor cursor = dbAdapter.fetchAllCompleteApartmentFloorsForBuilding("Shady Oak");
		startManagingCursor(cursor);
		setFloorSpinnerAdapterWithCursor(cursor);
    }
	
	private void setFloorSpinnerAdapterWithCursor(Cursor cursor) {
		ArrayList<String> floorSpinnerArray = new ArrayList<String>();
		floorSpinnerArray.add(allFloorsString);
		cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            floorSpinnerArray.add(cursor.getString(0));
            Log.v("YOO", cursor.getString(0));
       	    cursor.moveToNext();
        }
        cursor.close();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_item, floorSpinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        floorSpinner.setAdapter(spinnerArrayAdapter);
        floorSpinner.setEnabled(true);
	}
	
	private void setApartmentSpinnerAdapterWithCursor(Cursor cursor) {
		ArrayList<String> apartmentSpinnerArray = new ArrayList<String>();
		apartmentSpinnerArray.add(allApartmentsString);
		cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            apartmentSpinnerArray.add(cursor.getString(0));
       	    cursor.moveToNext();
        }
        cursor.close();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_item, apartmentSpinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        apartmentSpinner.setAdapter(spinnerArrayAdapter);
        apartmentSpinner.setEnabled(true);
	}
}

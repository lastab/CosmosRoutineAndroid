package com.xls.aroutine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DisplayRoutine extends Activity{
	Spinner sDays ,sProgram;
	
			////////////Declerations
	DateFormat formatter = new SimpleDateFormat("HH:mm");
	TableRow[] Trow= new TableRow[9];
	Date[] startTime=new Date[9], 	endTime=new Date[9];
	int selDay=0, selPro=0;
	
	Calendar calendar = Calendar.getInstance();
	
	
	/////////////
	OnItemSelectedListener sometihingSelected= new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			
			selDay=	sDays.getSelectedItemPosition();
			selPro=sProgram.getSelectedItemPosition();
			try {
				////////////////////
				displayRoutine(selDay,selPro);
				if(selDay==calendar.get(Calendar.DAY_OF_WEEK))
					getCurrentClass(Trow, startTime, endTime);
			
			} catch (Exception e) {	}
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	//////////////
	
	
	private Handler timeHandler= new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_routine);
		try{
			sDays=(Spinner) findViewById(R.id.Days);
			sProgram=(Spinner)findViewById(R.id.spinProgram);
			ArrayAdapter aDays=ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
			ArrayAdapter aPrograms=ArrayAdapter.createFromResource(this, R.array.programs, android.R.layout.simple_spinner_item);
			sProgram.setAdapter(aPrograms);
			sDays.setAdapter(aDays);
			sProgram.setOnItemSelectedListener(sometihingSelected);
			sDays.setOnItemSelectedListener(sometihingSelected);
			
			/*new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					selDay=	sDays.getSelectedItemPosition();
					try {
						displayRoutine(selDay);
						if(selDay==calendar.get(Calendar.DAY_OF_WEEK))
							getCurrentClass(Trow, startTime, endTime);
					
					} catch (Exception e) {	}
				}
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			})*/;
		//  	displayRoutine(0);
		}
		catch (Exception e){}
		 getCurrentClass( Trow, startTime,endTime);
		
		////////////////////time handeler
		 int min=(5-calendar.get(Calendar.MINUTE)%5),sec=(60-calendar.get(Calendar.SECOND));		 
		 timeHandler.postDelayed(updateTimerThread, (min*60+sec)*1000-60000);		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_routine, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void displayRoutine(int Tday, int program) throws Exception{
		String timeRange,temp;
		TextView Day =(TextView) findViewById(R.id.txtDay);
		
		
		// Creating Input Stream
        File file = new File("/storage/sdcard0/asd.xls");
        
        FileInputStream myInput = new FileInputStream(file);
        
        // Create a POIFSFileSystem object
        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
        
        // Create a workbook using the File System
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

        // Get the first sheet from workbook
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);
        
        Row row=mySheet.getRow(6);
        
        Cell cell= row.getCell(0);
        Day.setText(cell.toString());
        
        
        
        int today;
        //Check if request is for today or specific day
       /* if (Tday==0){
        today= calendar.get(Calendar.DAY_OF_WEEK);
        }
        else{
        	today=Tday;
        }*/
        
        ////////////////some problem with -1 check later to remove
        int minr= 6+((Tday-1)*39);
        int rdiff=3;         
        
       
        row=mySheet.getRow(minr);
        cell= row.getCell(0);
        Day.setText(cell.toString());
        
        
        

		TableLayout disArea= (TableLayout) findViewById(R.id.tblDisplayRoutine);
		disArea.removeAllViews();
		for (int rows=0;rows <9;rows++){			
		  	TableRow tableRow= new TableRow(this);
		  	disArea.addView(tableRow);
		  	
		  	if (rows%2==0)
		  	{tableRow.setBackgroundColor(Color.WHITE);}
		  	else
		  	{tableRow.setBackgroundColor(Color.LTGRAY);}
		  	
        	tableRow.setLayoutParams(new TableLayout.LayoutParams(
        			TableLayout.LayoutParams.MATCH_PARENT,
        			TableLayout.LayoutParams.MATCH_PARENT,1.0f
        			));        	
        	row=mySheet.getRow(minr);
        		cell=row.getCell(1);
        		TextView Period = new TextView(this);
        		Period.setLayoutParams(new TableRow.LayoutParams(
            			TableRow.LayoutParams.MATCH_PARENT,
            			TableRow.LayoutParams.MATCH_PARENT,1.0f            			
            			));        		
        		Period.setGravity(Gravity.CENTER);
        		Period.setText(cell.toString().substring(0,1)+". ");
        		tableRow.addView(Period);        		
        		cell=row.getCell(2);
        		timeRange =cell.toString();
        		
        		TextView Time1 = new TextView(this);
        		Time1.setLayoutParams(new TableRow.LayoutParams(
            			TableRow.LayoutParams.MATCH_PARENT,
            			TableRow.LayoutParams.MATCH_PARENT,1.0f
            			));
        		Time1.setGravity(Gravity.CENTER);
        		temp=timeRange.substring(0,9);
        		temp=temp.replace("a.m.", "");
        		temp=temp.replace("p.m.", "");
        		Time1.setText(temp);        		
        		startTime[rows] = (Date)formatter.parse(temp.trim());
        		tableRow.addView(Time1);
        		TextView Time2 = new TextView(this);
        		Time2.setLayoutParams(new TableRow.LayoutParams(
            			TableRow.LayoutParams.MATCH_PARENT,
            			TableRow.LayoutParams.MATCH_PARENT,1.0f
            			));
        		Time2.setGravity(Gravity.CENTER);
        		temp=timeRange.substring(11);
        		temp=temp.replace("a.m.", "");
        		temp=temp.replace("p.m.", "");
        		Time2.setText(temp);
        		endTime[rows] = (Date)formatter.parse(temp.trim());
        		tableRow.addView(Time2);  		
        		cell=row.getCell(75+program*6);
        		TextView Subject2 = new TextView(this);
        		Subject2.setLayoutParams(new TableRow.LayoutParams(
            			TableRow.LayoutParams.MATCH_PARENT,
            			TableRow.LayoutParams.MATCH_PARENT,1.0f
            			));            	
        		Subject2.setGravity(Gravity.CENTER);
        		try{
        			Subject2.setText(cell.toString());
        			tableRow.addView(Subject2);
        			row=mySheet.getRow(minr+1);
        			cell=row.getCell(75+program*6);
        			TextView Teacher = new TextView(this);
        			Teacher.setLayoutParams(new TableRow.LayoutParams(
                			TableRow.LayoutParams.MATCH_PARENT,
                			TableRow.LayoutParams.MATCH_PARENT,1.0f
                			));            			
        			Teacher.setGravity(Gravity.CENTER);
            		Teacher.setText(cell.toString().trim());
            		tableRow.addView(Teacher);
            		
            		
            		row=mySheet.getRow(minr+2);
            		cell=row.getCell(75+program*6);
        			TextView ClassType = new TextView(this);
        			ClassType.setLayoutParams(new TableRow.LayoutParams(
                			TableRow.LayoutParams.MATCH_PARENT,
                			TableRow.LayoutParams.MATCH_PARENT,1.0f
                			));
        			ClassType.setGravity(Gravity.CENTER);
            		ClassType.setText(cell.toString().trim());
            		tableRow.addView(ClassType);
            		TextView Room = new TextView(this);
            		try{
            			cell=row.getCell(75+program*6+1);
            			
            			Room.setLayoutParams(new TableRow.LayoutParams(
                    			TableRow.LayoutParams.MATCH_PARENT,
                    			TableRow.LayoutParams.MATCH_PARENT,1.0f
                    			));                		
            			Room.setGravity(Gravity.CENTER);
            			Room.setText(cell.toString().trim());
            			tableRow.addView(Room);
            		}
            		catch (Exception e){            			
            			Room.setText("No Room");
            			tableRow.addView(Room);
            		}        			
        		}
        		catch(Exception e){
        			Subject2.setText("no Period");
        			tableRow.addView(Subject2);
        		}    		
        	Trow[rows]=tableRow;	
        	minr+=rdiff;        	
		}		
		 myWorkBook.close();   
		
	}
	
	
	private  void getCurrentClass(TableRow[] row, Date[] Stime,Date[] Etime ){
		Date date=  new Date();
		String s=formatter.format(date);
		try{
		date=formatter.parse(s);
		//if selected day == today
		if(selDay==calendar.get(Calendar.DAY_OF_WEEK))
		for (int i=0;i<9;i++){
			if ((date.after(Stime[i])&& date.before(Etime[i]))||date.equals(Stime[i])){
				row[i].setBackgroundColor(Color.GREEN);
				}
				else if (i%2==0)
				{
					row[i].setBackgroundColor(Color.WHITE);					
				}
				else
				{
					row[i].setBackgroundColor(Color.LTGRAY);
				}
				
			
		}
		
		}
		catch(Exception e){
			
		}
	}
	
	
	
	
	
	//dont know what it is
	private Runnable updateTimerThread = new Runnable() {
			 
			        public void run() {			        	
			            getCurrentClass(Trow, startTime, endTime);
			            timeHandler.postDelayed(this, 5000*60);
			        }
			 
			    };
	
}

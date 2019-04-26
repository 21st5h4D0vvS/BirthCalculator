package rssths.batch2019.birth_calculator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
	private TextView yearsOutputData, daysOutputData, outputData;
	private DatePickerDialog datePickerDialog;
	private BirthCalculator birthCalculator = new BirthCalculator();
	private DatePickerDialog.OnDateSetListener ufdSetListener;
	private Button[] buttons = new Button[13];
	private String month = "";
	private String days  = "";
	private String years = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		buttons[0]  = (Button) findViewById(R.id.buttonClear);
		buttons[1]  = (Button) findViewById(R.id.buttonBack);
		buttons[2]  = (Button) findViewById(R.id.buttonEqual);
		buttons[3]  = (Button) findViewById(R.id.button0);
		buttons[4]  = (Button) findViewById(R.id.button1);
		buttons[5]  = (Button) findViewById(R.id.button2);
		buttons[6]  = (Button) findViewById(R.id.button3);
		buttons[7]  = (Button) findViewById(R.id.button4);
		buttons[8]  = (Button) findViewById(R.id.button5);
		buttons[9]  = (Button) findViewById(R.id.button6);
		buttons[10] = (Button) findViewById(R.id.button7);
		buttons[11] = (Button) findViewById(R.id.button8);
		buttons[12] = (Button) findViewById(R.id.button9);
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View argumentButton) {
					Button button = (Button) findViewById(argumentButton.getId());
					String[] responseData = inputData((String) button.getText());
					int fixedMonthLength = 2;
					int fixedDaysLength = 2;
					int fixedYearsLength = 4;
					
					String monthNumber = getZeros(fixedMonthLength -responseData[0].length()) +responseData[0];
					String daysNumber = getZeros(fixedDaysLength -responseData[1].length()) +responseData[1];
					String yearsNumber = getZeros(fixedYearsLength -responseData[2].length()) +responseData[2];
					
					outputData = (TextView) findViewById(R.id.headerDataInput);
					outputData.setText(monthNumber +"-" +daysNumber +"-" +yearsNumber);
				}
			});
		}
		
		TextView updateFixedDate = (TextView) findViewById(R.id.updateFixedDate);
		updateFixedDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View p1) {
				Calendar calendar = Calendar.getInstance();
				int month = calendar.get(Calendar.MONTH);
				int days  = calendar.get(Calendar.DAY_OF_MONTH);
				int years = calendar.get(Calendar.YEAR);
				
				datePickerDialog = new DatePickerDialog(MainActivity.this,
					android.R.style.Theme_Holo_Light_Dialog_MinWidth,
					ufdSetListener, years, month, days);
					
				datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				datePickerDialog.show();
			}
		});
		
		ufdSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int years, int month, int days) {
				birthCalculator.setFixedDate(years, month, days);
			}
		};
		
		TextView resetButton = (TextView) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View p1) {
				yearsOutputData.setAlpha((float) 0.6);
				yearsOutputData.setText("0");
				daysOutputData.setAlpha((float) 0.6);
				daysOutputData.setText("0");
				outputData.setText("00-00-0000");
				birthCalculator.resetFixedDate();
				month = "";
				days  = "";
				years = "";
			}
		});
    }
	
	protected String[] inputData(String input) {
		TextView inputObject = (TextView) findViewById(R.id.headerDataInput);
		String[] inputFormat = inputObject.getText().toString().split("-");
		
		if (isNotNumerical(input)) {
			if (month.length() >= 1 || days.length() >= 1 || years.length() >= 1) {
				if (input.equals("Back")) {
					years = (days.substring(days.length() >= 1 ? days.length() -1 : days.length())
						  + years.substring(0, years.length() >= 1 ? years.length() -1 : years.length()));
						  
					days  = (month.substring(month.length() >= 1 ? month.length() -1 : month.length())
						  + days.substring(0, days.length() >= 1 ? days.length() -1 : days.length()));
						  
					month = (month.substring(0, month.length() >= 1 ? month.length() -1 : month.length()));
				}
				
				else if (input.equals("Clear")) {
					month = ""; days  = ""; years = "";
				}
				
				else if (input.equals("=")) {
					try {
						int years = Integer.parseInt(inputFormat[2]);
						int month = Integer.parseInt(inputFormat[0]);
						int days  = Integer.parseInt(inputFormat[1]);
						int[] responseOutputData = birthCalculator.evaluate(getApplicationContext(), years, month, days);
						
						yearsOutputData = (TextView) findViewById(R.id.outputYears);
						daysOutputData = (TextView) findViewById(R.id.outputDays);
						
						yearsOutputData.setAlpha(1);
						yearsOutputData.setText(String.valueOf(responseOutputData[0]));
						
						daysOutputData.setAlpha(1);
						daysOutputData.setText(String.valueOf(responseOutputData[1]));
						
					} catch (NumberFormatException nfe) {
						Log.d(nfe.getClass().getName(), nfe.getMessage());
					}
				}
			}
		}
		
		else if (years.length() < inputFormat[2].length()) {
			years = (years + input);
		}
		
		else if (days.length() < inputFormat[1].length()) {
			days  = (days + years.substring(0, 1));
			years = (years.substring(1) + input);
		}
		
		else if (month.length() < inputFormat[0].length()
			&& days.length() == inputFormat[1].length()
			&& years.length() == inputFormat[2].length()) {
			
			month = (month + days.substring(0, 1));
			days  = (days.substring(1) + years.substring(0, 1));
			years = (years.substring(1) + input);
		}
		
		String[] data = new String[3];
		data[0] = month;
		data[1] = days;
		data[2] = years;
		
		return data;
	}
	
	protected boolean isNotNumerical(String data) {
		boolean responseBoolean = false;
		
		try {
			Integer.parseInt(data);
		} catch (NumberFormatException nfe) {
			responseBoolean = true;
		}
		
		return responseBoolean;
	}
	
	protected String getZeros(int countData) {
		String zeroData = "";
		
		while (countData >= 1) {
			zeroData += "0";
			countData--;
		}
		
		return zeroData;
	}

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p1, int p2) {finish();}
		}).setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p1, int p2) {}
		}).setTitle("Exit").setMessage("Do you want to leave ?")
		.setCancelable(false).create().show();
	}
}

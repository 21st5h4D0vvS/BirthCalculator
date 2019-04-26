package rssths.batch2019.birth_calculator;

import java.util.Calendar;
import android.content.Context;
import android.widget.Toast;

public class BirthCalculator
{
	protected int fixedYears, fixedMonth, fixedDays;
	
	public void setFixedDate(int years, int month, int days) {
		fixedYears = years;
		fixedMonth = month;
		fixedDays  = days;
	}
	
	public int[] evaluate(Context context, int years, int month, int days) {
		Calendar calendar = Calendar.getInstance();
		
		int calendarDays[] = getCalendarDays();
		int outputData[]   = new int[2];
		
		int currentMonth = (fixedMonth == 0 ? calendar.get(Calendar.MONTH)		 : fixedMonth) +1;
		int currentDays  = fixedDays  == 0 ? calendar.get(Calendar.DAY_OF_MONTH) : fixedDays;
		int currentYears = fixedYears == 0 ? calendar.get(Calendar.YEAR)		 : fixedYears;

		int evalYear = (currentYears -years);
		int evalDays = 0;

		if (month >= 13) {
			toastMessage(context, "Input Error: Input month is out of range !");
			evalYear = 0;
		} else if (days >= 32) {
			toastMessage(context, "Input Error: Input days is out of range !");
			evalYear = 0;
		} else {
			if (!(currentMonth >= month)
				|| (currentMonth == month
					&& currentDays < days)) {
				evalYear--;
			}
			
			if (currentMonth > month) {
				for (int i = 0; i < calendarDays.length; i++) {
					if ((month -1) == i) {
						evalDays += (calendarDays[i] -days);
					}
					
					if (i > (month -1) && i < (currentMonth -1)) {
						evalDays += calendarDays[i];
					}
					
					if ((currentMonth -1) == i) {
						evalDays += currentDays;
						continue;
					}
				}
			}
			
			if (currentMonth < month) {
				int reverseValue = 0;
				
				for (int i = 0; i < calendarDays.length; i++) {
					if (i == (currentMonth -1)) {
						reverseValue = (calendarDays[i] -currentDays);
					}
					
					if (i > (currentMonth -1) && i < (month -1)) {
						reverseValue += calendarDays[i];
					}
					
					if (i == (month -1)) {
						reverseValue += days;
						continue;
					}
				}
				
				evalDays = (365 - reverseValue);
			}
		}
		
		outputData[0] = evalYear;
		outputData[1] = evalDays;
		
		return outputData;
	}
	
	public void resetFixedDate() {
		fixedMonth = 0;
		fixedDays  = 0;
		fixedYears = 0;
	}
	
	private void toastMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	private int[] getCalendarDays() {
		int calendarDays[] = new int[12];

		calendarDays[0]  = 31; // January
		calendarDays[1]  = 28; // February
		calendarDays[2]  = 31; // March
		calendarDays[3]  = 30; // April
		calendarDays[4]  = 31; // May
		calendarDays[5]  = 30; // June
		calendarDays[6]  = 31; // July
		calendarDays[7]  = 31; // August
		calendarDays[8]  = 30; // September
		calendarDays[9]  = 31; // October
		calendarDays[10] = 30; // November
		calendarDays[11] = 31; // December

		return calendarDays;
	}
}

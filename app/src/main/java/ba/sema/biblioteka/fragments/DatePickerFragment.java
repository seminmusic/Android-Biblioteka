package ba.sema.biblioteka.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;


// Za prevenciju resetovanja selektovanog datuma na pickeru kad se promijeni orijentacija može se koristiti i ovo:
// https://stackoverflow.com/questions/24473252/saving-activity-state-on-screen-orientation-change
// Ovdje je postavljen konstruktor sa Calendar parametrom koji se setuje u activity.


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private OnDateSetListener callbackListener;

    private int year, month, day;

    public DatePickerFragment()
    {

    }
    public DatePickerFragment(Calendar datum)
    {
        this.year = datum.get(Calendar.YEAR);
        this.month = datum.get(Calendar.MONTH);
        this.day = datum.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * An interface containing onDateSet() method signature.
     * Container Activity must implement this interface.
     */
    public interface OnDateSetListener
    {
        void onDateSet(DatePicker view, int year, int month, int day);
    }

    /* (non-Javadoc)
   * @see android.app.DialogFragment#onAttach(android.app.Activity)
   */
    @Override
    public void onAttach(Context activity)
    {
        super.onAttach(activity);

        try
        {
            callbackListener = (OnDateSetListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnDateSetListener.");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Create a new instance of DatePickerDialog and return it:
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        this.year = year;
        this.month = month;
        this.day = day;

        if (callbackListener != null)
        {
            callbackListener.onDateSet(view, year, month, day);
        }
    }
}

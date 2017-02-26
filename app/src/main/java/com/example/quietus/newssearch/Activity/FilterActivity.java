package com.example.quietus.newssearch.Activity;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.quietus.newssearch.Model.Filter;
import com.example.quietus.newssearch.R;

import org.parceler.Parcels;

import java.util.Calendar;

import static com.example.quietus.newssearch.R.id.etDate;

public class FilterActivity extends AppCompatActivity {

    private EditText etDate;
    private Spinner spSort;
    private CheckBox cbArts;
    private CheckBox cbFashion;
    private CheckBox cbSports;
    private Filter filter;
    private Button btnSave;

    private int mYear, mMonth, mDay;
    private String[] saSort = {"oldest", "newest"};
    private ArrayAdapter<String> alSort;
    private boolean isDeskArtsFilter, isDeskFashionFilter, isDeskSportsFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Setting Actionbar title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.Filter);
        // Setting View
        setView();
        // Filter data init
        filter = new Filter();

    }

    private void setView(){
        // EditText Init
        etDate = (EditText)findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.sDate = "";
                showDatePickerDialog();
            }
        });
        // Spinner
        spSort = (Spinner)findViewById(R.id.spSort);
        alSort = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, saSort);
        spSort.setAdapter(alSort);
        // Filtet data about Sort
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter.sSort = saSort[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Checkbox Arts
        cbArts = (CheckBox)findViewById(R.id.cbArts);
        cbArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbArts.isChecked()){
                    isDeskArtsFilter = true;
                }else{
                    isDeskArtsFilter = false;
                }
            }
        });
        // Checkbox fashion
        cbFashion = (CheckBox)findViewById(R.id.cbFashion);
        cbFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbFashion.isChecked()){
                    isDeskFashionFilter = true;
                }else{
                    isDeskFashionFilter = false;
                }
            }
        });
        // Checkbox sports
        cbSports = (CheckBox)findViewById(R.id.cbSports);
        cbSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbSports.isChecked()){
                    isDeskSportsFilter = true;
                }else{
                    isDeskSportsFilter = false;
                }
            }
        });

        // Button save
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                filter.sDesk = "";
                if (isDeskFashionFilter || isDeskArtsFilter || isDeskSportsFilter) {
                    if (isDeskArtsFilter) {
                        filter.sDesk += "\"Arts\",";
                    }
                    if (isDeskFashionFilter) {
                        filter.sDesk += "\"Fashion&Style\",";
                    }
                    if (isDeskSportsFilter) {
                        filter.sDesk += "\"Sports\",";
                    }
                    int length = filter.sDesk.length() - 1;
                    filter.sDesk = filter.sDesk.substring(0, length);
                    filter.sDesk = "news_desk:(" + filter.sDesk + ")";
                }
                intent.putExtra("filter", Parcels.wrap(filter));

                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    public void showDatePickerDialog(){
        // Setting Init Date
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        // create new datepickerdialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                filter.sDate = String.valueOf(year);
                if (month >= 9){
                    filter.sDate += String.valueOf(month + 1);
                }else{
                    filter.sDate += "0";
                    filter.sDate += String.valueOf(month + 1);
                }
                filter.sDate += String.valueOf(dayOfMonth);
                etDate.setText(filter.sDate);

            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }
}

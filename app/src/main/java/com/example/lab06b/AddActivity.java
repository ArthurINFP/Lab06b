package com.example.lab06b;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout loName, loPlace, loDate, loTime;
    TextInputEditText edtName, edtPlace, edtDate, edtTime;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        loName = findViewById(R.id.lo_name);
        loPlace = findViewById(R.id.lo_place);
        loDate = findViewById(R.id.lo_date);
        loTime = findViewById(R.id.lo_time);

        edtName = findViewById(R.id.edt_name);
        edtPlace = findViewById(R.id.edt_place);
        edtDate = findViewById(R.id.edt_date);
        edtTime = findViewById(R.id.edt_time);

        edtPlace.setOnClickListener(this);
        edtDate.setOnClickListener(this);
        edtTime.setOnClickListener(this);

        edtPlace.setFocusable(false);
        edtDate.setFocusable(false);
        edtTime.setFocusable(false);

        Intent intent = getIntent();
        data = (Data) intent.getSerializableExtra("data");
        if (data != null) {
            edtName.setText(data.getTitle());
            edtPlace.setText(data.getRoom());
            edtDate.setText(data.getDate());
            edtTime.setText(data.getTime());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            if (checkEditTexts()) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                Data newData = new Data(edtName.getText().toString(), edtPlace.getText().toString()
                        , edtDate.getText().toString(), edtTime.getText().toString(), false);

                if (data != null) {
                    newData.setState(data.isState());
                    bundle.putSerializable("data", data);
                }
                bundle.putSerializable("new data", newData);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            }
        }
        return false;
    }

    private boolean checkEditTexts() {
        boolean flag = true;
        if (edtName.getText().toString().isEmpty()) {
            flag = false;
            loName.setError("Please enter event name");
        }
        if (edtPlace.getText().toString().isEmpty()) {
            flag = false;
            loPlace.setError("Please enter event place");
        }
        if (edtDate.getText().toString().isEmpty()) {
            flag = false;
            loDate.setError("Please enter event Date");
        }
        if (edtTime.getText().toString().isEmpty()) {
            flag = false;
            loTime.setError("Please enter event time");
        }
        return flag;
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edt_place) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // List of items to show in the dialog
            final String[] rooms = {"C201", "C202", "C203", "C204"};
            final int[] selectedChoice = {-1};
            builder.setSingleChoiceItems(rooms, selectedChoice[0], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedChoice[0] = which; // Update the selected choice
                    edtPlace.setText(rooms[which]);
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (view.getId() == R.id.edt_date) {
            final Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            // Create and show the DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String monthString = (month<10) ? "0"+Integer.toString(month) : Integer.toString(month);
                            edtDate.setText(monthString+"/"+Integer.toString(dayOfMonth)+"/"+Integer.toString(year));
                        }
                    }, year, month, day);
            datePickerDialog.show();
        }
        if (view.getId() == R.id.edt_time) {
            final Calendar c = Calendar.getInstance();
            int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create and show the TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            edtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, hourOfDay, minute, true); // 'true' for 24-hour format
            timePickerDialog.show();
        }
    }
}

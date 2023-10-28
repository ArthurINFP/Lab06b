package com.example.lab06b;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static int REQUEST_CODE = 1;
    private Switch swState;
    private ArrayList<Data> data;
    private RecyclerAdapter adapter;
    private RecyclerView rcv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv = findViewById(R.id.rcv);

        data = new ArrayList<>();
        data.add(new Data("Sinh hoat chu nhiem", "C120", "09/03/2020", "04:43", true));
        data.add(new Data("Huong dan luan van", "C120", "09/03/2020", "04:43", true));
        data.add(new Data("Bao ve do an", "C201", "09/03/2020", "04:43", false));

        adapter = new RecyclerAdapter(this, data);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_item, menu);
        MenuItem item = menu.findItem(R.id.menu_switch);
        item.setActionView(R.layout.menu_switch);
        swState = item.getActionView().findViewById(R.id.sw_state);
        swState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                adapter.setData(b);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add) {
            startActivityForResult(new Intent(this, AddActivity.class), REQUEST_CODE);
            return true;
        }

        if (id == R.id.menu_remove) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to remove all events?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.clearData();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
            return true;
        }
        if (id == R.id.menu_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setMessage("This application was built by Arthur");
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                {
                    adapter.addNewData((Data) data.getSerializableExtra("new data"));
                }
            }
            if (requestCode == RecyclerAdapter.REQUEST_CODE) {

                adapter.editData((Data) data.getSerializableExtra("data")
                        ,(Data) data.getSerializableExtra("new data"));

            }
        }
        else {
            Toast.makeText(this, "Add item false", Toast.LENGTH_SHORT).show();
        }
    }
}
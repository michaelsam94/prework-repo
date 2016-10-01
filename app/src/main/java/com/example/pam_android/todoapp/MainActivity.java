package com.example.pam_android.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.pam_android.todoapp.model.Item;
import com.facebook.stetho.Stetho;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int EDIT_REQUEST = 1;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        //readItems();
        readItemsDB();
        itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);

    }
    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        writeItemsDB();

    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                items.remove(pos);
                Toast.makeText(MainActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                writeItemsDB();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent goToEdit = new Intent(MainActivity.this,EditItemActivity.class);
                goToEdit.putExtra("postion", pos);
                goToEdit.putExtra("Item", items.get(pos));
                startActivityForResult(goToEdit, EDIT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                int pos = data.getIntExtra("postion", 0);
                String res = data.getStringExtra("result");
                items.set(pos, res);
                writeItems();
                writeItemsDB();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListViewListener();
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try{
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try {
            FileUtils.writeLines(todoFile,items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeItemsDB() {
        new Delete().from(Item.class).execute();
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {
                Item item = new Item(items.get(i));
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    private void readItemsDB() {
        items = new ArrayList();
        List<Item> itemsDB = new Select().from(Item.class).execute();
        items = Item.toArrayListOfItems(itemsDB);
    }

}

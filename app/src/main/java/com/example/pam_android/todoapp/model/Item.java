package com.example.pam_android.todoapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 01/10/16.
 */


@Table(name = "Items")
public class Item extends Model {
    @Column(name = "name")
    public String name;

    public Item() {
        super();
    }

    public Item(String name) {
        super();
        this.name = name;
    }

    public static ArrayList<String> toArrayListOfItems(List<Item> items) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            names.add(items.get(i).name);
        }
        return names;
    }
}

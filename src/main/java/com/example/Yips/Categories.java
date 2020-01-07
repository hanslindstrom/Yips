package com.example.Yips;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Categories {

    private List<String> categories = new ArrayList<String>() {
        {
            add("Running");
            add("Swimming");
            add("Cycling");
        }
    };

    public List<String> getCategories() {
        return categories;
    }

}

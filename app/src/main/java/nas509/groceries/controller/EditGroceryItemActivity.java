package nas509.groceries.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nas509.groceries.R;

public class EditGroceryItemActivity extends AppCompatActivity {
    public final static String ARG_GROCERY_ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grocery_item);
    }
}

package nas509.groceries.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.io.File;
import java.util.List;

import nas509.groceries.R;
import nas509.groceries.model.GroceryItem;
import nas509.groceries.model.Model;

public class PaymentsFragment extends Fragment {
    Model model;
    MyListFragment.GroceryItemRecyclerViewAdapter adapter;
    View recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    File filesDir;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payments, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recycler_list_my_list);
        assert recyclerView != null;

        // filesDir = this.getFilesDir();
        model = Model.getInstance();
        // File file = new File(filesDir, PersistenceManager.DEFAULT_TEXT_FILE_NAME);
        // model.loadText(file);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        adapter = new PaymentsFragment()
//        //Step 1.  Setup the recycler view by getting it from our layout in the main window
//
//        //Step 2.  Hook up the adapter to the view
//        setupRecyclerView((RecyclerView) recyclerView);
//        model.retrieveData(adapter, false);
    }

    /**
     * Set up an adapter and hook it to the provided view
     * @param recyclerView  the view that needs this adapter
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }
}

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
import nas509.groceries.model.PaymentsManager;

public class PaymentsFragment extends Fragment {
    static Model model;
    static PaymentsFragment.GroceryItemRecyclerViewAdapter adapter;
    static View recyclerView;
    static SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payments, container, false);
        
        recyclerView = view.findViewById(R.id.recycler_list_payments);
        assert recyclerView != null;
        
        model = Model.getInstance();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshPayments();
            }
        });
        return view;
    }

    public static void refreshPayments() {
        if (model != null) {
            adapter = new PaymentsFragment.GroceryItemRecyclerViewAdapter(model.getPaymentsList());
            System.out.println(adapter.getItemCount());
            onItemsLoadComplete();
        }
    }

    static void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        ((RecyclerView) recyclerView).setAdapter(adapter);
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new PaymentsFragment.GroceryItemRecyclerViewAdapter(model.getPaymentsList());
        //Step 1.  Setup the recycler view by getting it from our layout in the main window

        //Step 2.  Hook up the adapter to the view
        setupRecyclerView((RecyclerView) recyclerView);
    }

    /**
     * Set up an adapter and hook it to the provided view
     * @param recyclerView  the view that needs this adapter
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

    public static class GroceryItemRecyclerViewAdapter
            extends RecyclerView.Adapter<PaymentsFragment.GroceryItemRecyclerViewAdapter.ViewHolder> {

        /**
         * Collection of the items to be shown in this list.
         */
        private final List<String> mPayments;

        /**
         * set the items to be used by the adapter
         * @param items the list of items to be displayed in the recycler view
         */
        public GroceryItemRecyclerViewAdapter(List<String> items) {
            mPayments = items;
        }

        @Override
        public PaymentsFragment.GroceryItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /*

              This sets up the view for each individual item in the recycler display
              To edit the actual layout, we would look at: res/layout/course_list_content.xml
              If you look at the example file, you will see it currently just 2 TextView elements
             */
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_content_payments, parent, false);
            return new PaymentsFragment.GroceryItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PaymentsFragment.GroceryItemRecyclerViewAdapter.ViewHolder holder, int position) {

            final Model model = Model.getInstance();
            /*
            This is where we have to bind each data element in the list (given by position parameter)
            to an element in the view (which is one of our two TextView widgets
             */
            //start by getting the element at the correct position
            /*
              Now we bind the data to the widgets.  In this case, pretty simple, put the id in one
              textview and the string rep of a course in the other.
             */
            holder.mPaymentView.setText(mPayments.get(position));

            /*
             * set up a listener to handle if the user clicks on this list item, what should happen?
             */
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //editGroceryItemDialog(holder.mGroceryItem.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPayments.size();
        }

        /**
         * This inner class represents a ViewHolder which provides us a way to cache information
         * about the binding between the model element (in this case a Course) and the widgets in
         * the list view (in this case the two TextView)
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mPaymentView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPaymentView = (TextView) view.findViewById(R.id.payment);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mPaymentView.getText() + "'";
            }
        }
    }
}

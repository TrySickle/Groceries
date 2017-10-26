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
import android.util.Log;
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

public class SharedListFragment extends Fragment {
    Model model;
    SharedListFragment.GroceryItemRecyclerViewAdapter adapter;
    View recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    File filesDir;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroceryItemDialog();
            }
        });
        recyclerView = view.findViewById(R.id.recycler_list_shared_list);
        assert recyclerView != null;

        // filesDir = this.getFilesDir();
        model = Model.getInstance();
        // File file = new File(filesDir, PersistenceManager.DEFAULT_TEXT_FILE_NAME);
        // model.loadText(file);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        return view;
    }

    void refreshItems() {
        // Load items
        // ...

        // Load
        model.retrieveData(adapter, true);
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        adapter.notifyDataSetChanged();
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new SharedListFragment.GroceryItemRecyclerViewAdapter(model.getGroceryItems());
        //Step 1.  Setup the recycler view by getting it from our layout in the main window

        //Step 2.  Hook up the adapter to the view
        setupRecyclerView((RecyclerView) recyclerView);
        model.retrieveData(adapter, false);
    }

    /**
     * Set up an adapter and hook it to the provided view
     * @param recyclerView  the view that needs this adapter
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

    private void editGroceryItemDialog(final int groceryId) {
        GroceryItem groceryItem = model.getGroceryItemById(groceryId);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Item");

        LayoutInflater inflater = this.getLayoutInflater();
        View viewInflated = inflater.inflate(R.layout.dialog_add_item, null);
        final EditText name = (EditText) viewInflated.findViewById(R.id.name);
        name.setText(groceryItem.getName());
        final EditText price = (EditText) viewInflated.findViewById(R.id.price);
        price.setText(groceryItem.getPrice());
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (name.getText().toString().length() > 0 && price.getText().toString().length() > 0) {
                    model.editGroceryItem(groceryId, name.getText().toString(), price.getText().toString());
                    //File file = new File(filesDir, PersistenceManager.DEFAULT_TEXT_FILE_NAME);
                    //model.saveText(file);
                    adapter.notifyDataSetChanged();
                } else {
                    dialog.cancel();
                }
            }
        });
        builder.setNeutralButton("Delete",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        model.removeGroceryItem(groceryId);
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addGroceryItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Item");

        LayoutInflater inflater = this.getLayoutInflater();
        View viewInflated = inflater.inflate(R.layout.dialog_add_item, null);
        final EditText name = (EditText) viewInflated.findViewById(R.id.name);
        final EditText price = (EditText) viewInflated.findViewById(R.id.price);
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (name.getText().toString().length() > 0 && price.getText().toString().length() > 0) {
                    model.addGroceryItem(new GroceryItem(name.getText().toString(), price.getText().toString(), model.getLoggedInUser().getId(), model.getLoggedInUser().getGroupName()));
                    //File file = new File(filesDir, PersistenceManager.DEFAULT_TEXT_FILE_NAME);
                    //model.saveText(file);
                } else {
                    dialog.cancel();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * This inner class is our custom adapter.  It takes our basic model information and
     * converts it to the correct layout for this view.
     *
     * In this case, we are just mapping the toString of the Course object to a text field.
     */
    public class GroceryItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SharedListFragment.GroceryItemRecyclerViewAdapter.ViewHolder> {

        /**
         * Collection of the items to be shown in this list.
         */
        private final List<GroceryItem> mGroceryItems;

        /**
         * set the items to be used by the adapter
         * @param items the list of items to be displayed in the recycler view
         */
        public GroceryItemRecyclerViewAdapter(List<GroceryItem> items) {
            mGroceryItems = items;
        }

        @Override
        public SharedListFragment.GroceryItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /*

              This sets up the view for each individual item in the recycler display
              To edit the actual layout, we would look at: res/layout/course_list_content.xml
              If you look at the example file, you will see it currently just 2 TextView elements
             */
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_content_shared_list, parent, false);
            return new SharedListFragment.GroceryItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SharedListFragment.GroceryItemRecyclerViewAdapter.ViewHolder holder, int position) {

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
            holder.mNameView.setText(mGroceryItems.get(position).getName());
            holder.mPriceView.setText(mGroceryItems.get(position).getPrice());
            Log.d("bind", position + " " + mGroceryItems.get(position).getId());
            Log.d("bind", Boolean.toString(mGroceryItems.get(position).getWantedBy().contains(model.getLoggedInUser().getId())));
            holder.favoriteButton.setFavorite(mGroceryItems.get(position).containsWantedBy(model.getLoggedInUser()), false);
            holder.dollarButton.setFavorite(mGroceryItems.get(position).containsPurchasedBy(model.getLoggedInUser()), false);
            holder.mGroceryItem = mGroceryItems.get(position);

            /*
             * set up a listener to handle if the user clicks on this list item, what should happen?
             */
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editGroceryItemDialog(holder.mGroceryItem.getId());
                }
            });

            holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.starItem(holder.mGroceryItem.getId(), !holder.favoriteButton.isFavorite());
                    holder.favoriteButton.setFavorite(!holder.favoriteButton.isFavorite());
                }
            });

            holder.dollarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.dollarItem(holder.mGroceryItem.getId(), !holder.dollarButton.isFavorite());
                    holder.dollarButton.setFavorite(!holder.dollarButton.isFavorite());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGroceryItems.size();
        }

        /**
         * This inner class represents a ViewHolder which provides us a way to cache information
         * about the binding between the model element (in this case a Course) and the widgets in
         * the list view (in this case the two TextView)
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mPriceView;
            public final MaterialFavoriteButton favoriteButton;
            public final MaterialFavoriteButton dollarButton;
            public GroceryItem mGroceryItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.name);
                mPriceView = (TextView) view.findViewById(R.id.price);
                favoriteButton = (MaterialFavoriteButton) view.findViewById(R.id.star);
                dollarButton = (MaterialFavoriteButton) view.findViewById(R.id.dollar);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mPriceView.getText() + "'";
            }
        }
    }
}
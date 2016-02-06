package com.todor.diabetes.ui.product_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.todor.diabetes.R;
import com.todor.diabetes.db.ProductFunctionality;
import com.todor.diabetes.models.Product;
import com.todor.diabetes.ui.AddProductActivity;
import com.todor.diabetes.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductListFragment extends BaseFragment implements
//        LoaderManager.LoaderCallbacks<ArrayList<Product>>,
        SearchView.OnQueryTextListener {

    @Bind(R.id.recyclerView) RecyclerView         recyclerView;
    private                  ProductFunctionality dbManager;
    private List<Product>  productList    = null;
    private ProductAdapter productAdapter = null;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddProductActivity.class));
            }
        });

        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        dbManager = new ProductFunctionality(getActivity());

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

//        getActivity().getLoaderManager().initLoader(Constants.PRODUCT_LIST_LOADER, null, this);

        recyclerView.addItemDecoration(new ItemDecorator(getActivity(), R.drawable.divider));
        recyclerView.setItemAnimator(itemAnimator);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        productList = dbManager.getAllProducts();

        productAdapter = new ProductAdapter(productList, new OnProductListItemClickListener() {
            @Override
            public void onProductClick(Product product) {

            }
        });

        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public String getFragmentTitle() {
        return getResources().getString(R.string.title_products);
    }

//    @Override
//    public Loader<ArrayList<Product>> onCreateLoader(int id, Bundle args) {
//        return new ProductLoader(getActivity());
//    }
//
//    @Override
//    public void onLoadFinished(Loader<ArrayList<Product>> loader, ArrayList<Product> data) {
//        productList = data;
//
//        recyclerView.setAdapter(new ProductAdapter(data, new OnProductListItemClickListener() {
//            @Override
//            public void onProductClick(Product product) {
//                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
//                intent.putExtra(Constants.PRODUCT_KEY, product);
//                startActivity(intent);
//            }
//        }));
//    }
//
//    @Override
//    public void onLoaderReset(Loader<ArrayList<Product>> loader) {
//        recyclerView.setAdapter(null);
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Product> filteredModelList = filter(productList, query);
        productAdapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    private List<Product> filter(List<Product> models, String query) {
        query = query.toLowerCase();

        final List<Product> filteredModelList = new ArrayList<>();
        for (Product model : models) {
            final String text = model.name.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        Log.d("ProductListFragment", String.valueOf(productList));
        return filteredModelList;
    }
}

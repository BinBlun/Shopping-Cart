package a2_1901040003.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import a2_1901040003.R;
import a2_1901040003.adapter.ProductAdapter;
import a2_1901040003.database.ProductManager;
import a2_1901040003.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    private RecyclerView productRecyclerView;
    private final List<Product> productsForShopping;
    private TextView searchForProducts;
    private ProductAdapter theProductAdapter;
    private final ProductManager manageTheProduct;

    public ProductFragment(List<Product> productsForShopping, ProductManager manageTheProduct) {
        this.productsForShopping = productsForShopping;
        this.manageTheProduct = manageTheProduct;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_fragment, container, false);
        searchForProducts = v.findViewById(R.id.finding_text);

        productRecyclerView = v.findViewById(R.id.product_recyclerview);

        theProductAdapter = new ProductAdapter(productsForShopping, manageTheProduct);
        productRecyclerView.setAdapter(theProductAdapter);


        productRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        searchForProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return v;
    }

    private void filter(String string) {
        List<Product> products = new ArrayList();
        for (Product product : productsForShopping) {
            if (product.getName().toLowerCase().contains(string)) {
                products.add(product);
            }
        }
        //update the recyclerview
        theProductAdapter.updateList(products);
    }

}


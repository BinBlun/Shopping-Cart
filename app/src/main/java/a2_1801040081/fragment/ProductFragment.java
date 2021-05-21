package a2_1801040081.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import a2_1801040081.R;
import a2_1801040081.adapter.ProductAdapter;
import a2_1801040081.database.ProductManager;
import a2_1801040081.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    private RecyclerView productRv;
    private List<Product> shoppingProducts;
    private TextView searchProducts;
    private ProductAdapter productAdapter;
    private ProductManager productManager;

    public ProductFragment(List<Product> shoppingProducts, ProductManager productManager){
        this.shoppingProducts = shoppingProducts;
        this.productManager = productManager;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        searchProducts = view.findViewById(R.id.search_text);

        productRv = view.findViewById(R.id.product_rv);

        productAdapter = new ProductAdapter(shoppingProducts, productManager);
        productRv.setAdapter(productAdapter);


        productRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        searchProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        return view;
    }

    private void filter(String text){
        List<Product> temp = new ArrayList();
        for(Product product: shoppingProducts){
            if(product.getName().toLowerCase().contains(text)){
                temp.add(product);
            }
        }
        //update recyclerview
        productAdapter.updateList(temp);
    }

}

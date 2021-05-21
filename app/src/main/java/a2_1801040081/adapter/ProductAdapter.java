package a2_1801040081.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import a2_1801040081.MainActivity;
import a2_1801040081.R;
import a2_1801040081.database.ProductManager;
import a2_1801040081.model.Product;
import a2_1801040081.util.CurrencyFormatter;

import java.io.InputStream;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> products;
    private ProductManager productManager;

    public ProductAdapter(List<Product> products, ProductManager productManager) {
        this.products = products;
        this.productManager = productManager;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.product_rv, parent, false );

        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductHolder extends RecyclerView.ViewHolder{
        private ImageView productImg;
        private TextView productName;
        private TextView productPrice;
        private ImageButton addToCart;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            productImg = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            addToCart = itemView.findViewById(R.id.add_to_card);
        }

        public void bind(Product product){
            new DownloadImageTask(productImg).execute(product.getImgUrl());
            productName.setText(product.getName());
            productPrice.setText(CurrencyFormatter.format((long) product.getPrice()));
            productPrice.setWidth(MainActivity.WIDTH / 2 - 100);

            //handle click event later
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productManager.add(product);

                    Toast toast= Toast.makeText(itemView.getContext(),"Item have been added to the cart!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    public void updateList(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

package com.example.sif.sparkle.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sif.sparkle.Product;
import com.example.sif.sparkle.ProductAdaptor;
import com.example.sif.sparkle.ProductDisplayActivity;
import com.example.sif.sparkle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class WomenFragment extends Fragment {


    private RecyclerView recyclerView;
    private ProductAdaptor adaptor;
    private LinearLayoutManager layoutManager;
    private ArrayList<Product> products;
    private Context ctx;
    private ProgressDialog pd;
    private static final String GET_PRODUCTS_URL = "https://techstart.000webhostapp.com/get_products.php";

    public WomenFragment() {}

    public static WomenFragment newInstance(String param1, String param2) {
        WomenFragment fragment = new WomenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_men, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_product);
        layoutManager = new LinearLayoutManager(view.getContext());
        pd = new ProgressDialog(view.getContext());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pd.dismiss();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pd.setTitle("Loading Products");
        pd.setMessage("Please wait...");
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        products = new ArrayList<>();
        adaptor = new ProductAdaptor(products, ctx, new ProductAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(Product product, int position) {
                Intent intent=new Intent(getActivity(), ProductDisplayActivity.class);
                intent.putExtra("product",product);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adaptor);

        sendRequest();
    }

    private void sendRequest() {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_PRODUCTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        SharedPreferences sharedPreferences = getApplicationContext()
                                .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("response", response);
                        editor.commit();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                if(object.getString("gender").equals("female")) {
                                    Product p = new Product(ctx);
                                    p.setId(Integer.parseInt(object.getString("pid")));
                                    p.setProductName(object.getString("pname"));
                                    p.setShortDescription(object.getString("sdesc"));
                                    p.setLongDescription(object.getString("ldesc"));
                                    p.setRating(Double.parseDouble(object.getString("rating")));
                                    p.setPrice(Double.parseDouble(object.getString("price")));
                                    p.setDicount(Double.parseDouble(object.getString("discount")));
                                    p.setSuitedFor(object.getString("gender"));
                                    p.setProductImageUrl(object.getString("purl"));
                                    p.setProduct();
                                    products.add(p);
                                }
                            }
                            adaptor.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, getString(R.string.err_connection), Toast.LENGTH_LONG).show();
                pd.dismiss();
                useCachedResponse();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

    private void useCachedResponse(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
        String response=sharedPreferences.getString("response","-1");

        if(!response.equals("-1")){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Product p = new Product(ctx);
                    p.setId(Integer.parseInt(object.getString("pid")));
                    p.setProductName(object.getString("pname"));
                    p.setShortDescription(object.getString("sdesc"));
                    p.setLongDescription(object.getString("ldesc"));
                    p.setRating(Double.parseDouble(object.getString("rating")));
                    p.setPrice(Double.parseDouble(object.getString("price")));
                    p.setDicount(Double.parseDouble(object.getString("discount")));
                    p.setSuitedFor(object.getString("gender"));
                    p.setProductImageUrl(object.getString("purl"));
                    p.setProduct();
                    products.add(p);
                }
                adaptor.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

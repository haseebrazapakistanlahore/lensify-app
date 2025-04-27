package fyp.com.riona.CartFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import fyp.com.riona.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.CartProductRealmModel;
import fyp.com.riona.GlobalChecks;
import fyp.com.riona.SaleFragment.DiscountsModel;
import fyp.com.riona.SaleFragment.SaleList;

public class CartFragment extends Fragment {

    RealmResults<CartProductRealmModel> rows;
    static RecyclerView recyclerView;
    CartRecyclerAdapter madapter;
    GlobalChecks globalChecks;
    ArrayList<DiscountsModel> discountsModelList;
    Realm realm = Realm.getDefaultInstance();
  //  ProgressDialog progress;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_RecyclerView);
        if (getActivity() != null)
            Realm.init(getActivity());
        rows = realm.where(CartProductRealmModel.class).findAll();


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        rows.load();
        getAllSale();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));







        return view;
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {


//            Intent intent2 = context.getIntent();


            discountsModelList = new ArrayList<>();

            discountsModelList = (ArrayList<DiscountsModel>) intent.getSerializableExtra("LIST");

            if (getActivity() != null)
                Realm.init(getActivity());
                rows = realm.where(CartProductRealmModel.class).findAll();

                rows.load();

            madapter = new CartRecyclerAdapter(getContext(), rows, discountsModelList, getActivity());
            recyclerView.setAdapter(madapter);
            recyclerView.invalidate();





        }
    };



    @Override
    public void onResume() {
        super.onResume();

//        rows.clear();  //Reset before update adapter to avoid duplication of list
//update listofdata


    }

    ////////////////////////////////////////////////////Function that is used to get  Data from Server ////////////////////
    public void getAllSale() {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<SaleList> call = apiService.getAllSale();

        call.enqueue(new Callback<SaleList>() {
            @Override
            public void onResponse(Call<SaleList> call, retrofit2.Response<SaleList> response) {
                final SaleList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                  //  progress.dismiss();
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            //        progress.dismiss();
                                    discountsModelList = new ArrayList<>();


                                  discountsModelList = new ArrayList<>(listofhome.getDiscounts());
                                }
                            });

                        }
                    }

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                              //  progress.dismiss();
                                madapter = new CartRecyclerAdapter(getContext(), rows, discountsModelList, getActivity());
                                recyclerView.setAdapter(madapter);
                                recyclerView.invalidate();

                            }
                        });

                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                           //     progress.dismiss();
                                Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<SaleList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                        //    progress.dismiss();
                            Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            }
        });
    }
}

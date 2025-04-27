package fyp.com.riona.CategoriesFragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.HomeFragment.CategoryDataModel;
import fyp.com.riona.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class CategoriesFragment extends Fragment {

    RecyclerView categories_consumer_recyclerView, categories_pro_recyclerView;
    CategoriesTabListAdapter categoriesTabListAdapter;
    CategoriesTabListProAdapter categoriesTabListAdapterPro;
    ArrayList<CategoryDataModel> dataModelArrayList, proCategories,mylist ;
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        categories_consumer_recyclerView = (RecyclerView) view.findViewById(R.id.categories_tablist_recyclerView);
        categories_pro_recyclerView = (RecyclerView)  view.findViewById(R.id.categories_tablist_pro_recyclerView);
        progress= new ProgressDialog(getActivity());
        progress.setMessage("Loading..");
        progress.show();
        getAllCategories();
        return view;
    }
    ////////////////////////////////////////////////////Function that is used to get Home Data////////////////////
    public void getAllCategories() {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CategoriesList> call = apiService.getAllCategories();

        call.enqueue(new Callback<CategoriesList>() {
            @Override
            public void onResponse(Call<CategoriesList> call, retrofit2.Response<CategoriesList> response) {
                final CategoriesList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {
                        if(getActivity() !=null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                                }
                            });
                        }
                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////


                        mylist = new ArrayList<>(listofhome.getCategories());
                    }

                    if (getActivity() == null) {
                        return;
                    } else {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();
                                proCategories = new ArrayList<>();
                                dataModelArrayList = new ArrayList<>();

                                for(CategoryDataModel d:mylist)
                                {
                                    CategoryDataModel dd=new CategoryDataModel();
                                    String type = d.getType();
                                    String id = d.getId();
                                    String name=d.getTitle();
                                    String immg=d.getImage();
                                    if(type.contains("1"))
                                    {
                                        dd.setId(id);
                                        dd.setTitle(name);
                                        dd.setImage(immg);
                                        dd.setType(type);
                                        proCategories.add(dd);
                                    }
                                    else
                                    {

                                        dd.setId(id);
                                        dd.setTitle(name);
                                        dd.setImage(immg);
                                        dd.setType(type);
                                        dataModelArrayList.add(dd);

                                    }
                                }
                                setupRecycler();
                                setupProRecycler();


                            }
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoriesList> call, Throwable t) {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            progress.dismiss();
                            Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                        }

                    });
                }


            }
        });
    }

    private void setupProRecycler(){
        categoriesTabListAdapterPro = new CategoriesTabListProAdapter(getContext(),proCategories);
        categories_pro_recyclerView.setAdapter(categoriesTabListAdapterPro);
        categories_pro_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


    }
    private void setupRecycler(){


        categoriesTabListAdapter = new CategoriesTabListAdapter(getContext(),dataModelArrayList);
        categories_consumer_recyclerView.setAdapter(categoriesTabListAdapter);
        categories_consumer_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


    }


}

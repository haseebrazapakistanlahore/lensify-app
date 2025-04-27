package fyp.com.riona.TopSellingFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fyp.com.riona.Favourites.FavouriteDataModel;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductsRecyclerAdapter;
import fyp.com.riona.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopSellingFragment extends Fragment {

    private RecyclerView recyclerView;
    SharedPreferences featuredprefernce;
    String topsellinglist;
    ArrayList<FeaturedProductDataModel> dataModelArrayList;

    ////////////// List For Favourite Product Data Model ///////////////////////////////////

    ArrayList<FavouriteDataModel> dataModelArrayList_favrt = new ArrayList<>();


    SharedPreferences favertprefernce;
    String favertlist;
    ArrayList<FeaturedProductDataModel> dataModelArrayList_final = new ArrayList<>();
    public TopSellingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_selling, container, false);
        recyclerView = view.findViewById(R.id.topSelling_product_recyclerView);


        if(getActivity()!=null)
        {
            featuredprefernce = getActivity().getSharedPreferences("homelist", Context.MODE_PRIVATE);
            topsellinglist  = featuredprefernce.getString("topSellingProductDataModelArrayList",null);

            Gson gson = new Gson();
            Type type = new TypeToken<List<FeaturedProductDataModel>>(){}.getType();
            dataModelArrayList  = gson.fromJson(topsellinglist, type);



            /////////// Shared Prefernce for Favort Product /////////////////////

            favertprefernce = getActivity().getSharedPreferences("favrtListhead", Context.MODE_PRIVATE);

            favertlist  = favertprefernce.getString("favrtList",null);
            Gson gson2 = new Gson();
            Type type2 = new TypeToken<List<FavouriteDataModel>>(){}.getType();
            dataModelArrayList_favrt  = gson2.fromJson(favertlist, type2);




            if(dataModelArrayList!=null && !dataModelArrayList.isEmpty()) {
                ////////////////////////////////////////////Set Data to  Recycler View///////////////////////////////////////////////////////////////

                if(dataModelArrayList_favrt!=null && !dataModelArrayList_favrt.isEmpty())
                {


                    for (int index = 0; index < dataModelArrayList.size(); index++) {

                        for (int index2 = 0; index2 < dataModelArrayList_favrt.size(); index2++) {

                            String id = dataModelArrayList_favrt.get(index2).getId();
                            if (dataModelArrayList.get(index).getId().equals(id)) {
                                String inner_id = dataModelArrayList.get(index).getId();
                                String title = dataModelArrayList.get(index).getTitle();
                                String price = dataModelArrayList.get(index).getPrice();
                                String offerPrice = dataModelArrayList.get(index).getOfferPrice();
                                String offerAvailable = dataModelArrayList.get(index).getOfferAvailable();
                                String thumbnail = dataModelArrayList.get(index).getThumbnail();
                                String productType = dataModelArrayList.get(index).getProductType();
                                FeaturedProductDataModel model = new FeaturedProductDataModel();
                                model.setId(inner_id);
                                model.setTitle(title);
                                model.setFavrt_bit("1");
                                model.setOfferAvailable(offerAvailable);
                                model.setOfferPrice(offerPrice);
                                model.setPrice(price);
                                model.setThumbnail(thumbnail);
                                model.setProductType(productType);
                                dataModelArrayList.set(index, model);


                            }

                        }
                    }



                    FeaturedProductsRecyclerAdapter adapter = new FeaturedProductsRecyclerAdapter(getActivity(),dataModelArrayList , getActivity());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager( new GridLayoutManager(getActivity(),2));

                }
                else
                {
                    setupRecycler();

                }


            }





        }
        return view;
    }
    private void setupRecycler(){

        FeaturedProductsRecyclerAdapter adapter = new FeaturedProductsRecyclerAdapter(getActivity(),dataModelArrayList , getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager( new GridLayoutManager(getActivity(),2));
    }

}

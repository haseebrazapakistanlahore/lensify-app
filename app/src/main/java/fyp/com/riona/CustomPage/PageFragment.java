package fyp.com.riona.CustomPage;


import android.app.ProgressDialog;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.R;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    View view;
    TextView page_title, page_description;
    String title, id;
    ProgressDialog progress;
    PageDetailsModel d;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_page, container, false);
        page_title = (TextView) view.findViewById(R.id.page_title);
        page_description = (TextView) view.findViewById(R.id.page_description);

        title = getArguments().getString("page_title" , "");
        id = getArguments().getString("page_id" , "");
        progress= new ProgressDialog(getActivity());
        progress.setMessage("Loading..");
        if (!title.isEmpty() || !id.isEmpty()){

            page_title.setText(title);
            progress.show();
            getPageDetails(id);


        }




        return view;
    }

    ////////////////////////////////////////////////////Function that is used to get Home Data////////////////////
    public void getPageDetails(String id) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<PageDetailsList> call = apiService.getPageById(id);

        call.enqueue(new Callback<PageDetailsList>() {
            @Override
            public void onResponse(Call<PageDetailsList> call, retrofit2.Response<PageDetailsList> response) {
                final PageDetailsList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {
                        if(getActivity() !=null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (progress.isShowing()) {
                                        progress.dismiss();
                                    }
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                                }
                            });
                        }
                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                      d= listofhome.getPage();

                    }

                    if (getActivity() == null) {
                        return;
                    } else {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                              if(d.getTitle()!=null && !d.getTitle().isEmpty())
                              {
                                  page_title.setText(d.getTitle());

                              }
                              if(d.getDescription()!=null && !d.getDescription().isEmpty())
                              {
                                  page_description.setText(android.text.Html.fromHtml(d.getDescription().toString()));
                              }
                                if (progress.isShowing()) {
                                    progress.dismiss();
                                }
                            }
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progress.isShowing()) {
                                    progress.dismiss();
                                }
                                Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<PageDetailsList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }

                            Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                        }

                    });
                }


            }
        });
    }

}

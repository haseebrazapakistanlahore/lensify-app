package fyp.com.riona.ProfileFragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.CartPayment.CartShippingList;
import fyp.com.riona.Login.LoginActivity;

import fyp.com.riona.R;

import fyp.com.riona.UserAddress;
import fyp.com.riona.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    CircleImageView user_img, pick_user_img;
    EditText et_user_name, et_user_email, et_phone_number, et_user_company, et_user_company_address, et_user_company_website;
    Button btn_update_info, btn_add_address;
    TextView tv_user_type, tv_user_name;
    LinearLayout layoutofpro_comapny;
    ProgressDialog progress;
    SharedPreferences user_preference;
    SharedPreferences.Editor user_preferenceEditor;
    String user_type ;
    String user_token;
    private RecyclerView recyclerView;
    ProfileDataModel profileDataModel;
    private String userChoosenTask;
    private ImageView user_imageView;
    private int REQUEST_CAMERA = 0;
    Uri selectedImage;
    String newtokenconsumer, newtokenprofessional;

    //  private Activity activity = getActivity();
    private Bitmap bitmap;
    SharedPreferences user_prefernce;
    SharedPreferences.Editor user_prefernce_editor;
    ArrayList<UserAddress> dataModelArrayList;
    String update_name, update_phone, update_comapny_name, update_company_address, update_company_website;
    String address, country, city;
     Dialog dialog;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.shippingAddress_recyclerView);
        user_img = view.findViewById(R.id.user_img_pro);
        pick_user_img = view.findViewById(R.id.pick_user_img);
        tv_user_type = view.findViewById(R.id.profile_type);
        tv_user_name = view.findViewById(R.id.profile_name);
        et_user_name = view.findViewById(R.id.profile_username);
        et_user_email = view.findViewById(R.id.profile_email);
        et_phone_number = view.findViewById(R.id.profile_phoneNumber);
        et_user_company = view.findViewById(R.id.pro_company_name);
        et_user_company_address = view.findViewById(R.id.pro_company_address);
        layoutofpro_comapny = view.findViewById(R.id.profile_pro_layout);
        btn_update_info = view.findViewById(R.id.update_profile_info);
        btn_add_address = view.findViewById(R.id.bt_add_address_profile);
        et_user_company_website = view.findViewById(R.id.pro_company_website);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Please Wait");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (getActivity() != null)
            user_preference = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = user_preference.getString("user_type", "");
        user_token = user_preference.getString("token", null);
        if (user_type != null && !user_type.isEmpty()) {

                newtokenconsumer = "bearer   " + user_token;
                progress.show();
                Consumer_Profile(newtokenconsumer);

        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {


                   dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_add_edit_address_layout);

                    ImageView imageViewDialogClose = dialog.findViewById(R.id.close_dialogCustomAddress);
                    final EditText et_addressDialog = dialog.findViewById(R.id.editText_Address_custom_dialog);
                    final EditText et_cityDialog = dialog.findViewById(R.id.editText_city_custom_dialog);
                    final EditText et_countryDialog = dialog.findViewById(R.id.editText_country_custom_dialog);
                    Button btn_proceedDialog = dialog.findViewById(R.id.btn_proceed_customDialogAddress);

                    btn_proceedDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            address = et_addressDialog.getText().toString().trim();
                            country = et_countryDialog.getText().toString().trim();
                            city = et_cityDialog.getText().toString().trim();
                            String address_type = "1";
                            if (user_type != null && !user_type.isEmpty()) {

                                    if (address == null || address.isEmpty()) {
                                        Toast.makeText(getActivity(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                                    } else if (country == null || country.isEmpty()) {
                                        Toast.makeText(getActivity(), "Please Enter Country", Toast.LENGTH_SHORT).show();
                                    } else if (city == null || city.isEmpty()) {
                                        Toast.makeText(getActivity(), "Please Enter City", Toast.LENGTH_SHORT).show();
                                    } else
                                    {
                                        newtokenconsumer = "bearer   " + user_token;
                                        progress.show();
                                        AddShippingAddressforConsumer(newtokenconsumer, address, country, city, address_type);
                                    }

                            } else {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }

//                            progress.show();

                        }
                    });

                    imageViewDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        });


        btn_update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_name = et_user_name.getText().toString();
                update_phone = et_phone_number.getText().toString();
                update_comapny_name = et_user_company.getText().toString();
                update_company_address = et_user_company_address.getText().toString();
                update_company_website = et_user_company_website.getText().toString();
                if (user_type != null && !user_type.isEmpty()) {
                    if (user_type.contains("1")) {
                        String newtoken = "bearer   " + user_token;
                        if (selectedImage == null) {
                            if (update_name == null || update_name.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();

                            }
                            else if(update_phone == null || update_phone.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Phone", Toast.LENGTH_SHORT).show();
                            }
                            else if(update_comapny_name == null || update_comapny_name.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Company Name", Toast.LENGTH_SHORT).show();
                            }
                            else if(update_company_address == null || update_company_address.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Company Address", Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                progress.show();
                                Professionalupdatedatawithoutimage(newtoken, update_name, update_phone, update_comapny_name, update_company_website, update_company_address);

                            }
                                } else {
                            if (update_name == null || update_name.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();

                            }
                            else if(update_phone == null || update_phone.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Phone", Toast.LENGTH_SHORT).show();
                            }
                            else if(update_comapny_name == null || update_comapny_name.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Company Name", Toast.LENGTH_SHORT).show();
                            }
                            else if(update_company_address == null || update_company_address.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Company Address", Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                progress.show();
                                Professionalupdatedatawithimage(newtoken, update_name, update_phone, selectedImage, update_comapny_name, update_company_website, update_company_address);

                            }

                        }
                    } else {
                        String newtoken = "bearer   " + user_token;
                        if (selectedImage == null) {
                            if(update_name==null || update_name.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                            }
                            else if(update_phone==null || update_phone.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Phone", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progress.show();
                                Consumerupdatedatawithoutimage(newtoken, update_name, update_phone);
                            }
                        } else {
                            if(update_name==null || update_name.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                            }
                            else if(update_phone==null || update_phone.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Please Enter Phone", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progress.show();
                                Consumerupdatedatawithimage(newtoken, update_name, update_phone, selectedImage);
                            }

                        }

                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }


            }
        });
        pick_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                selectImage();
            }
        });
        return view;
    }

    /////////////////////////////////////////Add Shipping Address for Consumer////////////////////////////////////
    private void AddShippingAddressforConsumer(String token, String address, String country, String city, String address_type) {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CartShippingList> call = apiService.addConsumerShippingAddress(token, address, city, country, address_type);

        call.enqueue(new Callback<CartShippingList>() {
            @Override
            public void onResponse(Call<CartShippingList> call, Response<CartShippingList> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final CartShippingList listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                                //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                editor.clear(); //clear all stored data
                                editor.apply();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });


                    } else if (status.contains("0")) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Maximum Address Already Added", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getActivity(), "Shipping Address Added Successfully", Toast.LENGTH_SHORT).show();
                                dataModelArrayList = new ArrayList<>(listFoodModel.getUserAddress());
                                user_prefernce = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);
                                user_prefernce_editor = user_prefernce.edit();
                                Gson gson = new Gson();
                                if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                    String listofbillingaddress = gson.toJson(dataModelArrayList);
                                    user_prefernce_editor.putString("addressarraylist", listofbillingaddress);
                                }
                                user_prefernce_editor.apply();
                                dialog.dismiss();
                                FragmentTransaction ftr = getFragmentManager().beginTransaction();
                                ftr.detach(ProfileFragment.this).attach(ProfileFragment.this).commit();
                            }

                        });


                    }

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<CartShippingList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                    }
                });
            }
        });


    }

    ///////////////////////////Function that is used to Get Consumer  Data to Server/////////////////////////////////
    public void Consumer_Profile(String token) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Profile_Data_List> call = apiService.get_consumer_profile(token);

        call.enqueue(new Callback<Profile_Data_List>() {
            @Override
            public void onResponse(Call<Profile_Data_List> call, Response<Profile_Data_List> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final Profile_Data_List listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                                    //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                    editor.clear(); //clear all stored data
                                    editor.apply();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getActivity().startActivity(intent);
                                }
                            });
                        }

                    } else if (status.contains("0")) {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Invalid Email or Password", Snackbar.LENGTH_LONG).show();
                                }
                            });

//                        Snackbar.make(getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_LONG).show();
                    } /*else if (status == 3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();

                            }
                        });

                    }*/ else {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    profileDataModel = listFoodModel.getConsumer();
                                    String name = profileDataModel.getFullName();
                                    String email = profileDataModel.getEmail();
                                    String phone = profileDataModel.getPhone();
                                    String imageUrl = profileDataModel.getProfileImage();
                                    dataModelArrayList = (ArrayList<UserAddress>) profileDataModel.getAddresses();
                                    if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                        recyclerView.setAdapter(new ShippingAddressRecyclerAdapter(getActivity(), dataModelArrayList, getActivity()));
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    if (name != null && !name.isEmpty()) {
                                        tv_user_name.setText(name);
                                        tv_user_type.setText("Consumer");
                                        et_user_name.setText(name);
                                    }
                                    if (email != null && !email.isEmpty()) {
                                        et_user_email.setText(email);
                                    } else {
                                        et_user_email.setVisibility(View.GONE);
                                    }
                                    if (phone != null && !phone.isEmpty()) {
                                        et_phone_number.setText(phone);
                                    } else {
                                        et_phone_number.setVisibility(View.GONE);
                                    }
                                    layoutofpro_comapny.setVisibility(View.GONE);
                                    if (imageUrl != null && !imageUrl.isEmpty()) {
                                        Glide.with(getActivity())
                                                .load(getActivity().getResources().getString(R.string.url) + imageUrl).placeholder(R.drawable.placeholder)
                                                 
                                                .dontAnimate()
                                                .into(user_img);
                                    }

                                    if (getActivity() != null) {
                                        user_preference = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);
                                        user_preferenceEditor = user_preference.edit();
                                        user_preferenceEditor.putString("user_type", String.valueOf(user_type));
                                        user_preferenceEditor.putString("name", name);
                                        user_preferenceEditor.putString("email", email);
                                        user_preferenceEditor.putString("phone", phone);
                                        user_preferenceEditor.putString("url", imageUrl);
                                        user_preferenceEditor.apply();
                                    }


                                }

                            });


                    }

                } else {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                            }
                        });
                }

            }

            @Override
            public void onFailure(Call<Profile_Data_List> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                        }
                    });
            }
        });
    }


    //////////RunTime Permissions//////////////
    public void requestPermission() {
        XXPermissions.with(getActivity())
                //.constantRequest() //Can be set to continue to apply after being rejected until the user authorizes or permanently rejects
                //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //Support request 6.0 floating window permission 8.0 request installation permission
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE) //Automatically get dangerous permissions in the list without specifying permissions
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (allGranted) {
                            //   Toast.makeText(DailyProgressActivity.this, "Get Permissions success", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toasty.error(SubmitStartInActivity.this, "The permission is successfully obtained, and some permissions are not granted normally.", Toast.LENGTH_SHORT, true).show();
                            Toast.makeText(getActivity(), "The permission is successfully obtained, and some permissions are not granted normally.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            // Toasty.error(SubmitStartInActivity.this, "Authorized to be denied permanently, please grant permission manually", Toast.LENGTH_SHORT, true).show();
                            Toast.makeText(getActivity(), "Authorized to be denied permanently, please grant permission manually", Toast.LENGTH_SHORT).show();
//                            getActivity().finish();
                            refres();
                            //If it is permanently rejected, jump to the application rights system settings page
                        } else {
                            // Toasty.error(SubmitStartInActivity.this, "Failed to get permission", Toast.LENGTH_SHORT, true).show();
                            Toast.makeText(getActivity(), "Failed to get permission", Toast.LENGTH_SHORT).show();
//                            getActivity().finish();
                            refres();

                        }
                        OnPermissionCallback.super.onDenied(permissions, doNotAskAgain);
                    }

//                    @Override
//                    public void hasPermission(List<String> granted, boolean isAll) {
//                        if (isAll) {
//                            //   Toast.makeText(DailyProgressActivity.this, "Get Permissions success", Toast.LENGTH_SHORT).show();
//                        } else {
//                            //Toasty.error(SubmitStartInActivity.this, "The permission is successfully obtained, and some permissions are not granted normally.", Toast.LENGTH_SHORT, true).show();
//                            Toast.makeText(getActivity(), "The permission is successfully obtained, and some permissions are not granted normally.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void noPermission(List<String> denied, boolean quick) {
//                        if (quick) {
//                            // Toasty.error(SubmitStartInActivity.this, "Authorized to be denied permanently, please grant permission manually", Toast.LENGTH_SHORT, true).show();
//                            Toast.makeText(getActivity(), "Authorized to be denied permanently, please grant permission manually", Toast.LENGTH_SHORT).show();
////                            getActivity().finish();
//                            refres();
//
//                            //If it is permanently rejected, jump to the application rights system settings page
//                            XXPermissions.gotoPermissionSettings(getActivity());
//                        } else {
//                            // Toasty.error(SubmitStartInActivity.this, "Failed to get permission", Toast.LENGTH_SHORT, true).show();
//                            Toast.makeText(getActivity(), "Failed to get permission", Toast.LENGTH_SHORT).show();
////                            getActivity().finish();
//                            refres();
//
//                        }
//                    }
                });

    }
    //////////////////////For Taking  Picture Permission////////////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    // else if (userChoosenTask.equals("Choose from Gallery"))
                    //   galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    //////////////////////For Taking Picture Process////////////////////
    private void selectImage() {
        final CharSequence[] items = {"Take Photo",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                }
                /*else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();

                } */
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    ///////////////////For Taking Picture From Camera ////////////

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    ///////////////////For Taking Picture From Camera or Gallery  ////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            //   else if (requestCode == REQUEST_CAMERA)
            //   onCaptureImageResult(data);
        }
    }

    ///////////////////For  Picture From Camera ////////////

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        selectedImage = getImageUri(getActivity(), thumbnail);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());

            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        user_img.setImageBitmap(thumbnail);

    }

    ///////////////////Funcation Used For Get Imge Uri from Bitmap ////////////

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*
     * This method is fetching the absolute path of the image file
     * if you want to upload other kind of files like .pdf, .docx
     * you need to make changes on this method only
     * Rest part will be the same
     * */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    ////////////////////////////////////////Update Consumer Profile///////////////////////////////////////////
    public void Consumerupdatedatawithimage(String token, String name, String phone, Uri fileUri) {
        //creating a file
        File file = new File(getRealPathFromURI(fileUri));

        //creating request body for file

        RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), file);


        /////////////////// Statment That Convert to Body Request Api Parameter  ////////////

        RequestBody full_name = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody phone2 = RequestBody.create(MediaType.parse("text/plain"), phone);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Profile_Data_List> call = apiService.consumerupdate(token, full_name, phone2, requestFile);

        call.enqueue(new Callback<Profile_Data_List>() {

            // mapiinter.sendSign(name,email,phone,pass,uni,depart,token,spinnervalue).enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<Profile_Data_List> call, Response<Profile_Data_List> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final Profile_Data_List listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                                    //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                    editor.clear(); //clear all stored data
                                    editor.apply();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getActivity().startActivity(intent);
                                }
                            });
                        }

                    } else if (status.contains("0")) {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });

//                        Snackbar.make(getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_LONG).show();
                    } /*else if (status == 3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();

                            }
                        });

                    }*/ else {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                    profileDataModel = listFoodModel.getConsumer();
                                    String name = profileDataModel.getFullName();
                                    String email = profileDataModel.getEmail();
                                    String phone = profileDataModel.getPhone();
                                    String imageUrl = profileDataModel.getProfileImage();
                                    dataModelArrayList = (ArrayList<UserAddress>) profileDataModel.getAddresses();
                                    if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                        recyclerView.setAdapter(new ShippingAddressRecyclerAdapter(getActivity(), dataModelArrayList, getActivity()));
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    if (name != null && !name.isEmpty()) {
                                        tv_user_name.setText(name);
                                        tv_user_type.setText("Consumer");
                                        et_user_name.setText(name);
                                    }
                                    if (email != null && !email.isEmpty()) {
                                        et_user_email.setText(email);
                                    } else {
                                        et_user_email.setVisibility(View.GONE);
                                    }
                                    if (phone != null && !phone.isEmpty()) {
                                        et_phone_number.setText(phone);
                                    } else {
                                        et_phone_number.setVisibility(View.GONE);
                                    }
                                    layoutofpro_comapny.setVisibility(View.GONE);
                                    if (imageUrl != null && !imageUrl.isEmpty()) {

                                        Glide.with(getActivity())
                                                .load(getActivity().getResources().getString(R.string.url) + imageUrl).placeholder(R.drawable.placeholder)
                                                 
                                                .dontAnimate()
                                                .into(user_img);

                                        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                                        View hview = navigationView.getHeaderView(0);
                                        user_imageView = (ImageView) hview.findViewById(R.id.imageView_main);
                                        Glide.with(getActivity())
                                                .load(getActivity().getResources().getString(R.string.url) + imageUrl).placeholder(R.drawable.placeholder)
                                                 
                                                .dontAnimate()
                                                .into(user_imageView);
                                    }

                                    if (getActivity() != null) {
                                        user_preference = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);
                                        user_preferenceEditor = user_preference.edit();
                                        user_preferenceEditor.putString("user_type", String.valueOf(user_type));
                                        user_preferenceEditor.putString("name", name);
                                        user_preferenceEditor.putString("email", email);
                                        user_preferenceEditor.putString("phone", phone);
                                        user_preferenceEditor.putString("url", imageUrl);
                                        user_preferenceEditor.apply();
                                    }


                                }

                            });


                    }

                } else {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                            }
                        });
                }

            }

            @Override
            public void onFailure(Call<Profile_Data_List> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                        }
                    });
            }
        });
    }


///////////////////////////////////Consumer Profile update without image///////////////////////////////////

    ////////////////////////////////////////Update Consumer Profile///////////////////////////////////////////
    public void Consumerupdatedatawithoutimage(String token, String name, String phone) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Profile_Data_List> call = apiService.consumerupdatewithoutimage(token, name, phone);

        call.enqueue(new Callback<Profile_Data_List>() {

            // mapiinter.sendSign(name,email,phone,pass,uni,depart,token,spinnervalue).enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<Profile_Data_List> call, Response<Profile_Data_List> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final Profile_Data_List listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                                    //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                    editor.clear(); //clear all stored data
                                    editor.apply();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getActivity().startActivity(intent);
                                }
                            });
                        }

                    } else if (status.contains("0")) {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });

//                        Snackbar.make(getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_LONG).show();
                    } /*else if (status == 3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();

                            }
                        });

                    }*/ else {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                    profileDataModel = listFoodModel.getConsumer();
                                    String name = profileDataModel.getFullName();
                                    String email = profileDataModel.getEmail();
                                    String phone = profileDataModel.getPhone();
                                    String imageUrl = profileDataModel.getProfileImage();
                                    dataModelArrayList = (ArrayList<UserAddress>) profileDataModel.getAddresses();
                                    if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                        recyclerView.setAdapter(new ShippingAddressRecyclerAdapter(getActivity(), dataModelArrayList, getActivity()));
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    if (name != null && !name.isEmpty()) {
                                        tv_user_name.setText(name);
                                        tv_user_type.setText("Consumer");
                                        et_user_name.setText(name);
                                    }
                                    if (email != null && !email.isEmpty()) {
                                        et_user_email.setText(email);
                                    } else {
                                        et_user_email.setVisibility(View.GONE);
                                    }
                                    if (phone != null && !phone.isEmpty()) {
                                        et_phone_number.setText(phone);
                                    } else {
                                        et_phone_number.setVisibility(View.GONE);
                                    }
                                    layoutofpro_comapny.setVisibility(View.GONE);
                                    if (imageUrl != null && !imageUrl.isEmpty()) {
                                        Glide.with(getActivity())
                                                .load(getActivity().getResources().getString(R.string.url) + imageUrl).placeholder(R.drawable.placeholder)
                                                 
                                                .dontAnimate()
                                                .into(user_img);
                                    }

                                    if (getActivity() != null) {
                                        user_preference = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);
                                        user_preferenceEditor = user_preference.edit();
                                        user_preferenceEditor.putString("user_type", String.valueOf(user_type));
                                        user_preferenceEditor.putString("name", name);
                                        user_preferenceEditor.putString("email", email);
                                        user_preferenceEditor.putString("phone", phone);
                                        user_preferenceEditor.putString("url", imageUrl);
                                        user_preferenceEditor.apply();
                                    }


                                }

                            });


                    }

                } else {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                            }
                        });
                }

            }

            @Override
            public void onFailure(Call<Profile_Data_List> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                        }
                    });
            }
        });
    }


    //////////////////////////Update Professional Profile with image /////////////////////////////
    ////////////////////////////////////////Update Consumer Profile///////////////////////////////////////////
    public void Professionalupdatedatawithimage(String token, String name, String phone, Uri fileUri, String company_name, String company_website, String company_address) {
        //creating a file
        File file = new File(getRealPathFromURI(fileUri));

        //creating request body for file

        RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), file);


        /////////////////// Statment That Convert to Body Request Api Parameter  ////////////

        RequestBody full_name = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody phone2 = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody company_name2 = RequestBody.create(MediaType.parse("text/plain"), company_name);
        RequestBody company_address2 = RequestBody.create(MediaType.parse("text/plain"), company_address);
        RequestBody company_website2 = RequestBody.create(MediaType.parse("text/plain"), company_website);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Profile_Data_List> call = apiService.professionalupdate(token, full_name, phone2, requestFile, company_name2, company_website2, company_address2);

        call.enqueue(new Callback<Profile_Data_List>() {


            @Override
            public void onResponse(Call<Profile_Data_List> call, Response<Profile_Data_List> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final Profile_Data_List listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                                    //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                    editor.clear(); //clear all stored data
                                    editor.apply();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getActivity().startActivity(intent);
                                }
                            });
                        }

                    } else if (status.contains("0")) {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });

//                        Snackbar.make(getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_LONG).show();
                    } /*else if (status == 3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();

                            }
                        });

                    }*/ else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                    profileDataModel = listFoodModel.getProfessional();
                                    String name = profileDataModel.getFullName();
                                    String email = profileDataModel.getEmail();
                                    String phone = profileDataModel.getPhone();
                                    String imageUrl = profileDataModel.getProfileImage();
                                    String company_name = profileDataModel.getCompany_name();
                                    String company_address = profileDataModel.getCompany_address();
                                    String company_website = profileDataModel.getCompany_website();
                                    String discount_allowed = profileDataModel.getDiscount_allowed();
                                    String min_order_value = profileDataModel.getMin_order_value();
                                    String discount_value = profileDataModel.getDiscount_value();
                                    dataModelArrayList = (ArrayList<UserAddress>) profileDataModel.getAddresses();
                                    if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                        recyclerView.setAdapter(new ShippingAddressRecyclerAdapter(getActivity(), dataModelArrayList, getActivity()));
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    if (name != null && !name.isEmpty()) {
                                        tv_user_name.setText(name);
                                        tv_user_type.setText("Professional");
                                        et_user_name.setText(name);
                                    }
                                    if (email != null && !email.isEmpty()) {
                                        et_user_email.setText(email);
                                    } else {
                                        et_user_email.setVisibility(View.GONE);
                                    }
                                    if (phone != null && !phone.isEmpty()) {
                                        et_phone_number.setText(phone);
                                    } else {
                                        et_phone_number.setVisibility(View.GONE);
                                    }


                                    if (company_name != null && !company_name.isEmpty()) {
                                        et_user_company.setText(company_name);
                                    } else {
                                        et_user_company.setVisibility(View.GONE);
                                    }

                                    if (company_address != null && !company_address.isEmpty()) {
                                        et_user_company_address.setText(company_address);
                                    } else {
                                        et_user_company_address.setVisibility(View.GONE);
                                    }
                                    if (company_website != null && !company_website.isEmpty()) {
                                        et_user_company_website.setText(company_website);
                                    } else {
                                        et_user_company_website.setVisibility(View.GONE);
                                    }

                                    if (imageUrl != null && !imageUrl.isEmpty()) {
                                        Glide.with(getActivity())
                                                .load(getActivity().getResources().getString(R.string.url) + imageUrl).placeholder(R.drawable.placeholder)
                                                 
                                                .dontAnimate()
                                                .into(user_img);


                                        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                                        View hview = navigationView.getHeaderView(0);
                                        user_imageView = (ImageView) hview.findViewById(R.id.imageView_main);
                                        Glide.with(getActivity())
                                                .load(getActivity().getResources().getString(R.string.url) + imageUrl).placeholder(R.drawable.placeholder)
                                                 
                                                .dontAnimate()
                                                .into(user_imageView);
                                    }

                                    if (getActivity() != null) {
                                        user_preference = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);
                                        user_preferenceEditor = user_preference.edit();
                                        user_preferenceEditor.putString("user_type", String.valueOf(user_type));
                                        user_preferenceEditor.putString("name", name);
                                        user_preferenceEditor.putString("email", email);
                                        user_preferenceEditor.putString("phone", phone);
                                        user_preferenceEditor.putString("url", imageUrl);
                                        user_preferenceEditor.putString("company_name", company_name);
                                        user_preferenceEditor.putString("company_address", company_address);
                                        user_preferenceEditor.putString("company_website", company_website);
                                        user_preferenceEditor.putString("discount_allowed", discount_allowed);
                                        user_preferenceEditor.putString("min_order_value", min_order_value);
                                        user_preferenceEditor.putString("discount_value", discount_value);
                                        user_preferenceEditor.apply();
                                    }

                                }
                            });
                        }


                    }

                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<Profile_Data_List> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                        }
                    });
            }
        });
    }

    /////////////////////Update Professional without image///////////////////////////////////////
////////////////////////////////////////Update Consumer Profile///////////////////////////////////////////
    public void Professionalupdatedatawithoutimage(String token, String name, String phone, String company_name, String company_website, String company_address) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Profile_Data_List> call = apiService.professionalupdatewithoutimage(token, name, phone, company_name, company_website, company_address);

        call.enqueue(new Callback<Profile_Data_List>() {

            // mapiinter.sendSign(name,email,phone,pass,uni,depart,token,spinnervalue).enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<Profile_Data_List> call, Response<Profile_Data_List> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final Profile_Data_List listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                                    //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                    editor.clear(); //clear all stored data
                                    editor.apply();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getActivity().startActivity(intent);
                                }
                            });
                        }

                    } else if (status.contains("0")) {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });

//                        Snackbar.make(getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_LONG).show();
                    } /*else if (status == 3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();

                            }
                        });

                    }*/ else {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                    profileDataModel = listFoodModel.getProfessional();
                                    String name = profileDataModel.getFullName();
                                    String email = profileDataModel.getEmail();
                                    String phone = profileDataModel.getPhone();
                                    String imageUrl = profileDataModel.getProfileImage();
                                    String company_name = profileDataModel.getCompany_name();
                                    String company_address = profileDataModel.getCompany_address();
                                    String company_website = profileDataModel.getCompany_website();
                                    String discount_allowed = profileDataModel.getDiscount_allowed();
                                    String min_order_value = profileDataModel.getMin_order_value();
                                    String discount_value = profileDataModel.getDiscount_value();
                                    dataModelArrayList = (ArrayList<UserAddress>) profileDataModel.getAddresses();
                                    if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                        recyclerView.setAdapter(new ShippingAddressRecyclerAdapter(getActivity(), dataModelArrayList, getActivity()));
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    if (name != null && !name.isEmpty()) {
                                        tv_user_name.setText(name);
                                        tv_user_type.setText("Professional");
                                        et_user_name.setText(name);
                                    }
                                    if (email != null && !email.isEmpty()) {
                                        et_user_email.setText(email);
                                    } else {
                                        et_user_email.setVisibility(View.GONE);
                                    }
                                    if (phone != null && !phone.isEmpty()) {
                                        et_phone_number.setText(phone);
                                    } else {
                                        et_phone_number.setVisibility(View.GONE);
                                    }


                                    if (company_name != null && !company_name.isEmpty()) {
                                        et_user_company.setText(company_name);
                                    } else {
                                        et_user_company.setVisibility(View.GONE);
                                    }

                                    if (company_address != null && !company_address.isEmpty()) {
                                        et_user_company_address.setText(company_address);
                                    } else {
                                        et_user_company_address.setVisibility(View.GONE);
                                    }
                                    if (company_website != null && !company_website.isEmpty()) {
                                        et_user_company_website.setText(company_website);
                                    } else {
                                        et_user_company_website.setVisibility(View.GONE);
                                    }

                                    if (imageUrl != null && !imageUrl.isEmpty()) {
                                        Glide.with(getActivity())
                                                .load(getActivity().getResources().getString(R.string.url) + imageUrl).placeholder(R.drawable.placeholder)
                                                 
                                                .dontAnimate()
                                                .into(user_img);
                                    }

                                    if (getActivity() != null) {
                                        user_preference = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);
                                        user_preferenceEditor = user_preference.edit();
                                        user_preferenceEditor.putString("user_type", String.valueOf(user_type));
                                        user_preferenceEditor.putString("name", name);
                                        user_preferenceEditor.putString("email", email);
                                        user_preferenceEditor.putString("phone", phone);
                                        user_preferenceEditor.putString("url", imageUrl);
                                        user_preferenceEditor.putString("company_name", company_name);
                                        user_preferenceEditor.putString("company_address", company_address);
                                        user_preferenceEditor.putString("company_website", company_website);
                                        user_preferenceEditor.putString("discount_allowed", discount_allowed);
                                        user_preferenceEditor.putString("min_order_value", min_order_value);
                                        user_preferenceEditor.putString("discount_value", discount_value);
                                        user_preferenceEditor.apply();
                                    }

                                }
                            });
                    }


                } else {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                            }
                        });
                }

            }

            @Override
            public void onFailure(Call<Profile_Data_List> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                        }
                    });
            }
        });
    }

    public void refres() {
        ProfileFragment searchFragment = new ProfileFragment();
        FragmentTransaction fragmentTransactionSearch = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransactionSearch.replace(R.id.fragment_container, searchFragment, "Fragment_Search");
        fragmentTransactionSearch.addToBackStack(null);
        fragmentTransactionSearch.commit();
    }
}

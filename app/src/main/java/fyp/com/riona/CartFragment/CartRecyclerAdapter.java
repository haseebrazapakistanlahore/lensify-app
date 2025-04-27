package fyp.com.riona.CartFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.cardview.widget.CardView;
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

import java.io.Serializable;
import java.text.DecimalFormat;
import com.bumptech.glide.Glide;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.CartPayment.CartPaymentActivity;
import fyp.com.riona.CartProductRealmModel;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MainActivity.MainActivity;
import fyp.com.riona.ProductDetails.ProductDetailsActivity;
import fyp.com.riona.R;
import fyp.com.riona.SaleFragment.DiscountsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;


class CartRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    List<CartProductRealmModel> cartRealmModelList;
    Context context;
    RealmResults<CartProductRealmModel> rows,row2;
    Realm realm = Realm.getDefaultInstance();
    public static Dialog loading;
    ArrayList<DiscountsModel> discountsModelList;

    SharedPreferences sharedPreferences;

    double grossTotal = 0;
    double calculatedDiscount = 0;
    double calculatedSlabDiscount = 0;
    double calculatedUserDiscount = 0;
    double calculatedCouponDiscount = 0;

    String userType;
    String productType;
     String userToken,newtoken;
    String userDiscountAllowed;
    String userMinOrderValue;
    String userDiscountPercentage;

    String couponCode = "";
    String discountId = "";

    SharedPreferences user_preference;
    SharedPreferences caritem_preference,cart_payment_prefernce;
    SharedPreferences.Editor caritem_preferenceEditor,cart_payment_prefernceEditor;
    Activity act;

    public CartRecyclerAdapter(Context context, List<CartProductRealmModel> cartRealmModelList, ArrayList<DiscountsModel> discountSlabList, Activity act) {
        this.context = context;
        this.cartRealmModelList = cartRealmModelList;
        this.discountsModelList = discountSlabList;
        this.act = act;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler_item, parent, false);
            return new ItemViewHolder(itemView);

        } else if (viewType == TYPE_FOOTER) {
            calculateTotal();
            //Inflating footer view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cart_total_view, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Realm.init(context);
        rows = realm.where(CartProductRealmModel.class).findAll();
        rows.load();
        user_preference = context.getSharedPreferences("signupdata", MODE_PRIVATE);
        userType = user_preference.getString("user_type", "");
        userToken = user_preference.getString("token", "");
        // islogin = user_preference.getString("islogin", "");
        if (holder instanceof FooterViewHolder) {
            final FooterViewHolder footerHolder = (FooterViewHolder) holder;
            ///////////////// Calculation for Discount between max and min///////////////////////
            //////////////// Discount for All is null ////////////



            for (DiscountsModel d : discountsModelList) {
                String maxDiscountLimit = d.getMaxAmount();
                String minDiscountLimit = d.getMinAmount();

                String discount_percentage = d.getDiscountPercentage();


                if (grossTotal <= Integer.valueOf(maxDiscountLimit) && grossTotal >= Integer.valueOf(minDiscountLimit)) {
                    calculatedSlabDiscount = grossTotal * Float.valueOf(discount_percentage) / 100;
                    discountId = d.getId();
                }
            }

            userDiscountAllowed = user_preference.getString("discount_allowed", "");
            userMinOrderValue = user_preference.getString("min_order_value", "");
            userDiscountPercentage = user_preference.getString("discount_value", "");

            if (userDiscountAllowed.contains("1")) {
                float minimumOrderLimit = Float.parseFloat(userMinOrderValue);

                if (grossTotal >= minimumOrderLimit) {
                    float discountPercentage = Float.parseFloat(userDiscountPercentage) / 100;
                    calculatedUserDiscount = grossTotal * discountPercentage;
                }
            }
            SharedPreferences mypref2 = context.getSharedPreferences("flagforcoupanbuttondisable", MODE_PRIVATE);
            String couponapplied = mypref2.getString("couponapplied", "");
            if(couponapplied!=null && !couponapplied.isEmpty())
            {
                if(couponapplied.contains("1"))
                {
                    String oldcouponcode = mypref2.getString("couponcode","");
                    double oldcouponDiscountPercentage = Double.parseDouble(mypref2.getString("coupondiscountpercentage","")) ;
                    calculatedCouponDiscount = grossTotal*oldcouponDiscountPercentage;
                    couponCode = oldcouponcode;
                    footerHolder.linearLayout.setVisibility(View.GONE);
                }

            }


            // check highest coupon among calculatedUserDiscount, calculatedCouponDiscount and calculatedSlabDiscount

            if (calculatedUserDiscount > calculatedCouponDiscount && calculatedUserDiscount > calculatedSlabDiscount) {
                calculatedDiscount = calculatedUserDiscount;

            } else if (calculatedCouponDiscount > calculatedSlabDiscount && calculatedCouponDiscount > calculatedUserDiscount) {
                calculatedDiscount = calculatedCouponDiscount;

            } else {
                calculatedDiscount = calculatedSlabDiscount;

            }
            DecimalFormat precision = new DecimalFormat("0.00");
            footerHolder.totall.setText(String.valueOf(precision.format(grossTotal)));
            footerHolder.discount_tv.setText(String.valueOf((precision.format(calculatedDiscount))));
            footerHolder.net_total_tv.setText(String.valueOf(precision.format(grossTotal - calculatedDiscount)));


            footerHolder.apply_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(userType!=null && !userType.isEmpty())
                    {
                        newtoken = "bearer   " + userToken;

                        if (footerHolder.coupon_text.getText().toString().length() > 0) {
                            String code = footerHolder.coupon_text.getText().toString();
                            getCoupancode(newtoken,grossTotal, code);

                        } else if (footerHolder.coupon_text.getText().toString().length() == 0) {
                            footerHolder.coupon_text.setError("No Code");

                        }
                    }
                    else
                    {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                }
            });

            footerHolder.btn_continue_shopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    userToken = sharedPreferences.getString("access_token", "");
                    userType = sharedPreferences.getString("UserType", "");

                    if (userType != null && !userType.isEmpty()) {

                            context.startActivity(new Intent(context, MainActivity.class));

                    } else {
                        context.startActivity(new Intent(context, MainActivity.class));
                    }

                }
            });

            footerHolder.proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!rows.isEmpty()) {
                        if (userType != null && !userType.isEmpty()) {
                            proceedToCheckOut(grossTotal, calculatedDiscount);
                        } else {
                            Toast.makeText(context, "Login First", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                }
            });


        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Glide.with(context)
                    .load(context.getResources().getString(R.string.url) + cartRealmModelList.get(position ).getThumbnail()).placeholder(R.drawable.placeholder)

                    .dontAnimate()
                    .into(itemViewHolder.product_image);
            itemViewHolder.product_name.setText(cartRealmModelList.get(position).getTitle());
            // itemViewHolder.product_decsription.setText(cartRealmModelList.get(position).getDescription());
            itemViewHolder.cart_product_quantity.setText(cartRealmModelList.get(position).getProduct_quantity());

            productType = cartRealmModelList.get(position).getProduct_type();
            if (cartRealmModelList.get(position).getOffer_price().equals("0")) {

                itemViewHolder.price_off.setVisibility(View.GONE);
                if (userType != null && !userType.isEmpty()) {


                        itemViewHolder.product_price.setText(cartRealmModelList.get(position).getPrice());

                }
                else
                {
                    itemViewHolder.product_price.setText(cartRealmModelList.get(position).getPrice());
                }


            } else {

                itemViewHolder.price_off.setVisibility(View.VISIBLE);


                    itemViewHolder.product_price.setText(cartRealmModelList.get(position).getOffer_price());


                itemViewHolder.price_off.setText(cartRealmModelList.get(position).getPrice());


            }

            itemViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    realm.beginTransaction();
                    rows.deleteFromRealm(position);
                    realm.commitTransaction();
                    //  notifyItemRemoved(position-1);
                    realm.refresh();

                    refresh(discountsModelList);

                    //   ((AddorRemoveCallbacks) context).onRemoveProduct();



                    long count = realm.where(CartProductRealmModel.class).count();

                    ((MainActivity) Objects.requireNonNull(context)).addBadgeView(Integer.parseInt(String.valueOf(count)));
                    caritem_preference =context.getSharedPreferences("cartitem", MODE_PRIVATE);
                    caritem_preferenceEditor = caritem_preference.edit();
                    caritem_preferenceEditor.putString(
                            "cartValue", String.valueOf(count));
                    caritem_preferenceEditor.apply();


                }
            });

            final CartProductRealmModel toEdit = realm.where(CartProductRealmModel.class)
                    .equalTo("id", cartRealmModelList.get(position).getId()).findFirst();


            if (Integer.parseInt(cartRealmModelList.get(position).getProduct_quantity()) < Integer.parseInt(cartRealmModelList.get(position).getAvailable_quantity()) || Integer.parseInt(cartRealmModelList.get(position).getProduct_quantity()) == 1) {

                itemViewHolder.add.setVisibility(View.VISIBLE);

                itemViewHolder.add.setEnabled(true);
                itemViewHolder.cart_product_quantity.setEnabled(true);

                itemViewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String productOfferPriceAdd = cartRealmModelList.get(position).getOffer_price();
                        if (productOfferPriceAdd.equals("0")) {
                            itemViewHolder.add.setEnabled(true);
                            int plusVal = Integer.parseInt(toEdit.getProduct_quantity());
                            plusVal += 1;

                            realm.beginTransaction();
                            toEdit.setProduct_quantity(String.valueOf(plusVal));

                            if (userType.contains("1") && productType.contains("0")) {

                                String price = cartRealmModelList.get(position).getPrice();
                                float newSubTotal = Float.valueOf(price) * plusVal;
                                float discPercentage = newSubTotal * 10 / 100;
                                float productTotal = newSubTotal - discPercentage;

                                toEdit.setProduct_total(String.valueOf(productTotal));
                                realm.commitTransaction();
                            } else {
                                toEdit.setProduct_total(String.valueOf(Float.valueOf(cartRealmModelList.get(position).getPrice()) * plusVal));
                                realm.commitTransaction();
                            }
                            itemViewHolder.cart_product_quantity.setText(cartRealmModelList.get(position).getProduct_quantity());

//                            rows.addChangeListener(callback);




                            refresh(discountsModelList);

                        } else {
                            itemViewHolder.add.setEnabled(true);
                            int plusVal = Integer.parseInt(toEdit.getProduct_quantity());
                            plusVal += 1;
                            realm.beginTransaction();
                            toEdit.setProduct_quantity(String.valueOf(plusVal));
                            if (userType.contains("1") && productType.contains("0")) {
                                String price = cartRealmModelList.get(position).getOffer_price();
                                float newSubTotal = Float.valueOf(price) * plusVal;
                                float discPercentage = newSubTotal * 10 / 100;
                                float productTotal = newSubTotal - discPercentage;
                                toEdit.setProduct_total(String.valueOf(productTotal));
                                realm.commitTransaction();

                            } else {
                                toEdit.setProduct_total(String.valueOf(Float.valueOf(cartRealmModelList.get(position ).getOffer_price()) * plusVal));
                                realm.commitTransaction();
                            }

                            itemViewHolder.product_price.setText(cartRealmModelList.get(position ).getOffer_price());
                            itemViewHolder.cart_product_quantity.setText(cartRealmModelList.get(position).getProduct_quantity());

                            refresh(discountsModelList);

                        }

                    }
                });


            } else if (cartRealmModelList.get(position).getProduct_quantity().equals(cartRealmModelList.get(position).getAvailable_quantity())) {

                itemViewHolder.add.setEnabled(false);
                itemViewHolder.cart_product_quantity.setEnabled(false);
                itemViewHolder.add.setVisibility(View.INVISIBLE);

            }


            if (Integer.parseInt(cartRealmModelList.get(position).getAvailable_quantity()) > Integer.parseInt(toEdit.getProduct_quantity()) || Integer.parseInt(cartRealmModelList.get(position).getAvailable_quantity()) == Integer.parseInt(toEdit.getProduct_quantity())) {
                itemViewHolder.cart_product_quantity.setEnabled(true);
                itemViewHolder.subtract.setEnabled(true);

                itemViewHolder.subtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String productOfferPriceSub = cartRealmModelList.get(position).getOffer_price();
                        if (productOfferPriceSub.equals("0")) {
                            if (Integer.parseInt(cartRealmModelList.get(position).getProduct_quantity()) > 1) {
                                int minusVal = Integer.parseInt(toEdit.getProduct_quantity());
                                minusVal -= 1;
                                realm.beginTransaction();
                                toEdit.setProduct_quantity(String.valueOf(minusVal));

                                    toEdit.setProduct_total(String.valueOf(Float.valueOf(cartRealmModelList.get(position).getPrice()) * minusVal));
                                    realm.commitTransaction();


                                itemViewHolder.product_price.setText(cartRealmModelList.get(position).getPrice());
                                itemViewHolder.cart_product_quantity.setText(cartRealmModelList.get(position).getProduct_quantity());

                                refresh(discountsModelList);


                            } else if (Integer.parseInt(cartRealmModelList.get(position).getProduct_quantity()) == 1) {
                                itemViewHolder.cart_product_quantity.setEnabled(false);
                                itemViewHolder.subtract.setEnabled(false);
                            }

                        } else {
                            if (Integer.parseInt(cartRealmModelList.get(position).getProduct_quantity()) > 1) {
                                int minusVal = Integer.parseInt(toEdit.getProduct_quantity());
                                minusVal -= 1;
                                realm.beginTransaction();
                                toEdit.setProduct_quantity(String.valueOf(minusVal));

                                    toEdit.setProduct_total(String.valueOf(Float.valueOf(cartRealmModelList.get(position).getOffer_price()) * minusVal));
                                    realm.commitTransaction();


                                itemViewHolder.product_price.setText(cartRealmModelList.get(position).getOffer_price());
                                itemViewHolder.cart_product_quantity.setText(cartRealmModelList.get(position).getProduct_quantity());



                                refresh(discountsModelList);


                            } else if (Integer.parseInt(cartRealmModelList.get(position).getProduct_quantity()) == 1) {
                                itemViewHolder.cart_product_quantity.setEnabled(false);
                                itemViewHolder.subtract.setEnabled(false);
                            }
                        }
                    }

                });


            } else if (itemViewHolder.cart_product_quantity.getText().toString().equals("1")) {


                itemViewHolder.cart_product_quantity.setEnabled(false);
                itemViewHolder.subtract.setEnabled(false);
            }


            itemViewHolder.cart_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    String id =cartRealmModelList.get(position).getId();
                    intent.putExtra("product_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            });


        }
    }



    public void swap(List<DiscountsModel> data ) {


        Intent intent = new Intent("custom-message");
        intent.putExtra("LIST", (Serializable) data);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }



    @Override
    public int getItemViewType(int position) {
        int size =cartRealmModelList.size();
        if (position==size) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return cartRealmModelList.size() +1;
    }





    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView totall, discount_tv, net_total_tv;
        LinearLayout linearLayout;
        EditText coupon_text;
        Button apply_coupon, proceed, btn_continue_shopping;

        public FooterViewHolder(View view) {
            super(view);
            totall = (TextView) itemView.findViewById(R.id.total);

            proceed = (Button) itemView.findViewById(R.id.fragmentCart_proceed);
            apply_coupon = (Button) view.findViewById(R.id.apply_coupon);
            coupon_text = (EditText) view.findViewById(R.id.coupon_text);
            discount_tv = (TextView) view.findViewById(R.id.discount_tv_my);
            net_total_tv = (TextView) view.findViewById(R.id.nettotal_tv_my);
            btn_continue_shopping = (Button) view.findViewById(R.id.cart_continue_shopping);
            linearLayout = (LinearLayout)view.findViewById(R.id.promo_layout);

        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView product_price, product_name, price_off;
        ImageView product_image;
        public Button add, subtract;
        TextView cart_product_quantity;
        CardView cart_card;
        ImageView delete;

        //Header
        TextView totall, proceed;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cart_product_quantity = (TextView) itemView.findViewById(R.id.cart_product_quantity_txt);
            product_price = (TextView) itemView.findViewById(R.id.cart_product_origional_price);
            product_name = (TextView) itemView.findViewById(R.id.cart_product_name);
            price_off = (TextView) itemView.findViewById(R.id.cart_product_off_price);
            product_image = (ImageView) itemView.findViewById(R.id.cart_product_image);
            delete = itemView.findViewById(R.id.cart_product_delete_btn);
            add = (Button) itemView.findViewById(R.id.cart_product_increase_btn);
            subtract = (Button) itemView.findViewById(R.id.cart_product_decrease_btn);
            cart_card = itemView.findViewById(R.id.cart_detail_product);
            totall = (TextView) itemView.findViewById(R.id.total);
            cart_product_quantity.setFocusable(false);
            cart_product_quantity.setClickable(false);
        }

    }





    public void refresh(List<DiscountsModel> data) {



        swap(data);


    }


    public void calculateTotal() {
        Realm.init(context);

        rows = realm.where(CartProductRealmModel.class).findAll();
        for (CartProductRealmModel cartRealmModel : rows) {
            if (cartRealmModel.getOffer_price().equals("0"))
            {
                productType = cartRealmModel.getProduct_type();
                if(userType!=null && !userType.isEmpty()) {

                        double productPrice = Double.parseDouble(cartRealmModel.getPrice());
                        double productQuantity = Double.parseDouble(cartRealmModel.getProduct_quantity());
                        double subTotal = productPrice * productQuantity;
                        grossTotal += (float) subTotal;

                }else {
                    double productPrice = Double.parseDouble(cartRealmModel.getPrice());
                    double productQuantity = Double.parseDouble(cartRealmModel.getProduct_quantity());
                    double subTotal = productPrice * productQuantity;
                    grossTotal += (float) subTotal;
                }

            }
            else
            {
                productType = cartRealmModel.getProduct_type();
                if(userType!=null && !userType.isEmpty()) {

                        double productPrice = Double.parseDouble(cartRealmModel.getOffer_price());
                        double productQuantity = Double.parseDouble(cartRealmModel.getProduct_quantity());
                        double subTotal = productPrice * productQuantity;
                        grossTotal += (float) subTotal;

                }
                else
                {
                    double productPrice = Double.parseDouble(cartRealmModel.getOffer_price());
                    double productQuantity = Double.parseDouble(cartRealmModel.getProduct_quantity());
                    double subTotal = productPrice * productQuantity;
                    grossTotal += (float) subTotal;
                }

            }


        }


    }
    ////////////////////////////////////////////////////Function that is used to get  Data from Server ////////////////////
    public void getCoupancode(String userToken, final double grossTotal, final String code) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CouponList> call = apiService.check_coupan_code(userToken,code);

        call.enqueue(new Callback<CouponList>() {
                         @Override
                         public void onResponse(Call<CouponList> call, retrofit2.Response<CouponList> response) {
                             final CouponList listofhome = response.body();

                             if (listofhome != null) {
                                 final String status = listofhome.getStatus();
                                 final String msg= listofhome.getMessage();
                                 if(act!=null)
                                 {
                                     act.runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             if (status.contains("0")) {


                                                 Toast.makeText(context, ""+msg, Toast.LENGTH_LONG).show();

                                             }
                                             else if(status.contains("7"))
                                             {
                                                 Toast.makeText(context, ""+msg, Toast.LENGTH_LONG).show();
                                             }

                                             else if(status.contains("8"))
                                             {
                                                 Toast.makeText(context, ""+msg, Toast.LENGTH_LONG).show();
                                             }
                                             else {


                                                 ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                                                 couponCode = code;
                                                 double couponDiscount = Double.parseDouble(listofhome.getDiscountPercentage());
                                                 double couponDiscountPercentage = couponDiscount / 100;
                                                 calculatedCouponDiscount = couponDiscountPercentage * grossTotal;
                                                 SharedPreferences spp2 = context.getSharedPreferences("flagforcoupanbuttondisable", MODE_PRIVATE);
                                                 SharedPreferences.Editor edit2 = spp2.edit();
                                                 edit2.putString("couponapplied", "1");
                                                 edit2.putString("couponcode",couponCode);
                                                 edit2.putString("coupondiscountpercentage",String.valueOf(couponDiscountPercentage));
                                                 edit2.apply();

                                                 CartFragment cartProfilePaymentFragment = new CartFragment();
                                                 FragmentTransaction fragmentTransactionCartProfilePayment = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                                                 fragmentTransactionCartProfilePayment.replace(R.id.fragment_container, cartProfilePaymentFragment, "Fragment_Cart");
                                                 fragmentTransactionCartProfilePayment.addToBackStack(null);
                                                 fragmentTransactionCartProfilePayment.commit();


                                             }
                                         }
                                     });
                                 }



                             } else {
                                 if(act!=null)
                                 {
                                     act.runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             Toast.makeText(context, "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                                         }
                                     });
                                 }



                             }
                         }



                         @Override
                         public void onFailure(Call<CouponList> call, Throwable t) {
                             /////Progress Dialog Dismiss  /////

                             if(act!=null)
                             {
                                 act.runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         Toast.makeText(context, "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                                     }
                                 });
                             }

                         }
                     }
        );
    }

    public void proceedToCheckOut(double grossTotal, double calculatedDiscount) {

        cart_payment_prefernce= context.getSharedPreferences("testcartdata", MODE_PRIVATE);
        cart_payment_prefernceEditor = cart_payment_prefernce.edit();
        cart_payment_prefernceEditor.putString("grossTotal",  String.valueOf(grossTotal));
        cart_payment_prefernceEditor.putString("calculatedDiscount", String.valueOf(calculatedDiscount));
        cart_payment_prefernceEditor.putString("couponCode", String.valueOf(couponCode));
        cart_payment_prefernceEditor.putString("couponDiscount",  String.valueOf(calculatedCouponDiscount));
        cart_payment_prefernceEditor.putString("discountSlabId", String.valueOf(discountId));
        cart_payment_prefernceEditor.putString("discountSlabAmount", String.valueOf(calculatedSlabDiscount));
        cart_payment_prefernceEditor.putString("userFixedDiscount", String.valueOf(calculatedUserDiscount));
        cart_payment_prefernceEditor.apply();
        Intent intent = new Intent(context, CartPaymentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



}
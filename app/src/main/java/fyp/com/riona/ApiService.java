package fyp.com.riona;


import fyp.com.riona.CartFragment.CouponList;
import fyp.com.riona.CartPayment.CartPaymentActivity;
import fyp.com.riona.CartPayment.CartShippingList;
import fyp.com.riona.CartPayment.OrderPost;
import fyp.com.riona.CategoriesFragment.CategoriesList;
import fyp.com.riona.CustomPage.PageDetailsList;
import fyp.com.riona.Favourites.Favourite_List;
import fyp.com.riona.HomeFragment.ConsumerHomeList;
import fyp.com.riona.Login.User_List_Login;
import fyp.com.riona.MainActivity.PagesList;
import fyp.com.riona.MyOrders.OrderList;
import fyp.com.riona.Notification.NotificationList;
import fyp.com.riona.OrderDetail.OrderDetailsList;
import fyp.com.riona.ProductDetails.ProductDetailsList;
import fyp.com.riona.ProductDetails.ReviewSubmitList;
import fyp.com.riona.ProfileFragment.Profile_Data_List;
import fyp.com.riona.SaleFragment.SaleList;
import fyp.com.riona.SeeAllProducts.ForConsumer.ConsumerProductsList;
import fyp.com.riona.SeeAllProducts.ProductbysubinnercatList;
import fyp.com.riona.SignUp.ConsumerUserList;
import fyp.com.riona.SubCategory.Sub_Cat_List;
import fyp.com.riona.SubInnerCategory.SubInnerList;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiService {


    ///////////////////////////Function that is used to Send Tourist_SignUp Data to Server/////////////////////////////////
    @FormUrlEncoded
    @POST("api/consumer/signUp")
    Call<ConsumerUserList> Consumer_SignUp(@Field("email") String email, @Field("full_name") String full_name, @Field("password") String password, @Field("phone") String phone, @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("api/consumer/forgotPassword")
    Call<User_List_Login> Consumer_ForgotPassword(@Field("email") String email);


    ///////////////////////////Function that is used to Send Consumer_Login Data to Server/////////////////////////////////
    @FormUrlEncoded
    @POST("api/consumer/signIn")
    Call<User_List_Login> Consumer_Login(@Field("email") String email, @Field("password") String password, @Field("device_id") String device_id);

    ///////////////////////////Function that is used to Send Commenet  Data to Server/////////////////////////////////


    @POST("api/consumer/getProfile")
    Call<Profile_Data_List> get_consumer_profile(@Header("Authorization") String token);

    ///////////////////////////Function that is used to get Home Page Data to Server/////////////////////////////////
    @POST("api/consumer/getHomeData")
    Call<ConsumerHomeList> getHomeDataforConsumer();

    ///////////////////////////Function that is used to get Detail of Custom page/////////////////////////////////
    @FormUrlEncoded
    @POST("api/getPageDetail")
    Call<PageDetailsList> getPageById(@Field("page_id") String id);

    ///////////////////////////Function that is used to get Home Custom Navigation Page Data to Server/////////////////////////////////
    @POST("api/getPages")
    Call<PagesList> getCustomPages();

    ///////////////////////////Function that is used to get All Categories Data to Server/////////////////////////////////
    @POST("api/category/getAll")
    Call<CategoriesList> getAllCategories();


    ////////////////////////Funcation that is used to get Sub Category  by category id //////////////////////////////
    @FormUrlEncoded
    @POST("api/product/byCategoryId")
    Call<Sub_Cat_List> getByCatId(@Field("category_id") String id);

    ////////////////////////Funcation that is used to get Sub Inner Category  by sub_category id //////////////////////////////
    @FormUrlEncoded
    @POST("api/product/bySubCategoryId")
    Call<SubInnerList> getBySubCatId(@Field("sub_category_id") String id);


    @FormUrlEncoded
    @POST("api/product/bySubChildCategoryId")
    Call<ProductbysubinnercatList> getBySubChildCategoryId(@Field("sub_child_category_id") String id);

    ///////////////////////////Function that is used to get All Sale or Discount Data to Server/////////////////////////////////
    @POST("api/getDiscounts")
    Call<SaleList> getAllSale();

    ///////////////////////////Function that is used to get All Sale or Discount Data to Server/////////////////////////////////
    @POST("api/consumer/getMiscellaneousData")
    @FormUrlEncoded
    Call<CartPaymentActivity.MiscellaneousModel> getconsumerdeliverycharges(@Header("Authorization") String token, @Field("net_total") String net_total);


    ///////////////////////////Function that is used to Add Favourite Consumer   Data to Server/////////////////////////////////

    @FormUrlEncoded
    @POST("api/consumer/addFavourite")
    Call<Favrt_List> add_favrt_consumer(@Header("Authorization") String token, @Field("product_id") String product_id);

    @POST("api/consumer/getAllFavourite")
    Call<Favourite_List> get_favrt_consumer(@Header("Authorization") String token);

    @POST("api/consumer/getNotifications")
    Call<NotificationList> get_notification_consumer(@Header("Authorization") String token);

    ///////////////////////////Function that is used to Delete Favourite Consumer  Data to Server/////////////////////////////////
    @FormUrlEncoded
    @POST("api/consumer/removeFavourite")
    Call<Favrt_List> delete_favrt_consumer(@Header("Authorization") String token, @Field("product_id") String product_id);

    @Multipart
    @POST("api/consumer/updateProfile")
    Call<Profile_Data_List> consumerupdate(@Header("Authorization") String token, @Part("full_name") RequestBody full_name, @Part("phone") RequestBody phone, @Part("profile_image\"; filename=\"user-image.jpg\" ") RequestBody file);

    @FormUrlEncoded
    @POST("api/consumer/updateProfile")
    Call<Profile_Data_List> consumerupdatewithoutimage(@Header("Authorization") String token, @Field("full_name") String full_name, @Field("phone") String phone);

    @Multipart
    @POST("api/professional/updateProfile")
    Call<Profile_Data_List> professionalupdate(@Header("Authorization") String token, @Part("full_name") RequestBody full_name, @Part("phone") RequestBody phone, @Part("profile_image\"; filename=\"user-image.jpg\" ") RequestBody file, @Part("company_name") RequestBody company_name, @Part("company_website") RequestBody company_website, @Part("company_address") RequestBody company_address);

    @FormUrlEncoded
    @POST("api/professional/updateProfile")
    Call<Profile_Data_List> professionalupdatewithoutimage(@Header("Authorization") String token, @Field("full_name") String full_name, @Field("phone") String phone, @Field("company_name") String company_name, @Field("company_website") String company_website, @Field("company_address") String company_address);

    @FormUrlEncoded
    @POST("api/product/detail")
    Call<ProductDetailsList> getProductDetailsbyId(@Field("product_id") String id);


    @POST("api/product/consumer")
    Call<ConsumerProductsList> getConsumerProducts();


    @FormUrlEncoded
    @POST("api/consumer/addReview")
    Call<ReviewSubmitList> add_review_product_consumer(@Header("Authorization") String token, @Field("product_id") String product_id, @Field("rating") String rating, @Field("comment") String comment);

    @FormUrlEncoded
    @POST("api/consumer/address/update")
    Call<CartShippingList> updateConsumerShippingAddress(@Header("Authorization") String token, @Field("address_id") String address_id, @Field("address") String address, @Field("city") String city, @Field("country") String country, @Field("address_type") String address_type);


    ///////////////////////////Function that is used to Check Coupan  Data to Server/////////////////////////////////

    @FormUrlEncoded
    @POST("api/checkCoupon")
    Call<CouponList> check_coupan_code(@Header("Authorization") String token, @Field("coupon_code") String coupon_code);

    @FormUrlEncoded
    @POST("api/consumer/address/add")
    Call<CartShippingList> addConsumerShippingAddress(@Header("Authorization") String token, @Field("address") String address, @Field("city") String city, @Field("country") String country, @Field("address_type") String address_type);

    @POST("api/consumer/order/place")
    Call<fyp.com.riona.CartPayment.OrderList> sendorderforconsumer(@Header("Authorization") String token,
                                                                   @Query("gross_total") String gross_total,
                                                                   @Query("net_total") String net_total,
                                                                   @Query("discount_slab_id") String discount_slab_id,
                                                                   @Query("discount_amount") String discount_amount,
                                                                   @Query("coupon_code") String coupon_code,
                                                                   @Query("coupon_discount_amount") String coupon_discount_amount,
                                                                   @Query("fix_discount_amount") String fix_discount_amount,
                                                                   @Query("delivery_charges") String delivery_charges,
                                                                   @Query("shipping_address_id") String shipping_address_id,
                                                                   @Query("payment_method") String payment_method,
                                                                   @Body OrderPost orderPost);

    @POST("api/consumer/order/myOrders")
    Call<OrderList> get_order_consumer(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/consumer/order/detail")
    Call<OrderDetailsList> getOrderDetailsbyIdbyconsumer(@Header("Authorization") String token, @Field("order_id") String id);

    @FormUrlEncoded
    @POST("api/consumer/order/update")
    Call<OrderDetailsList> changeorderstatusbyconsumer(@Header("Authorization") String token, @Field("order_id") String order_id, @Field("order_status") String order_status);

}

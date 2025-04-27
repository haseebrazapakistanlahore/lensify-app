package fyp.com.riona.OrderDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("consumer_id")
    @Expose
    private String consumerId;
    @SerializedName("professional_id")
    @Expose
    private String professionalId;
    @SerializedName("net_total")
    @Expose
    private String netTotal;
    @SerializedName("gross_total")
    @Expose
    private String grossTotal;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("coupon_discount_amount")
    @Expose
    private String couponDiscountAmount;
    @SerializedName("discount_id")
    @Expose
    private String discountId;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("fix_discount")
    @Expose
    private String fixDiscount;
    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("tracking_id")
    @Expose
    private String trackingId;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public OrderDetailsModel(String id, String invoiceId, String consumerId, String professionalId, String netTotal, String grossTotal, String couponCode, String couponDiscountAmount, String discountId, String discountAmount, String fixDiscount, String deliveryCharges, String addressId, String trackingId, String orderStatus, String paymentMethod, String paymentStatus, String orderDate, String createdAt, String updatedAt) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.consumerId = consumerId;
        this.professionalId = professionalId;
        this.netTotal = netTotal;
        this.grossTotal = grossTotal;
        this.couponCode = couponCode;
        this.couponDiscountAmount = couponDiscountAmount;
        this.discountId = discountId;
        this.discountAmount = discountAmount;
        this.fixDiscount = fixDiscount;
        this.deliveryCharges = deliveryCharges;
        this.addressId = addressId;
        this.trackingId = trackingId;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.orderDate = orderDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrderDetailsModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(String grossTotal) {
        this.grossTotal = grossTotal;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public void setCouponDiscountAmount(String couponDiscountAmount) {
        this.couponDiscountAmount = couponDiscountAmount;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getFixDiscount() {
        return fixDiscount;
    }

    public void setFixDiscount(String fixDiscount) {
        this.fixDiscount = fixDiscount;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

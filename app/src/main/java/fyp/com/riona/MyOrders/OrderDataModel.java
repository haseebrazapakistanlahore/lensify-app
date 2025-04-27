package fyp.com.riona.MyOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDataModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("gross_total")
    @Expose
    private String grossTotal;
    @SerializedName("net_total")
    @Expose
    private String netTotal;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public OrderDataModel(String id, String invoiceId, String grossTotal, String netTotal, String orderStatus, String createdAt) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.grossTotal = grossTotal;
        this.netTotal = netTotal;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
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

    public String getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(String grossTotal) {
        this.grossTotal = grossTotal;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

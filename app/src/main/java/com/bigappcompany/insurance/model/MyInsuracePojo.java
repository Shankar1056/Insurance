package com.bigappcompany.insurance.model;

/**
 * Created by pushpa on 7/8/17.
 */

public class MyInsuracePojo {
    public String getIDV() {
        return IDV;
    }

    public void setIDV(String IDV) {
        this.IDV = IDV;
    }

    public String getNCB() {
        return NCB;
    }

    public void setNCB(String NCB) {
        this.NCB = NCB;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String IDV;
    private String NCB;
    private String headerName;
    private String amount;



}

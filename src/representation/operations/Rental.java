package representation.operations;

import representation.utils.Date;

public class Rental {
    private String RID;
    private Date rentDate;
    private int durationHours;
    private double paymentAmount;
    private boolean HasInsurance;
    private boolean Returned;
    private String VID;
    private String DID;
    private String CID;

    private void verifyConstructorArguments(String RID, int day, int month, int year, int durationHours, double paymentAmount, String VID, String DID, String CID) {
        assert (!RID.isBlank() && (day > 0 && day <= 31) && (month > 0 && month <= 12) && year > 0 && durationHours > 0 && paymentAmount > 0 && !VID.isBlank() && !DID.isBlank() && !CID.isBlank());
    }

    public Rental(String RID, int day, int month, int year, int durationHours, boolean hasInsurance, boolean returned, double paymentAmount, String VID, String DID, String CID) {
        verifyConstructorArguments(RID, day, month, year, durationHours, paymentAmount, VID, DID, CID);
        this.RID = RID;
        this.rentDate = new Date(String.valueOf(day), String.valueOf(month), String.valueOf(year));
        this.durationHours = durationHours;
        this.paymentAmount = paymentAmount;
        this.HasInsurance = hasInsurance;
        this.Returned = returned;
        this.VID = VID;
        this.DID = DID;
        this.CID = CID;
    }

    public Rental(String RID, int day, int month, int year, int durationHours, double paymentAmount, String VID, String DID, String CID) {
        verifyConstructorArguments(RID, day, month, year, durationHours, paymentAmount, VID, DID, CID);
        this.RID = RID;
        this.rentDate = new Date(String.valueOf(day), String.valueOf(month), String.valueOf(year));
        this.durationHours = durationHours;
        this.paymentAmount = paymentAmount;
        this.VID = VID;
        this.DID = DID;
        this.CID = CID;
    }

    public String getRID() {
        return RID;
    }

    public void setRID(String RID) {
        this.RID = RID;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date date) {
        this.rentDate = date;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public boolean getDurationInsurance() {
        return HasInsurance;
    }

    public void setDurationInsurance(boolean hasInsurance) {
        this.HasInsurance = hasInsurance;
    }

    public boolean getDurationReturned() {
        return Returned;
    }

    public void setDurationReturned(boolean returned) {
        this.Returned = returned;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getVID() {
        return VID;
    }

    public void setVID(String VID) {
        this.VID = VID;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

}
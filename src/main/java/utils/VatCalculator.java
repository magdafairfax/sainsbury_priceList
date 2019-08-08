package utils;

public class VatCalculator {

    private double vatRate = 0.2;
    private double gross;
    private double net;

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public double getVatAmountFromGross(double  grossAmount){
        return grossAmount / (1 + (1 / vatRate));

    }

    public double getVatAmountFromNet(double  netAmount){
        return netAmount * (1 + vatRate);

    }
}

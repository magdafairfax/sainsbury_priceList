package entity;

public class Total {
    private double gross;
    private double vat;

    public Total(double gross, double vat) {
        this.gross = gross;
        this.vat = vat;
    }

    public double getGross() {
        return gross;
    }

    public void setGross(double gross) {
        this.gross = gross;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }
}

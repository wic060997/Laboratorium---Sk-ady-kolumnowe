public class Samochod {
    public String marka;
    public String model;
    public Integer rok;
    public Integer moc;
    public String rej;

    public Samochod() {
    }

    public Samochod(String marka, String model, Integer rok, Integer moc, String rej) {
        this.marka = marka;
        this.model = model;
        this.rok = rok;
        this.moc = moc;
        this.rej = rej;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getRokP() {
        return rok;
    }

    public void setRokP(Integer rok) {
        this.rok = rok;
    }

    public Integer getkM() {
        return moc;
    }

    public void setkM(Integer moc) {
        this.moc = moc;
    }

    public String getRej() {
        return rej;
    }

    public void setRej(String rej) {
        this.rej = rej;
    }

    @Override
    public String toString() {
        return "Samochod{" +
                "marka='" + marka + '\'' +
                ", model='" + model + '\'' +
                ", rok=" + rok +
                ", moc=" + moc +
                ", rej='" + rej + '\'' +
                '}';
    }
}

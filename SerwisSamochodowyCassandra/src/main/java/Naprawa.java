import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.List;

@Entity
public class Naprawa {
    @PartitionKey
    public Integer id;
    public String imie;
    public String nazwisko;
    public String data;
    public Samochod samo;
    public List<String> usterki;

    @Override
    public String toString() {
        return "Wizyta{" +
                "id=" + id +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", data='" + data + '\'' +
                ", samo=" + samo +
                ", usterki=" + usterki +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Samochod getSamo() {
        return samo;
    }

    public void setSamo(Samochod samo) {
        this.samo = samo;
    }

    public List<String> getUsterki() {
        return usterki;
    }

    public void setUsterki(List<String> usterki) {
        this.usterki = usterki;
    }

    public Naprawa(Integer id, String imie, String nazwisko, String data, Samochod samo, List<String> usterki) {
        this.id = id;
        this.imie = imie;
        this.nazwisko= nazwisko;
        this.data = data;
        this.samo = samo;
        this.usterki = usterki;
    }

    public Naprawa() {
    }
}

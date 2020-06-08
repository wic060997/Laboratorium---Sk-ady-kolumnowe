import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import org.omg.CORBA.TIMEOUT;

import java.time.Duration;

public class SerwisTableManager {
    CqlSession session;
    final private static Duration TIMEOUT = Duration.ofSeconds(10);

    public SerwisTableManager(CqlSession session) {
        this.session = session;
    }

    public void createTable() {
        String car = " CREATE TYPE Samochod (marka text, model text, rok int, moc int, rej text);";
        String table = " CREATE TABLE Naprawa (id int PRIMARY KEY, imie text, nazwisko text, data text, samo frozen<Samochod>, usterki list<text>);";

        session.execute(new SimpleStatementBuilder(car).build().setTimeout(TIMEOUT));

        session.execute(new SimpleStatementBuilder(table).build().setTimeout(TIMEOUT));


    }

}

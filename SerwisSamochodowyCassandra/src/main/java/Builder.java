import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.Drop;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;

import javax.xml.validation.Schema;
import java.time.Duration;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

public class Builder {

    public Builder() {
    }

    final private static Duration TIMEOUT = Duration.ofSeconds(10);

    public CqlSession init() {
        CqlSession session = CqlSession.builder().build();

        //drop keyspaces
        Drop drop = SchemaBuilder.dropKeyspace("serwis").ifExists();
        SimpleStatement dropString = drop.build();
        session.execute(dropString.setTimeout(TIMEOUT));
        System.out.println("Statement \"" + dropString.getQuery() + "\" executed successfully");

        //select keyspaces
        Select query = selectFrom("system_schema", "keyspaces").column("keyspace_name");
        SimpleStatement statement = query.build();
        ResultSet resultSet = session.execute(statement);
        System.out.println("");
        System.out.print("Keyspaces = ");
        for (Row row : resultSet) {
            System.out.print(row.getString("keyspace_name") + ", ");
        }
        System.out.println();

        //create keyspace
        CreateKeyspace create = SchemaBuilder.createKeyspace("serwis").withSimpleStrategy(1);
        SimpleStatement createStatement = create.build();
        session.execute(createStatement.setTimeout(TIMEOUT));
        System.out.println("Statement \"" + createStatement.getQuery() + "\" executed successfully");

        //use keyspace
        String use = " USE serwis;";
        session.execute(new SimpleStatementBuilder(use).build().setTimeout(TIMEOUT));

        return session;
    }
}

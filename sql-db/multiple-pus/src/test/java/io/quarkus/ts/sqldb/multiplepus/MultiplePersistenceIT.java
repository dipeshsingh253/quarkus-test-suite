package io.quarkus.ts.sqldb.multiplepus;

import io.quarkus.test.bootstrap.MariaDbService;
import io.quarkus.test.bootstrap.PostgresqlService;
import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.Container;
import io.quarkus.test.services.QuarkusApplication;

@QuarkusScenario
public class MultiplePersistenceIT extends AbstractMultiplePersistenceIT {

    static final int MARIADB_PORT = 3306;
    static final int POSTGRESQL_PORT = 5432;

    @Container(image = "${mariadb.10.image}", port = MARIADB_PORT, expectedLog = "ready for connections")
    static MariaDbService mariadb = new MariaDbService();

    @Container(image = "${postgresql.13.image}", port = POSTGRESQL_PORT, expectedLog = "listening on IPv4 address")
    static PostgresqlService postgresql = new PostgresqlService();

    @QuarkusApplication
    static RestService app = new RestService()
            .withProperty("MARIA_DB_USERNAME", mariadb.getUser())
            .withProperty("MARIA_DB_PASSWORD", mariadb.getPassword())
            .withProperty("MARIA_DB_JDBC_URL", mariadb::getJdbcUrl)
            .withProperty("POSTGRESQL_USERNAME", postgresql.getUser())
            .withProperty("POSTGRESQL_PASSWORD", postgresql.getPassword())
            .withProperty("POSTGRESQL_JDBC_URL", postgresql::getJdbcUrl);

    @Override
    RestService getApp() {
        return app;
    }
}

package com.hilton.utils;

import com.hilton.GeoConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import javax.ws.rs.client.Client;
import java.sql.SQLException;

@Slf4j
public class AppRunnerUtils {

    public void applyDatabaseMigration(GeoConfiguration configuration) {
        try (var c = configuration.getDataSourceFactory().build(null, null).getConnection()) {
            final var migrator = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(c));
            migrator.update("");
            log.debug("Database migration finished");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startH2Database() {
        try {
            var h2Server = Server.createWebServer().start();
            log.info("H2 web console started at url: {}", h2Server.getURL());
            h2Server = Server.createTcpServer("-tcpPort", "9092", "-ifNotExists", "-tcpAllowOthers").start();
            log.info("H2 database server started at url: {}", h2Server.getURL());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Client buildJerseyClient(GeoConfiguration config, Environment environment) {
        return new JerseyClientBuilder(environment).using(config.getJerseyClientConfiguration())
                .build(environment.getName());
    }
}
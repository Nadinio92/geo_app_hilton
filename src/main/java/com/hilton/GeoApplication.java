package com.hilton;

import com.hilton.config.GeoAppModule;
import com.hilton.utils.AppRunnerUtils;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.guicey.jdbi3.JdbiBundle;

@Slf4j
public class GeoApplication extends Application<GeoConfiguration> {

    private final AppRunnerUtils util = new AppRunnerUtils();

    public static void main(final String[] args) throws Exception {
        new GeoApplication().run(args);
    }

    @Override
    public String getName() {
        return "GeoApp";
    }

    @Override
    public void initialize(final Bootstrap<GeoConfiguration> bootstrap) {
        // could be used if we want to run migration separately
        // e.g.: java -jar target/geo_app-1.0-SNAPSHOT.jar db migrate config.yml
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public DataSourceFactory getDataSourceFactory(GeoConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        // adds Guice dependency injection
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                        .modules(new GeoAppModule())
                        .bundles(JdbiBundle.<GeoConfiguration>forDatabase((conf, env) -> conf.getDataSourceFactory()))
                .build());
    }

    @Override
    public void run(final GeoConfiguration configuration,
                    final Environment environment) {
        util.startH2Database();
        util.applyDatabaseMigration(configuration);
    }

}

package com.hilton.db;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import java.util.List;
import java.util.Optional;

@JdbiRepository
@InTransaction
public interface GeolocationDao {

    @SqlUpdate("MERGE INTO GEOLOCATION KEY (IP) VALUES (:ip, :data FORMAT JSON, NOW());")
    void saveOrUpdate(@Bind("ip") String ip,@Bind("data") String data);

    @SqlQuery("SELECT IP, PAYLOAD, TIMESTAMP FROM GEOLOCATION WHERE IP=:ip")
    @RegisterBeanMapper(GeolocationEntityMapper.class)
    Optional<GeolocationEntity> findByIp(@Bind("ip") String ip);

    @SqlQuery("SELECT IP FROM GEOLOCATION WHERE TIMESTAMP <= DATEADD('SECOND',-:age, NOW()) ")
    List<String> findDeprecated(@Bind("age")int deprecationInSeconds);
}

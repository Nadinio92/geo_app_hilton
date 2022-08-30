package com.hilton.db;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GeolocationEntityMapper implements RowMapper<GeolocationEntity> {
    @Override
    public GeolocationEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new GeolocationEntity(
                rs.getString("ip"),
                rs.getString("payload"),
                rs.getTimestamp("timestamp").toInstant()
        );
    }
}

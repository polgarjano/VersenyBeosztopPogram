package hu.unideb.inf.szakdolgozat.model.dao;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

public class DurationColumnMapper implements ColumnMapper<Duration> {
    @Override
    public Duration map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
        return Duration.ofMinutes(r.getLong(columnNumber));
    }


}

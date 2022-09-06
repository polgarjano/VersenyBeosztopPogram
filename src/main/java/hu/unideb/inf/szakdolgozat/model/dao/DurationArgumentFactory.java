package hu.unideb.inf.szakdolgozat.model.dao;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;
import java.time.Duration;

public class DurationArgumentFactory extends AbstractArgumentFactory<Duration> {

    public  DurationArgumentFactory(){super(Types.INTEGER);}

    @Override
    protected Argument build(Duration value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setLong(position, value.toMinutes());
    }
}

package hu.unideb.inf.szakdolgozat.model.dao;

import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;

public class EventTypeArgumentFactory  extends AbstractArgumentFactory<EventType> {
    public EventTypeArgumentFactory() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(EventType value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.getName());
    }
}

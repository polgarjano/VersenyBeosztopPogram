package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

import java.util.Collection;

public class IsUniqueValidator<E> extends AbstractValidator<E>{

    private Collection<E> collection;

    public IsUniqueValidator(Collection<E> collection) {
        this.collection = collection;
    }

    @Override
    boolean validate(E T) {
        return collection.contains(T);
    }

    @Override
    void writeMessage(Label OutputLabel) {
        OutputLabel.setText("Unique value required");
    }
}

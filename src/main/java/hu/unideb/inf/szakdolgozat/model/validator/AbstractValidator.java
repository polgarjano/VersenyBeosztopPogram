package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

public abstract class AbstractValidator<E> {

    private AbstractValidator nextValidator;

    public boolean execute(E T , Label OutputLabel) {
        if (validate(T)) {
            if (nextValidator != null) {
                return nextValidator.execute(T, OutputLabel);
            } else {
                return true;
            }
        } else {
            writeMessage(OutputLabel);
            return false;
        }

    }

    public void add(AbstractValidator validator){
        if (nextValidator != null) {
            nextValidator.add(validator);
        }else {
            nextValidator = validator;
        }
    }

    abstract boolean validate(E T);

    abstract  void  writeMessage(Label OutputLabel);

}

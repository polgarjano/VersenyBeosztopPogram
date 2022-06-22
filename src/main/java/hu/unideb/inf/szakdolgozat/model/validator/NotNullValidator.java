package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

public class NotNullValidator extends AbstractValidator<Object> {

    @Override
    boolean validate(Object T) {
        if(T != null) {
            return true;
        }
        return false;
    }

    @Override
    void writeMessage(Label OutputLabel) {
            OutputLabel.setText("required field");
    }
}

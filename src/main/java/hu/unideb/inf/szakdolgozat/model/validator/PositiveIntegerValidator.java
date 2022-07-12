package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

public class PositiveIntegerValidator extends AbstractValidator<String>{
    @Override
    boolean validate(String T) {
        int value = Integer.parseInt(T);
        return  value >0 ;
    }

    @Override
    void writeMessage(Label OutputLabel) {
        OutputLabel.setText("The value need to be positive");
    }
}

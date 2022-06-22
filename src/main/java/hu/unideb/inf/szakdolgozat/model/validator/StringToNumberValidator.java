package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

public class StringToNumberValidator extends AbstractValidator<String> {

    @Override
    boolean validate(String T) {
        try {
            Integer.parseInt(T);
        }catch (NumberFormatException e){
            return false;
        }
        return false;
    }

    @Override
    void writeMessage(Label OutputLabel) {
        OutputLabel.setText("pleas enter numeric value");
    }
}

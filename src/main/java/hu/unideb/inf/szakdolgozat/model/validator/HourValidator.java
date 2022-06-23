package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

public class HourValidator extends AbstractValidator<String> {
    @Override
    boolean validate(String T) {
        int hour = Integer.parseInt(T);
        return ((hour >= 0) && (hour < 24));

    }

    @Override
    void writeMessage(Label OutputLabel) {
        OutputLabel.setText("Hour field value need to be between 0 and 23");
    }
}

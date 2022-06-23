package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

public class MinuteValidator extends AbstractValidator<String>{

    @Override
    boolean validate(String T) {
        Integer minute = Integer.parseInt(T);
        return ((minute >= 0) && (minute < 60));
    }

    @Override
    void writeMessage(Label OutputLabel) {
        OutputLabel.setText("Minute field value need to be between 0 and 59");
    }
}

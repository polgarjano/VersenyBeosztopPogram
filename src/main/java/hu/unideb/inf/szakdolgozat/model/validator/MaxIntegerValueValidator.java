package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

public class MaxIntegerValueValidator extends AbstractValidator<String> {

    public MaxIntegerValueValidator(Integer maxValue) {
        this.maxValue = maxValue;
    }

    private Integer maxValue;

    @Override
    boolean validate(String T) {
        int value = Integer.parseInt(T);
        return value <= maxValue;
    }

    @Override
    void writeMessage(Label OutputLabel) {
        OutputLabel.setText("The value need to be less or equal to " + maxValue);
    }
}

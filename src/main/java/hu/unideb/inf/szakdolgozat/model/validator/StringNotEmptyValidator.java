package hu.unideb.inf.szakdolgozat.model.validator;

import javafx.scene.control.Label;

public class StringNotEmptyValidator extends AbstractValidator<String>{
    @Override
    boolean validate(String T) {
       if(T.isEmpty()){
           return false;
       }else {
           return true;
       }
    }

    @Override
    void writeMessage(Label OutputLabel) {
        OutputLabel.setText("required field");
    }
}

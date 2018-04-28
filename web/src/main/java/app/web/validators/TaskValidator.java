package app.web.validators;

import app.core.models.Task;
import app.web.DTO.TaskDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TaskValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        TaskDTO task = (TaskDTO) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "Type.not.selected");
//        if ("".equals(task.getType())) {
//            errors.rejectValue("type", "Type.not.selected");
//            return;
//        }
    }
}

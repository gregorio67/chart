import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.DefaultBeanValidator;
import org.springmodules.validation.commons.ValidatorFactory;

public class BeanValidator extends DefaultBeanValidator {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultBeanValidator.class);

	private ValidatorFactory localValidatorFactory;

	public BeanValidator() {
	}

	public void validate(String formName, Object target, Errors errors) {
		Validator commonsValidator = getValidator(formName, target, errors);
		try {
			commonsValidator.validate();
		} catch (ValidatorException e) {
			LOGGER.error("Exception while validating object " + target, e);
		}
	}

	private Validator getValidator(String formName, Object obj, Errors errors) {
		return this.localValidatorFactory.getValidator(formName, obj, errors);
	}

	public void setValidatorFactory(ValidatorFactory validatorFactory) {
		this.localValidatorFactory = validatorFactory;
		super.setValidatorFactory(validatorFactory);
	}
}


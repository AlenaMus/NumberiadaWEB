package GameEngine.validation;


public class XmlNotValidException extends Exception {

    private final ValidationResult r_ValidationResult;

    public XmlNotValidException(ValidationResult i_ValidationResult)
    {
        super();
        r_ValidationResult = i_ValidationResult;
    }

    public ValidationResult getValidationResult()
    {
        return r_ValidationResult;
    }

    @Override
    public String getMessage() {
       return r_ValidationResult.toString();
    }
}

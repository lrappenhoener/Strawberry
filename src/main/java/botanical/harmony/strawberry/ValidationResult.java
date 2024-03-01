package botanical.harmony.strawberry;

public class ValidationResult {
  private final boolean isValid;
  private final String[] messages;

  private ValidationResult(boolean isValid, String[] messages) {
    this.isValid = isValid;
    this.messages = messages;
  }

  public static ValidationResult Success() {
    return new ValidationResult(true, new String[0]);
  }

  public static ValidationResult Failed(String... messages) {
    return new ValidationResult(false, messages);
  }

  public boolean isValid() {
    return isValid;
  }

  public boolean isInvalid() {
    return !isValid;
  }
}

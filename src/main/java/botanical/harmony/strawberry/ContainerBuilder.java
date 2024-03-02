package botanical.harmony.strawberry;

public class ContainerBuilder {
  private final Registrations registrations = new Registrations();

  public static ContainerBuilder create() {
    return new ContainerBuilder();
  }

  public <T> RegistrationBuilder<T> register(Class<T> clazz) {
    Registration<T> registration = new Registration<T>(clazz);
    registrations.add(registration);
    return registration;
  }

  public Container build() {
    ValidationResult validationResult = registrations.validate();
    if (validationResult.isInvalid()) throw new BadRegistrationException(validationResult);
    Resolvers resolvers = registrations.build();
    return new Container(resolvers);
  }
}

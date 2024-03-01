package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Registrations {
  private final HashMap<Class<?>, Registration<?>> registrations = new HashMap<>();

  public void add(Registration<?> registration) {
    registrations.put(registration.getClazz(), registration);
  }

  public ValidationResult validate() {
    for (Registration<?> registration : registrations.values()) {
      ValidationResult validationResult = validateRegistration(registration);
      if (validationResult.isInvalid())
        throw new BadRegistrationException(validationResult);
    }
    return ValidationResult.Success();
  }

  private ValidationResult validateRegistration(Registration<?> registration) {
    if (hasCyclicDependency(registration)) throw new BadRegistrationException(ValidationResult.Failed("Cyclic Dependency"));

    if (registration.getOptionalFactory().isPresent()) {
      Function<Container, ?> factory = registration.getOptionalFactory().get();
      return validateFactory(factory);
    }

    if (registration.getOptionalConstructor().isPresent()) {
      Constructor<?> constructor = registration.getOptionalConstructor().get();
      return validateConstructor(constructor);
    }

    Optional<Constructor<?>> validConstructor = searchValidConstructor(registration);
    if (validConstructor.isEmpty()) return ValidationResult.Failed("No resolvable constructor found");
    registration.setConstructor(validConstructor.get());
    return ValidationResult.Success();
  }

  private boolean hasCyclicDependency(Registration<?> registration) {
    Class<?> clazz = registration.getClazz();
    return hasCyclicDependency(clazz, clazz);
  }

  private boolean hasCyclicDependency(Class<?> current, Class<?> original) {
    for (Constructor<?> constructor : current.getDeclaredConstructors()) {
      for (Parameter parameter : constructor.getParameters()) {
        if (parameter.getType() == original ||
            hasCyclicDependency(parameter.getType(), original)) return true;
      }
    }
    return false;
  }

  private ValidationResult validateFactory(Function<Container, ?> factory) {
    return ValidationResult.Success();
  }

  private Optional<Constructor<?>> searchValidConstructor(Registration<?> registration) {
    Constructor<?>[] constructors = registration.getClazz().getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      Parameter[] parameters = constructor.getParameters();
      ValidationResult validationResult = validateParameters(parameters);
      if (validationResult.isValid()) return Optional.of(constructor);
    }
    return Optional.empty();
  }

  private ValidationResult validateConstructor(Constructor<?> constructor) {
    return validateParameters(constructor.getParameters());
  }

  private ValidationResult validateParameters(Parameter[] parameters) {
    for (Parameter parameter : parameters) {
      Class<?> clazz = parameter.getType();
      if (!registrations.containsKey(clazz)) return ValidationResult.Failed("Parameter type not registered");
      Registration<?> parameterRegistration = registrations.get(clazz);
      ValidationResult validationResult = validateRegistration(parameterRegistration);
      if (validationResult.isInvalid()) return ValidationResult.Failed("Registered parameter type not valid");
    }
    return ValidationResult.Success();
  }

  public Resolvers build() {
    Map<Class<?>, Resolver> resolvers = registrations
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    entry -> entry.getKey(),
                    entry -> ResolverBuilder.createFromRegistration(entry.getValue())));
    return new Resolvers(resolvers);
  }
}

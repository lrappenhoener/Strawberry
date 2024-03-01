package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ContainerBuilder {
  private final Registrations registrations = new Registrations();

  public static ContainerBuilder create() {
    return new ContainerBuilder();
  }

  public <T> ContainerBuilder register(Class<T> clazz) {
    register(clazz, Optional.empty(), Optional.empty(), LifeTime.Fresh);
    return this;
  }

  public <T> ContainerBuilder register(Class<T> clazz, Function<Container, T> factory) {
    register(clazz, Optional.of(factory), Optional.empty(), LifeTime.Fresh);
    return this;
  }

  public <T> ContainerBuilder registerSingleton(Class<T> clazz) {
    register(clazz, Optional.empty(), Optional.empty(), LifeTime.Singleton);
    return this;
  }

  public <T> ContainerBuilder registerSingleton(Class<T> clazz, Function<Container, T> factory) {
    register(clazz, Optional.of(factory), Optional.empty(), LifeTime.Singleton);
    return this;
  }

  private <T> void register(Class<T> clazz, Optional<Function<Container, T>> factory, Optional<Constructor<T>> constructor, LifeTime lifeTime) {
    Registration<T> registration = new Registration<T>(clazz, factory, constructor, lifeTime);
    registrations.add(registration);
  }

  public Container build() {
    ValidationResult validationResult = registrations.validate();
    if (validationResult.isInvalid()) throw new BadRegistrationException(validationResult);
    Resolvers resolvers = registrations.build();
    return new Container(resolvers);
  }
}

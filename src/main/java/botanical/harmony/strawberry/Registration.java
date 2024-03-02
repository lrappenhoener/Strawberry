package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.function.Function;

public class Registration<T> implements RegistrationBuilder<T> {
  private final Class<T> clazz;
  private Optional<Function<Container, T>> factory = Optional.empty();
  private Optional<Constructor<T>> constructor = Optional.empty();
  private LifeTime lifeTime;

  public Registration(Class<T> clazz) {
    this.clazz = clazz;
    this.lifeTime = LifeTime.Fresh;
  }

  @Override
  public RegistrationBuilder<T> withFactory(Function<Container, T> factory) {
    setFactory(factory);
    return this;
  }

  @Override
  public RegistrationBuilder<T> withConstructor(Constructor<T> constructor) {
    setConstructor(constructor);
    return this;
  }

  @Override
  public RegistrationBuilder<T> withLifeTime(LifeTime lifeTime) {
    setLifeTime(lifeTime);
    return this;
  }

  Class<T> getClazz() {
    return clazz;
  }

  Optional<Function<Container, T>> getOptionalFactory() {
    return factory;
  }

  Optional<Constructor<T>> getOptionalConstructor() {
    return constructor;
  }

  LifeTime getLifeTime() {
    return lifeTime;
  }

  private void setFactory(Function<Container, T> factory) {
    this.factory = Optional.of(factory);
  }

  void setConstructor(Constructor<?> constructor) {
    this.constructor = Optional.of((Constructor<T>) constructor);
  }

  void setLifeTime(LifeTime lifeTime) {
    this.lifeTime = lifeTime;
  }
}

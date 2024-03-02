package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Registration<T> implements RegistrationBuilder<T> {
  private final Class<T> clazz;
  private Optional<Function<Container, T>> factory = Optional.empty();
  private Optional<Constructor<T>> constructor = Optional.empty();
  private LifeTime lifeTime;
  private List<Class<?>> abstractions = new ArrayList<>();

  public Registration(Class<T> clazz) {
    this.clazz = clazz;
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

  @Override
  public RegistrationBuilder<T> as(Class<?> abstraction) {
    addAbstraction(abstraction);
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

  List<Class<?>> getAbstractions() {
    return abstractions;
  }

  private void setFactory(Function<Container, T> factory) {
    this.factory = Optional.of(factory);
  }

  void setConstructor(Constructor<?> constructor) {
    this.constructor = Optional.of((Constructor<T>) constructor);
  }

  private void setLifeTime(LifeTime lifeTime) {
    this.lifeTime = lifeTime;
  }

  private void addAbstraction(Class<?> abstraction) {
    abstractions.add(abstraction);
  }

}

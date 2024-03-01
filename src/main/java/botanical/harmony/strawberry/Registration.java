package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.function.Function;

public class Registration<T> {
  private final Class<T> clazz;
  private final Optional<Function<Container, T>> factory;
  private Optional<Constructor<T>> constructor;
  private final LifeTime lifeTime;

  public Registration(Class<T> clazz, Optional<Function<Container, T>> factory, Optional<Constructor<T>> constructor, LifeTime lifeTime) {
    this.clazz = clazz;
    this.factory = factory;
    this.constructor = constructor;
    this.lifeTime = lifeTime;
  }

  public Class<T> getClazz() {
    return clazz;
  }

  public Optional<Function<Container, T>> getOptionalFactory() {
    return factory;
  }

  public Optional<Constructor<T>> getOptionalConstructor() {
    return constructor;
  }

  public void setConstructor(Constructor<?> constructor) {
    this.constructor = Optional.of((Constructor<T>)constructor);
  }

  public LifeTime getLifeTime() {
    return lifeTime;
  }
}

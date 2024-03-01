package botanical.harmony.strawberry;

import java.util.Optional;
import java.util.function.Function;

public class FactoryResolver implements Resolver {
  private final Class<?> clazz;
  private final Function<Container, ?> containerFunction;
  private final LifeTime lifeTime;
  private Optional<Object> singleton = Optional.empty();

  public FactoryResolver(Class<?> clazz, Function<Container, ?> containerFunction, LifeTime lifeTime) {
    this.clazz = clazz;
    this.containerFunction = containerFunction;
    this.lifeTime = lifeTime;
  }


  @Override
  public Object resolve(Container container) {
    if (lifeTime == LifeTime.Singleton) {
      if (singleton.isEmpty()) {
        Object instance = containerFunction.apply(container);
        singleton = Optional.of(instance);
      }
      return singleton.get();
    }
    return containerFunction.apply(container);
  }
}

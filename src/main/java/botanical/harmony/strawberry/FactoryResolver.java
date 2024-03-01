package botanical.harmony.strawberry;

import java.util.function.Function;

public class FactoryResolver implements Resolver {
  private final Class<?> clazz;
  private final Function<Container, ?> containerFunction;
  private final LifeTime lifeTime;

  public FactoryResolver(Class<?> clazz, Function<Container, ?> containerFunction, LifeTime lifeTime) {

    this.clazz = clazz;
    this.containerFunction = containerFunction;
    this.lifeTime = lifeTime;
  }


  @Override
  public Object resolve(Container container) {
    return containerFunction.apply(container);
  }
}

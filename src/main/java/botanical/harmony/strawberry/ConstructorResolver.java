package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ConstructorResolver implements Resolver {
  private final Class<?> clazz;
  private final Constructor<?> constructor;
  private final LifeTime lifeTime;
  private final List<Class<?>> abstractions;
  private Optional<Object> singleton = Optional.empty();

  public ConstructorResolver(Class<?> clazz, Constructor<?> constructor, LifeTime lifeTime, List<Class<?>> abstractions) {
    this.clazz = clazz;
    this.constructor = constructor;
    this.lifeTime = lifeTime;
    this.abstractions = abstractions;
  }

  @Override
  public Object resolve(Container container) {
    try {
      if (lifeTime == LifeTime.Singleton) {
        if (singleton.isPresent()) return singleton.get();
        Object instance = createInstance(container);
        singleton = Optional.of(instance);
        return singleton.get();
      } else
        return createInstance(container);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new FailedResolveException(e);
    }
  }

  private Object createInstance(Container container) throws InstantiationException, IllegalAccessException, InvocationTargetException {
    Object[] arguments = createArguments(constructor.getParameters(), container);
    return constructor.newInstance(arguments);
  }

  private Object[] createArguments(Parameter[] parameters, Container container) {
    return Arrays.stream(parameters).map(p -> container.resolve(p.getType())).toArray();
  }
}

package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ConstructorResolver implements Resolver {
  private final Class<?> clazz;
  private final Constructor<?> constructor;
  private final LifeTime lifeTime;

  public ConstructorResolver(Class<?> clazz, Constructor<?> constructor, LifeTime lifeTime) {
    this.clazz = clazz;
    this.constructor = constructor;
    this.lifeTime = lifeTime;
  }

  @Override
  public Object resolve(Container container) {
    try {
      Object[] arguments = createArguments(constructor.getParameters(), container);
      return constructor.newInstance(arguments);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new FailedResolveException(e);
    }
  }

  private Object[] createArguments(Parameter[] parameters, Container container) {
    return Arrays.stream(parameters).map(p -> container.resolve(p.getType())).toArray();
  }
}

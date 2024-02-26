package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Container {

  private final List<Class<?>> registeredTypes;

  public Container(List<Class<?>> registeredTypes) {
    this.registeredTypes = registeredTypes;
  }

  public <T> Optional<T> resolve(Class<T> clazz) {
    if (!registeredTypes.contains(clazz))
      return Optional.empty();

    Optional<Constructor<?>> constructor = getConstructor(clazz);
    return constructor.isPresent() ?
            createInstance(constructor.get()) :
            Optional.empty();
  }

  private <T> Optional<Constructor<?>> getConstructor(Class<T> clazz) {
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();

    return Arrays.stream(constructors)
            .filter(constructor -> parametersResolvable(constructor.getParameters()))
            .findFirst();
  }

  private boolean parametersResolvable(Parameter[] parameters) {
    return parameters.length == 0 ||
            Arrays.stream(parameters)
                    .allMatch(p -> registeredTypes.contains(p.getType()));
  }

  @SuppressWarnings("unchecked")
  private <T> Optional<T> createInstance(Constructor<?> constructor) {
    try {
      Object[] parameterInstances = createParameterInstances(constructor.getParameters());
      return Optional.of((T)constructor.newInstance(parameterInstances));
    } catch (ClassCastException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      return Optional.empty();
    }
  }

  private Object[] createParameterInstances(Parameter[] parameters) throws InstantiationException {
    List<Object> list = new ArrayList<>();
    for (Parameter p : parameters) {
      Optional<?> o = resolve(p.getType());
      if (o.isEmpty()) throw new InstantiationException();
      list.add(o.get());
    }
    return list.toArray();
  }
}

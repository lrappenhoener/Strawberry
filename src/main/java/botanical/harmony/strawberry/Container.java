package botanical.harmony.strawberry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class Container {

  private List<Class<?>> registeredTypes;

  public Container(List<Class<?>> registeredTypes) {
    this.registeredTypes = registeredTypes;
  }

  public <T> Optional<T> resolve(Class<T> class1) {
    if (!registeredTypes.contains(class1))
      return Optional.empty();

    try {
      Constructor<?>[] constructors = class1.getDeclaredConstructors();

      Optional<Constructor<?>> defaultConstructor = getDefaultConstructor(constructors);
      if (defaultConstructor.isPresent())
        return Optional.of((T) defaultConstructor.get().newInstance());

      Optional<Constructor<?>> resolvableConstructor = getResolvableConstructor(constructors);
      if (resolvableConstructor.isPresent())
        return createInstance(resolvableConstructor.get());

    } catch (Exception ex) {
    }
    return Optional.empty();
  }

  private <T> T createInstance(Constructor<?> resolvableConstructor) throws Exception {
    Parameter[] parameters = resolvableConstructor.getParameters();
    List<Object> parameterInstances = new ArrayList<>();

    for (Parameter parameter : parameters) {
      Object instance = resolve(parameter.getClass());
      parameterInstances.add(instance);
    }

    return (T) resolvableConstructor.newInstance(parameterInstances.toArray());
  }

  private Optional<Constructor<?>> getResolvableConstructor(Constructor<?>[] constructors) {
    for (Constructor<?> constructor : constructors) {
      for (Parameter parameter : constructor.getParameters()) {
        if (!registeredTypes.contains(parameter.getClass()))
          continue;
      }
      return Optional.of(constructor);
    }
    return Optional.empty();
  }

  private <T> Optional<Constructor<?>> getDefaultConstructor(Constructor<T>[] constructors) {
    for (Constructor<?> constructor : constructors) {
      if (constructor.getParameters().length == 0)
        return Optional.of(constructor);
    }
    return Optional.empty();
  }

}

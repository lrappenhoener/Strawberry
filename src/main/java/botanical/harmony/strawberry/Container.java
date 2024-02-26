package botanical.harmony.strawberry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class Container {

  private final List<Class<?>> registeredTypes;

  public Container(List<Class<?>> registeredTypes) {
    this.registeredTypes = registeredTypes;
  }

  public <T> Optional<T> resolve(Class<T> clazz) {
    if (!registeredTypes.contains(clazz))
      return Optional.empty();

    try {
      Constructor<?>[] constructors = clazz.getDeclaredConstructors();

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

  private <T> Optional<T> createInstance(Constructor<?> resolvableConstructor) {
    Parameter[] parameters = resolvableConstructor.getParameters();
    List<Object> parameterInstances = new ArrayList<>();

    for (Parameter parameter : parameters) {
      Optional<?> optionalInstance = resolve(parameter.getType());
      if (optionalInstance.isPresent()) {
        Object instance = optionalInstance.get();
        parameterInstances.add(instance);
      }
    }
    try {
      return Optional.of(((T)resolvableConstructor.newInstance(parameterInstances.toArray())));
    }
    catch(InstantiationException | IllegalAccessException | InvocationTargetException ex){
      System.err.println(ex.toString());
    }

      return Optional.empty();
  }

  private Optional<Constructor<?>> getResolvableConstructor(Constructor<?>[] constructors) {
    for (Constructor<?> constructor : constructors) {
      boolean valid = true;
      for (Parameter parameter : constructor.getParameters()) {
        if (!registeredTypes.contains(parameter.getType())) {
          valid = false;
          break;
        }
        ;
      }
      if (valid)
        return Optional.of(constructor);
    }
    return Optional.empty();
  }

  private <T> Optional<Constructor<?>> getDefaultConstructor(Constructor<?>[] constructors) {
    for (Constructor<?> constructor : constructors) {
      if (constructor.getParameters().length == 0)
        return Optional.of(constructor);
    }
    return Optional.empty();
  }

}

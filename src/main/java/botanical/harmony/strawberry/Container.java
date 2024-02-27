package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public class Container {

  private final List<Class<?>> registeredTypes;
  private final HashMap<Class<?>, List<Function<Container, ?>>> registeredTypeFactories;

  public Container(List<Class<?>> registeredTypes, HashMap<Class<?>, List<Function<Container, ?>>> registeredTypeFactories) {
    this.registeredTypes = registeredTypes;
    this.registeredTypeFactories = registeredTypeFactories;
  }

  public <T> Optional<T> resolve(Class<T> clazz) {
    Optional<T> instance = resolveByFactory(clazz);
    if (instance.isPresent()) return instance;
    return resolveByReflection(clazz);
  }

  private <T> Optional<T> resolveByFactory(Class<T> clazz) {
    if (!registeredTypeFactories.containsKey(clazz))
      return Optional.empty();
    Optional<Function<Container, ?>> optionalFactory = registeredTypeFactories.get(clazz).stream().findFirst();
    if (optionalFactory.isEmpty())
      return Optional.empty();
    Function<Container, ?> factory = optionalFactory.get();
    Object instance = factory.apply(this);
    return Optional.of((T)instance);
  }

  private <T> Optional<T> resolveByReflection(Class<T> clazz) {
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
            .filter(constructor -> resolvableDependencies(constructor.getParameters()))
            .findFirst();
  }

  private boolean resolvableDependencies(Parameter[] parameters) {
    return parameters.length == 0 ||
            Arrays.stream(parameters)
                    .allMatch(p -> registeredTypes.contains(p.getType()));
  }

  @SuppressWarnings("unchecked")
  private <T> Optional<T> createInstance(Constructor<?> constructor) {
    try {
      Object[] dependencies = createDependencies(constructor.getParameters());
      return Optional.of((T)constructor.newInstance(dependencies));
    } catch (ClassCastException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      return Optional.empty();
    }
  }

  private Object[] createDependencies(Parameter[] parameters) throws InstantiationException {
    List<Object> list = new ArrayList<>();
    for (Parameter p : parameters) {
      Optional<?> o = resolve(p.getType());
      if (o.isEmpty()) throw new InstantiationException();
      list.add(o.get());
    }
    return list.toArray();
  }
}

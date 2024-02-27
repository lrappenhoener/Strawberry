package botanical.harmony.strawberry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ContainerBuilder {
  private final List<Class<?>> registeredTypes = new ArrayList<>();
  private final HashMap<Class<?>, List<Function<Container, ?>>> registeredFactories = new HashMap<>();

  public static ContainerBuilder create() {
    return new ContainerBuilder();
  }

  public <T> void register(Class<T> class1) {
    registeredTypes.add(class1);
  }
  public <T> void register(Class<T> clazz, Function<Container, T> factory) {
    List<Function<Container, ?>> factories;
    if (registeredFactories.containsKey(clazz)) {
      factories = registeredFactories.get(clazz);
      factories.add(factory);
    }
    else {
      factories = new ArrayList<>();
      factories.add(factory);
      registeredFactories.put(clazz, factories);
    }
  }

  public Container build() {
    return new Container(registeredTypes, registeredFactories);
  }

}

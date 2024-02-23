package botanical.harmony.strawberry;

import java.util.List;
import java.util.Optional;
import java.lang.reflect.Constructor;

public class Container {

  private List<Class<?>> registeredTypes;

  public Container(List<Class<?>> registeredTypes) {
    this.registeredTypes = registeredTypes;
  }

  public <T> Optional<T> resolve(Class<T> class1) {
    if (!registeredTypes.contains(class1))
      return Optional.empty();
    try {
      Constructor<T> ctor = class1.getDeclaredConstructor();
      return Optional.of((T) ctor.newInstance());
    } catch (Exception ex) {
    }
    return Optional.empty();
  }

}

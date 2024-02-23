package botanical.harmony.strawberry;

import java.util.Optional;
import java.lang.reflect.Constructor;

public class Container {

  public <T> Optional<T> resolve(Class<T> class1) {
    try {
      Constructor<T> ctor = class1.getDeclaredConstructor();
      return Optional.of((T) ctor.newInstance());
    } catch (Exception ex) {
    }
    return Optional.empty();
  }

}

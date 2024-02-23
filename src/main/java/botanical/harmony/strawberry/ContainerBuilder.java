package botanical.harmony.strawberry;

import java.util.ArrayList;
import java.util.List;

public class ContainerBuilder {
  private List<Class<?>> registeredTypes = new ArrayList<>();

  public static ContainerBuilder create() {
    return new ContainerBuilder();
  }

  public <T> void register(Class<T> class1) {
    registeredTypes.add(class1);
  }

  public Container build() {
    return new Container(registeredTypes);
  }

}

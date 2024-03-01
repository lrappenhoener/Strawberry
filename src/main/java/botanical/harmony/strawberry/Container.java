package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public class Container {
  private final Resolvers resolvers;

  public Container(Resolvers resolvers) {
    this.resolvers = resolvers;
  }

  public <T> T resolve(Class<T> clazz) {
    return resolvers.resolve(this, clazz);
  }
}

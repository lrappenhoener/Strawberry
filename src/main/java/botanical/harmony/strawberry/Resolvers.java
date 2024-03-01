package botanical.harmony.strawberry;

import java.util.Map;

public class Resolvers {
  private final Map<Class<?>, Resolver> resolvers;

  public Resolvers(Map<Class<?>, Resolver> resolvers) {
    this.resolvers = resolvers;
  }

  public <T> T resolve(Container container, Class<T> clazz) {
    if (!resolvers.containsKey(clazz)) throw new BadRequestException();
    return (T)resolvers.get(clazz).resolve(container);
  }
}

package botanical.harmony.strawberry;

public class ContainerBuilder {

  public static ContainerBuilder create() {
    return new ContainerBuilder();
  }

  public <T> void register(Class<T> class1) {
  }

  public Container build() {
    return new Container();
  }

}

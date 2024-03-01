package botanical.harmony.strawberry;

public class ResolverBuilder {
  public static Resolver createFromRegistration(Registration<?> registration) {
    if (registration.getOptionalFactory().isPresent())
      return createFactoryResolver(registration);
    return createConstructorResolver(registration);
  }

  private static Resolver createConstructorResolver(Registration<?> registration) {
    return new ConstructorResolver(
            registration.getClazz(),
            registration.getOptionalConstructor().get(),
            registration.getLifeTime());
  }

  private static Resolver createFactoryResolver(Registration<?> registration) {
    return new FactoryResolver(
            registration.getClazz(),
            registration.getOptionalFactory().get(),
            registration.getLifeTime());
  }
}

package botanical.harmony.strawberry;

import botanical.harmony.strawberry.helpers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResolveTests {
  @Test
  void resolve_simple_type() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(SimpleTestType.class);
    Container container = builder.build();

    SimpleTestType result = container.resolve(SimpleTestType.class);

    assertNotNull(result);
    assertSame(result.getClass(), SimpleTestType.class);
  }

  @Test
  void throws_bad_request_exception_when_resolving_unregistered_type() {
    ContainerBuilder builder = ContainerBuilder.create();
    Container container = builder.build();

    Assertions.assertThrows(BadRequestException.class, () -> container.resolve(SimpleTestType.class));
  }

  @Test
  void resolve_type_with_dependencies() {
    ContainerBuilder builder = getContainerBuilder();
    Container container = builder.build();

    TestTypeWithDependencies result = container.resolve(TestTypeWithDependencies.class);

    assertNotNull(result);
    assertSame(result.getClass(), TestTypeWithDependencies.class);
  }

  @Test
  void resolve_type_with_dependencies_by_factory() {
    ContainerBuilder builder = getContainerBuilder();
    builder.register(TestTypeForFactory.class)
        .withFactory((c) -> {
          AnotherTestTypeWithDependencies dependencyA = c.resolve(AnotherTestTypeWithDependencies.class);
          SimpleTestType dependencyB = c.resolve(SimpleTestType.class);
          TestTypeWithDependencies dependencyC = c.resolve(TestTypeWithDependencies.class);
          return new TestTypeForFactory(dependencyA, dependencyB, dependencyC);
        });
    Container container = builder.build();

    TestTypeForFactory result = container.resolve(TestTypeForFactory.class);

    assertNotNull(result);
    assertSame(result.getClass(), TestTypeForFactory.class);
  }

  @Test
  void resolve_singleton() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(SimpleTestType.class)
        .withLifeTime(LifeTime.Singleton);
    Container container = builder.build();

    SimpleTestType a = container.resolve(SimpleTestType.class);
    SimpleTestType b = container.resolve(SimpleTestType.class);

    assertEquals(a, b);
  }

  @Test
  void resolve_singleton_by_factory() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(SimpleTestType.class)
        .withFactory(c -> new SimpleTestType())
        .withLifeTime(LifeTime.Singleton);
    Container container = builder.build();

    SimpleTestType a = container.resolve(SimpleTestType.class);
    SimpleTestType b = container.resolve(SimpleTestType.class);

    assertEquals(a, b);
  }

  @Test
  void resolve_by_interface() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(SimpleTestType.class).as(SomeTestInterface.class);
    Container container = builder.build();

    SomeTestInterface result = container.resolve(SomeTestInterface.class);

    assertNotNull(result);
    assertEquals(result.getClass(), SimpleTestType.class);
  }

  @Test
  void can_be_resolved_returns_false_when_unregistered_type(){
    ContainerBuilder builder = ContainerBuilder.create();
    Container container = builder.build();

    boolean resolvable = container.canResolve(SimpleTestType.class);

    assertFalse(resolvable);
  }

  @Test
  void can_be_resolved_returns_true_when_registered_type() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(SimpleTestType.class);
    Container container = builder.build();

    boolean resolvable = container.canResolve(SimpleTestType.class);

    assertTrue(resolvable);
  }

  private static ContainerBuilder getContainerBuilder() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeWithDependencies.class);
    builder.register(AnotherTestTypeWithDependencies.class);
    builder.register(SimpleTestType.class);
    return builder;
  }
}

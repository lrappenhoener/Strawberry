package botanical.harmony.strawberry;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class ResolveTests {
  @Test
  void resolve_simple_type() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(SimpleTestType.class);
    Container container = builder.build();

    Optional<SimpleTestType> result = container.resolve(SimpleTestType.class);

    assertTrue(result.isPresent());
  }
}

class SimpleTestType {

}

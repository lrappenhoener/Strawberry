package botanical.harmony.strawberry;

import java.lang.reflect.Constructor;
import java.util.function.Function;

public interface RegistrationBuilder<T> {
  RegistrationBuilder<T> withFactory(Function<Container, T> factory);
  RegistrationBuilder<T> withConstructor(Constructor<T> constructor);
  RegistrationBuilder<T> withLifeTime(LifeTime lifeTime);
  RegistrationBuilder<T> as(Class<?> abstraction);
}

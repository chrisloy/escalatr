package net.escalatr

import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, BeforeAndAfter, FlatSpec}
import org.mockito.stubbing.OngoingStubbing
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock

/**
 * Convenience trait to cut down on the number of mixins we're using in all our tests.
 * @since
 */
trait BaseSpec extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfter {

  class ThrowCheckedStub[T](val stub: OngoingStubbing[T]) {
    def thenThrowChecked(e: Throwable) = stub thenAnswer new Answer[T] {
      def answer(invoke: InvocationOnMock) = throw e
    }
  }

  implicit def toBetterStub[T](stub: OngoingStubbing[T]) = new ThrowCheckedStub[T](stub)
}

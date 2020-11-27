import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test

class IterableMonadTests {
    @Test
    fun `all features`() {
        assertIterableEquals(IterableMonad.Wrapper(10, 10, 10, 15, 15, 15, 100), IterableMonad {
            returns(1)
                .then { returns(it * 2, it * 3) }
                .fmap { it * 5 }
                .thenIgnoring { returns(it, it, it) } or returns(100)
        })
        assertIterableEquals(listOf(2, 2, 2, 3, 3, 3), IterableMonad {
            returns(2, 3).thenIgnoring { returns(4, 5, 6) }
        })
        assertIterableEquals(IterableMonad.returns(6, 7, 5), IterableMonad { returns(6, 7) or returns(5) })
        assertIterableEquals(IterableMonad.returns(6, 7), IterableMonad { returns(6, 7) or returns() })
        assertIterableEquals(IterableMonad.returns<Int>(), IterableMonad { fail<Int>("omg") })
        assertIterableEquals(IterableMonad.returns(12, 22, 32, 13, 23, 33),
            IterableMonad { { a: Int, b: Int -> a + b }.lift(returns(2, 3), returns(10, 20, 30)) })
        assertIterableEquals(IterableMonad.returns<Int>(),
            IterableMonad { { a: Int, b: Int -> a + b }.lift(returns(2), returns()) })
        assertIterableEquals(IterableMonad.returns(Unit), IterableMonad())
    }
}
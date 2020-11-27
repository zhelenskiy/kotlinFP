import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MaybeMonadTests {
    @Test
    fun `all features`() {
        assertEquals(Just(6), MaybeMonad {
            returns(1)
                .then { Just(it * 2) }
                .fmap { it * 3 }
                .thenIgnoring { returns(it * 4) } or
                    returns(5)
        })
        assertEquals(Just(6), MaybeMonad { None<Int>() or Just(6) })
        assertEquals(Just(6), MaybeMonad { Just(6) or None() })
        assertEquals(None(), MaybeMonad { fail<Int>("omg")})
        assertEquals(Just(5), MaybeMonad { { a: Int, b: Int -> a + b }.lift(Just(2), Just(3)) })
        assertEquals(None(), MaybeMonad { { a: Int, b: Int -> a + b }.lift(Just(2), None()) })
        assertEquals(Just(Unit), MaybeMonad())
    }
}
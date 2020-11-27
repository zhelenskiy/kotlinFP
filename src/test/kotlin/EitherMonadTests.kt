import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EitherMonadTests {
    @Test
    fun `all features`() {
        assertEquals(Right(6), EitherMonad {
            returns<String, Int>(1)
                .then { Right(it * 2) }
                .fmap { it * 3 }
                .thenIgnoring { returns(it * 4) } or
                    returns(5)
        })
        assertEquals(Right(6), EitherMonad { Left<String, Int>("f") or Right(6) })
        assertEquals(Right(6), EitherMonad { Right<String, Int>(6) or Left("s") })
        assertEquals(Left("omg"), EitherMonad { fail<Int>("omg")})
        assertEquals(Right(5), EitherMonad { { a: Int, b: Int -> a + b }.lift(Right<Unit, Int>(2), Right(3)) })
        assertEquals(Left(Unit), EitherMonad { { a: Int, b: Int -> a + b }.lift(Right<Unit, Int>(2), Left(Unit)) })
        assertEquals(Right(Unit), EitherMonad())
    }
}
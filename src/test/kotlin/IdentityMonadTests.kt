import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IdentityMonadTests {
    @Test
    fun `all features`() {
        assertEquals(6, IdentityMonad {
            returns(1)
                .then { Id(it * 2) }
                .fmap { it * 3 }
                .thenIgnoring { Id(it * 4) } or
                    returns(5)
        }.value)
        assertEquals(Id(Unit), IdentityMonad())
        assertEquals(Id(5), IdentityMonad { { a: Int, b: Int -> a + b }.lift(Id(2), Id(3)) })
    }
}
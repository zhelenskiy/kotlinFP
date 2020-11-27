import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class MonoidTests {
    @Test
    fun sum() {
        with(MonoidImpl(0) { a, b -> a + b }) {
            assertEquals(0, mempty)
            assertEquals(0, listOf<Int>().mconcat())
            assertEquals(10, (1..4).mconcat())
            assertEquals(0, 1.mtimes(0))
        }
    }

    @Test
    fun product() {
        with(MonoidImpl(1) { a, b -> a * b }) {
            assertEquals(1, mempty)
            assertEquals(1, listOf<Int>().mconcat())
            assertEquals(24, (1..4).mconcat())
            assertEquals(1, 1.mtimes(0))
        }
    }

    @Test
    fun concat() {
        with(MonoidImpl(emptyList<Int>()) { a, b -> a + b }) {
            assertEquals(emptyList<Int>(), mempty)
            assertEquals(emptyList<Int>(), emptyList<List<Int>>().mconcat())
            assertEquals((1..4).toList(), (1..4).map(::listOf).mconcat())
            assertEquals(emptyList<Int>(), listOf(5).mtimes(0))
        }
    }
}
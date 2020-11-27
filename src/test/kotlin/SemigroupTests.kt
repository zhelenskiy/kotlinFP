
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.min
import kotlin.math.max

class SemigroupTests {
    @Test
    fun max() {
        with(SemigroupImpl<Int>(::max)) {
            assertEquals(4, 2 assocOp 3 assocOp 4)
            assertEquals(4, 4.stimes(5))
            assertEquals(6, listOf(4, 5, 6).sconcat())
        }
    }

    @Test
    fun min() {
        with(SemigroupImpl<Int>(::min)) {
            assertEquals(2, 2 assocOp 3 assocOp 4)
            assertEquals(4, 4.stimes(5))
            assertEquals(4, listOf(4, 5, 6).sconcat())
        }
    }

    @Test
    fun sum() {
        with(SemigroupImpl<Int> { a, b -> a + b }) {
            assertEquals(9, 2 assocOp 3 assocOp 4)
            assertEquals(20, 4.stimes(5))
            assertEquals(15, listOf(4, 5, 6).sconcat())
        }
    }

    @Test
    fun concat() {
        with(SemigroupImpl<Iterable<Int>> { a, b -> a + b }) {
            val list = listOf(1, 2, 3)
            val list3 = list + list + list
            assertEquals(list3, list assocOp list assocOp list)
            assertEquals(list3 + list + list, list.stimes(5))
            assertEquals(list + listOf(10) + listOf(5), listOf(list, listOf(10), listOf(5)).sconcat())
        }
    }
}
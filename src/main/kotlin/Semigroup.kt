interface Semigroup<T> {
    infix fun T.assocOp(other: T): T

    fun Iterable<T>.sconcat() = this.reduce { acc, t -> with(this@Semigroup) { acc assocOp t } }

    fun Sequence<T>.sconcat() = this.reduce { acc, t -> with(this@Semigroup) { acc assocOp t } }

    fun T.stimes(n: Int): T = (1..n).asSequence().map { this }.sconcat()
}

open class SemigroupImpl<T>(val operation: (T, T) -> T) : Semigroup<T> {
    override fun T.assocOp(other: T): T = operation(this, other)
}

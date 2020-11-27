interface Monoid<T> : Semigroup<T> {
    val mempty: T

    fun Iterable<T>.mconcat() =
        if (this.none()) this@Monoid.mempty else with(this@Monoid as Semigroup<T>) { this@mconcat.sconcat() }

    fun Sequence<T>.mconcat() =
        if (this.none()) this@Monoid.mempty else with(this@Monoid as Semigroup<T>) { this@mconcat.sconcat() }

    fun T.mtimes(n: Int) =
        if (n == 0) this@Monoid.mempty else with(this@Monoid as Semigroup<T>) { this@mtimes.stimes(n) }
}

open class MonoidImpl<T>(override val mempty: T, operation: (T, T) -> T) : Monoid<T>, SemigroupImpl<T>(operation)

object IterableMonad {
    data class Wrapper<T>(val values: Iterable<T>) : Iterable<T> {
        fun <R> fmap(f: (T) -> R): Wrapper<R> = Wrapper(this.values.map(f))
        fun <R> then(f: (T) -> Wrapper<R>): Wrapper<R> = Wrapper(this.values.flatMap { f(it).values })
        fun <R> thenIgnoring(f: (T) -> Wrapper<R>): Wrapper<T> =
            Wrapper(this.values.flatMap { x -> f(x).fmap { x } })

        infix fun or(other: Wrapper<T>) = this + other
        override fun iterator() = values.iterator()
    }

    fun <T> returns(item: T): Wrapper<T> = Wrapper(listOf(item))
    operator fun <T> invoke(f: IterableMonad.() -> Wrapper<T>): Wrapper<T> = IterableMonad.f()
}
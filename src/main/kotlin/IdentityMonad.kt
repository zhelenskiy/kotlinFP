object IdentityMonad {
    data class Wrapper<T>(val value: T) {
        fun <R> fmap(f: (T) -> R): Wrapper<R> = Wrapper(f(this.value))
        fun <R> then(f: (T) -> Wrapper<R>): Wrapper<R> = f(this.value)
        fun <R> thenIgnoring(f: (T) -> Wrapper<R>): Wrapper<T> = then(f).fmap { value }
        infix fun or(@Suppress("UNUSED_PARAMETER") other: Wrapper<T>) = this
    }

    fun <T> returns(item: T): Wrapper<T> = Wrapper(item)
    operator fun <T> invoke(f: IdentityMonad.() -> Wrapper<T>): Wrapper<T> = IdentityMonad.f()
}


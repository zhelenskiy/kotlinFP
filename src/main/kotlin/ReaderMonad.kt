class ReaderMonad<R>(val reader: R) {
    inner class Wrapper<T>(val value: T) {
        fun <S> fmap(f: (T) -> S): Wrapper<S> = Wrapper(f(this.value))
        fun <S> then(f: (T) -> Wrapper<S>): Wrapper<S> = f(this.value)
        fun <S> thenIgnoring(f: (T) -> Wrapper<S>): Wrapper<T> = then(f).fmap { value }

        fun withReader(modification: (R) -> R, action: (Wrapper<T>) -> Wrapper<T>): Wrapper<T> =
            with(ReaderMonad(modification(reader))) { action(Wrapper(this@Wrapper.value)) }
    }

    fun ask() = Wrapper(reader)

    fun <A> asks(f: (R) -> A) = ask().fmap(f)

    fun <T> returns(item: T): Wrapper<T> = Wrapper(item)
    operator fun <T> invoke(f: MaybeMonad.() -> Wrapper<T>): Wrapper<T> = MaybeMonad.f()
    fun <T> runValue(f: MaybeMonad.() -> Wrapper<T>): T = invoke(f).value
}
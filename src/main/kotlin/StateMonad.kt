object StateMonad {
    class Wrapper<S, T>(val state: S, val value: T) {
        fun <R> fmap(f: (T) -> R): Wrapper<S, R> = Wrapper(state, f(value))
        fun <R> then(f: (T) -> Wrapper<S, R>): Wrapper<S, R> = f(this.value)
        fun <R> thenIgnoring(f: (T) -> Wrapper<S, R>): Wrapper<S, T> = then(f).fmap { value }
        fun put(state: S) = Wrapper(state, value)
        fun put(state: Pair<S, T>) = Wrapper(state.first, state.second)
        fun modify(f: (S) -> S) = Wrapper(f(state), value)
        fun <R> gets(f: (S) -> R) = Wrapper(state, f(state))
    }


    fun <S, T> returns(state: S, item: T): Wrapper<S, T> = Wrapper(state, item)
    operator fun <S, T> invoke(f: MaybeMonad.() -> Wrapper<S, T>): Wrapper<S, T> = MaybeMonad.f()
    fun <S, T> runValue(f: MaybeMonad.() -> Wrapper<S, T>): T = invoke(f).value
}
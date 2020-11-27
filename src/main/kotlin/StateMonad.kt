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
    operator fun <S, T> invoke(f: StateMonad.() -> Wrapper<S, T>): Wrapper<S, T> = this.f()
    operator fun invoke(): Wrapper<Unit, Unit> = returns(Unit, Unit)
    fun <S, T> runValue(f: StateMonad.() -> Wrapper<S, T>): T = invoke(f).value

    fun <S, T1, R> ((T1) -> R).lift(a: Wrapper<S, T1>) = a.fmap(this)
    fun <S, T1, T2, R> ((T1, T2) -> R).lift(a: Wrapper<S, T1>, b: Wrapper<S, T2>) =
        a.then { a1: T1 -> { a2: T2 -> this(a1, a2) }.lift(b) }

    fun <S, T1, T2, T3, R> ((T1, T2, T3) -> R).lift(a: Wrapper<S, T1>, b: Wrapper<S, T2>, c: Wrapper<S, T3>) =
        a.then { a1: T1 -> { a2: T2, a3: T3 -> this(a1, a2, a3) }.lift(b, c) }

    fun <S, T1, T2, T3, T4, R> ((T1, T2, T3, T4) -> R).lift(
        a: Wrapper<S, T1>,
        b: Wrapper<S, T2>,
        c: Wrapper<S, T3>,
        d: Wrapper<S, T4>
    ) = a.then { a1: T1 -> { a2: T2, a3: T3, a4: T4 -> this(a1, a2, a3, a4) }.lift(b, c, d) }

    fun <S, T1, T2, T3, T4, T5, R> ((T1, T2, T3, T4, T5) -> R).lift(
        a: Wrapper<S, T1>,
        b: Wrapper<S, T2>,
        c: Wrapper<S, T3>,
        d: Wrapper<S, T4>,
        e: Wrapper<S, T5>
    ) = a.then { a1: T1 -> { a2: T2, a3: T3, a4: T4, a5: T5 -> this(a1, a2, a3, a4, a5) }.lift(b, c, d, e) }
}
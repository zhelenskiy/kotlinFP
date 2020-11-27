object IdentityMonad {
    data class Wrapper<T>(val value: T) {
        fun <R> fmap(f: (T) -> R): Wrapper<R> = Wrapper(f(this.value))
        fun <R> then(f: (T) -> Wrapper<R>): Wrapper<R> = f(this.value)
        fun <R> thenIgnoring(f: (T) -> Wrapper<R>): Wrapper<T> = then(f).fmap { value }
        infix fun or(@Suppress("UNUSED_PARAMETER") other: Wrapper<T>) = this
    }

    fun <T> returns(item: T): Wrapper<T> = Wrapper(item)
    operator fun <T> invoke(f: IdentityMonad.() -> Wrapper<T>): Wrapper<T> = this.f()
    operator fun invoke(): Wrapper<Unit> = returns(Unit)

    fun <T1, R> ((T1) -> R).lift(a: Wrapper<T1>) = a.fmap(this)
    fun <T1, T2, R> ((T1, T2) -> R).lift(a: Wrapper<T1>, b: Wrapper<T2>) =
        a.then { a1: T1 -> { a2: T2 -> this(a1, a2) }.lift(b) }

    fun <T1, T2, T3, R> ((T1, T2, T3) -> R).lift(a: Wrapper<T1>, b: Wrapper<T2>, c: Wrapper<T3>) =
        a.then { a1: T1 -> { a2: T2, a3: T3 -> this(a1, a2, a3) }.lift(b, c) }

    fun <T1, T2, T3, T4, R> ((T1, T2, T3, T4) -> R).lift(
        a: Wrapper<T1>,
        b: Wrapper<T2>,
        c: Wrapper<T3>,
        d: Wrapper<T4>
    ) = a.then { a1: T1 -> { a2: T2, a3: T3, a4: T4 -> this(a1, a2, a3, a4) }.lift(b, c, d) }

    fun <T1, T2, T3, T4, T5, R> ((T1, T2, T3, T4, T5) -> R).lift(
        a: Wrapper<T1>,
        b: Wrapper<T2>,
        c: Wrapper<T3>,
        d: Wrapper<T4>,
        e: Wrapper<T5>
    ) = a.then { a1: T1 -> { a2: T2, a3: T3, a4: T4, a5: T5 -> this(a1, a2, a3, a4, a5) }.lift(b, c, d, e) }
}

typealias Id<T> = IdentityMonad.Wrapper<T>


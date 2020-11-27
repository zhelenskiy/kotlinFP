object MaybeMonad {
    sealed class Maybe<T> {
        data class Just<T>(val value: T) : Maybe<T>()

        class None<T> : Maybe<T>() {
            override fun equals(other: Any?): Boolean = other is None<*>
            override fun hashCode(): Int = 0
        }

        fun <R> fmap(f: (T) -> R): Maybe<R> = when (this) {
            is Just -> Just(f(this.value))
            is None -> None()
        }

        fun <R> then(f: (T) -> Maybe<R>): Maybe<R> = when (this) {
            is Just -> f(this.value)
            is None -> None()
        }

        fun <R> thenIgnoring(f: (T) -> Maybe<R>): Maybe<T> = when (this) {
            is Just -> f(this.value).let { this }
            is None -> None()
        }

        infix fun or(other: Maybe<T>): Maybe<T> = when (this) {
            is Just -> this
            is None -> other
        }
    }

    fun <T> fail(@Suppress("UNUSED_PARAMETER") msg: String = ""): Maybe<T> = Maybe.None()
    fun <T> returns(item: T): Maybe<T> = Maybe.Just(item)
    operator fun <T> invoke(f: MaybeMonad.() -> Maybe<T>): Maybe<T> = this.f()
    operator fun invoke(): Maybe<Unit> = returns(Unit)

    fun <T1, R> ((T1) -> R).lift(a: Maybe<T1>) = a.fmap(this)
    fun <T1, T2, R> ((T1, T2) -> R).lift(a: Maybe<T1>, b: Maybe<T2>) =
        a.then { a1: T1 -> { a2: T2 -> this(a1, a2) }.lift(b) }

    fun <T1, T2, T3, R> ((T1, T2, T3) -> R).lift(a: Maybe<T1>, b: Maybe<T2>, c: Maybe<T3>) =
        a.then { a1: T1 -> { a2: T2, a3: T3 -> this(a1, a2, a3) }.lift(b, c) }

    fun <T1, T2, T3, T4, R> ((T1, T2, T3, T4) -> R).lift(
        a: Maybe<T1>,
        b: Maybe<T2>,
        c: Maybe<T3>,
        d: Maybe<T4>
    ) = a.then { a1: T1 -> { a2: T2, a3: T3, a4: T4 -> this(a1, a2, a3, a4) }.lift(b, c, d) }

    fun <T1, T2, T3, T4, T5, R> ((T1, T2, T3, T4, T5) -> R).lift(
        a: Maybe<T1>,
        b: Maybe<T2>,
        c: Maybe<T3>,
        d: Maybe<T4>,
        e: Maybe<T5>
    ) = a.then { a1: T1 -> { a2: T2, a3: T3, a4: T4, a5: T5 -> this(a1, a2, a3, a4, a5) }.lift(b, c, d, e) }
}

typealias Maybe<T> = MaybeMonad.Maybe<T>
typealias Just<T> = MaybeMonad.Maybe.Just<T>
typealias None<T> = MaybeMonad.Maybe.None<T>
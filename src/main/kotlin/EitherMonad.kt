object EitherMonad {
    sealed class Either<L, R> {
        data class Right<L, R>(val value: R) : Either<L, R>()

        data class Left<L, R>(val value: L) : Either<L, R>()

        fun <S> fmap(f: (R) -> S): Either<L, S> = when (this) {
            is Right -> Right(f(this.value))
            is Left -> Left(this.value)
        }

        fun <S> then(f: (R) -> Either<L, S>): Either<L, S> = when (this) {
            is Right -> f(this.value)
            is Left -> Left(this.value)
        }

        fun <S> thenIgnoring(f: (R) -> Either<L, S>): Either<L, R> = when (this) {
            is Right -> f(this.value).let { this }
            is Left -> Left(this.value)
        }

        infix fun or(other: Either<L, R>): Either<L, R> = when (this) {
            is Right -> this
            is Left -> other
        }
    }

    fun <L, R> returns(item: R): Either<L, R> = Either.Right(item)
    fun <R> fail(msg: String): Either<String, R> = Either.Left(msg)
    operator fun <L, R> invoke(f: EitherMonad.() -> Either<L, R>): Either<L, R> = this.f()
    operator fun invoke(): Either<Unit, Unit> = returns(Unit)

    fun <L, T1, R> ((T1) -> R).lift(a: Either<L, T1>) = a.fmap(this)
    fun <L, T1, T2, R> ((T1, T2) -> R).lift(a: Either<L, T1>, b: Either<L, T2>) =
        a.then { a1: T1 -> { a2: T2 -> this(a1, a2) }.lift(b) }

    fun <L, T1, T2, T3, R> ((T1, T2, T3) -> R).lift(a: Either<L, T1>, b: Either<L, T2>, c: Either<L, T3>) =
        a.then { a1: T1 -> { a2: T2, a3: T3 -> this(a1, a2, a3) }.lift(b, c) }

    fun <L, T1, T2, T3, T4, R> ((T1, T2, T3, T4) -> R).lift(
        a: Either<L, T1>,
        b: Either<L, T2>,
        c: Either<L, T3>,
        d: Either<L, T4>
    ) = a.then { a1: T1 -> { a2: T2, a3: T3, a4: T4 -> this(a1, a2, a3, a4) }.lift(b, c, d) }

    fun <L, T1, T2, T3, T4, T5, R> ((T1, T2, T3, T4, T5) -> R).lift(
        a: Either<L, T1>,
        b: Either<L, T2>,
        c: Either<L, T3>,
        d: Either<L, T4>,
        e: Either<L, T5>
    ) = a.then { a1: T1 -> { a2: T2, a3: T3, a4: T4, a5: T5 -> this(a1, a2, a3, a4, a5) }.lift(b, c, d, e) }
}

typealias Either<L, R> = EitherMonad.Either<L, R>
typealias Right<L, R> = EitherMonad.Either.Right<L, R>
typealias Left<L, R> = EitherMonad.Either.Left<L, R>
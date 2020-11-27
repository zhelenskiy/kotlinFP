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
    operator fun <L, R> invoke(f: EitherMonad.() -> Either<L, R>): Either<L, R> = EitherMonad.f()
}
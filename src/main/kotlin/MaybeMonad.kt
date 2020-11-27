object MaybeMonad {
    sealed class Maybe<T> {
        data class Just<T>(val value: T) : Maybe<T>()

        class None<T> : Maybe<T>()

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
    operator fun <T> invoke(f: MaybeMonad.() -> Maybe<T>): Maybe<T> = MaybeMonad.f()
}
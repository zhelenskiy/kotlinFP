class WriterMonad<W>(val monoid: Monoid<W>) {
    inner class Wrapper<T>(val value: T, val writer: W) {
        fun <R> fmap(f: (T) -> R): Wrapper<R> = Wrapper(f(this.value), writer)
        fun <R> then(f: (T) -> Wrapper<R>): Wrapper<R> = f(this.value)
        fun <R> thenIgnoring(f: (T) -> Wrapper<R>): Wrapper<T> = then(f).fmap { value }

        fun <R> tell(w: W, value: R) = Wrapper(value, with(monoid) { writer assocOp w })
        fun tell(w: W) = tell(w, Unit)

        fun listen() = fmap { Pair(this.value, this.writer) }
        fun <R> listens(f: (W) -> R) = fmap { Pair(this.value, f(this.writer)) }

        fun censor(f: (W) -> W) = Wrapper(value, f(writer))
    }

    fun <T> Wrapper<Pair<T, (W) -> W>>.pass() = fmap { it.first }.censor(this.value.second)

    fun <T> returns(item: T): Wrapper<T> = Wrapper(item, monoid.mempty)
    fun returns(): Wrapper<Unit> = returns(Unit)
    operator fun <T> invoke(f: WriterMonad<W>.() -> Wrapper<T>): Wrapper<T> = f()
    operator fun invoke(): Wrapper<Unit> = returns()
    fun <T> runValue(f: WriterMonad<W>.() -> Wrapper<T>): T = invoke(f).value
    fun <T> runWriter(f: WriterMonad<W>.() -> Wrapper<T>): W = invoke(f).writer

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
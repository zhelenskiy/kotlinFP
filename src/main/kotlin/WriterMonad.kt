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
    fun <T> run(f: WriterMonad<W>.() -> Wrapper<T>): Wrapper<T> = f()
    fun <T> runValue(f: WriterMonad<W>.() -> Wrapper<T>): T = run(f).value
    fun <T> runWriter(f: WriterMonad<W>.() -> Wrapper<T>): W = run(f).writer
}
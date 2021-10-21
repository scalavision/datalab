package bio.codec

import ColumnCodec.*

import zio.Chunk

trait RowCodec[A, B] extends Codec[A]:
  def split(s: String, splitter: Splitter[B]): Chunk[String] =
    val chunks = Chunk.fromArray(s.split(splitter.splitChar).map(_.trim()))
    // java split method will delete the last empty string, we need to
    // add it back to avoid NullPointerException
    if s.endsWith(",") then chunks :+ ""
    else chunks

object RowCodec:

  def apply[A, B](using instance: RowCodec[A, B]): RowCodec[A, B] = instance

  def merge[B](chunks: Chunk[String])(using splitter: Splitter[B]): String =
    chunks.mkString(splitter.splitChar.toString)

  def to[A, B](
      decodeN: Chunk[String] => A,
      encodeN: A => String
  )(using
      splitter: Splitter[B]
  ): RowCodec[A, B] = new RowCodec[A, B] {
    def decode(a: String): A =
      val chunks = split(a, splitter)
      decodeN(chunks)
    def encode(a: A): String = encodeN(a)
  }

  given [S: Splitter]: RowCodec[EmptyTuple, S] =
    to(
      _ => EmptyTuple,
      _ => ""
    )

  given [C: ColumnCodec, T <: Tuple, S: Splitter](using
      rowCode: RowCodec[T, S]
  ): RowCodec[C *: T, S] = new RowCodec[C *: T, S] {
    def decode(s: String) =
      val char = summon[Splitter[S]].splitChar
      val chunks = Chunk.fromArray(s.split(char))
      summon[ColumnCodec[C]].decode(chunks.head) *: rowCode.decode(
        chunks.mkString
      )
    def encode(tuple: C *: T) =
      summon[ColumnCodec[C]].encode(tuple.head) + summon[RowCodec[T, S]].encode(
        tuple.tail
      )
  }

  given chunkCodec[A, B](using
      colCodec: ColumnCodec[A],
      splitter: Splitter[B]
  ): RowCodec[Chunk[A], B] = to(
    chunks => chunks.map(colCodec.decode),
    as => merge(as.map(colCodec.encode))
  )

  given tuple2[A, B, C](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      splitter: Splitter[C]
  ): RowCodec[(A, B), C] = to(
    chunks => (colA.decode(chunks(0)), colB.decode(chunks(1))),
    (a, b) => merge(Chunk.apply(colA.encode(a), colB.encode(b)))
  )

  given tuple3[A, B, C, D](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      splitter: Splitter[D]
  ): RowCodec[(A, B, C), D] = to(
    chunks =>
      (colA.decode(chunks(0)), colB.decode(chunks(1)), colC.decode(chunks(2))),
    (a, b, c) =>
      merge(Chunk.apply(colA.encode(a), colB.encode(b), colC.encode(c)))
  )

  given tuple4[A, B, C, D, E](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      splitter: Splitter[E]
  ): RowCodec[(A, B, C, D), E] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3))
      ),
    (a, b, c, d) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d)
        )
      )
  )

  given tuple5[A, B, C, D, E, F](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      splitter: Splitter[F]
  ): RowCodec[(A, B, C, D, E), F] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4))
      ),
    (a, b, c, d, e) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e)
        )
      )
  )

  given tuple6[A, B, C, D, E, F, G](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      splitter: Splitter[G]
  ): RowCodec[(A, B, C, D, E, F), G] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5))
      ),
    (a, b, c, d, e, f) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f)
        )
      )
  )

  given tuple7[A, B, C, D, E, F, G, H](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      splitter: Splitter[H]
  ): RowCodec[(A, B, C, D, E, F, G), H] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6))
      ),
    (a, b, c, d, e, f, g) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g)
        )
      )
  )

  given tuple8[A, B, C, D, E, F, G, H, I](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      splitter: Splitter[I]
  ): RowCodec[(A, B, C, D, E, F, G, H), I] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7))
      ),
    (a, b, c, d, e, f, g, h) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h)
        )
      )
  )

  given tuple9[A, B, C, D, E, F, G, H, I, J](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      splitter: Splitter[J]
  ): RowCodec[(A, B, C, D, E, F, G, H, I), J] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8))
      ),
    (a, b, c, d, e, f, g, h, i) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i)
        )
      )
  )

  given tuple10[A, B, C, D, E, F, G, H, I, J, K](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      splitter: Splitter[K]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J), K] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9))
      ),
    (a, b, c, d, e, f, g, h, i, j) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j)
        )
      )
  )

  given tuple11[A, B, C, D, E, F, G, H, I, J, K, L](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      splitter: Splitter[L]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K), L] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10))
      ),
    (a, b, c, d, e, f, g, h, i, j, k) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k)
        )
      )
  )

  given tuple12[A, B, C, D, E, F, G, H, I, J, K, L, M](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      splitter: Splitter[M]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L), M] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l)
        )
      )
  )

  given tuple13[A, B, C, D, E, F, G, H, I, J, K, L, M, N](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      splitter: Splitter[N]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L, M), N] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m)
        )
      )
  )

  given tuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      splitter: Splitter[O]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N), O] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n)
        )
      )
  )

  given tuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      splitter: Splitter[P]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O), P] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o)
        )
      )
  )

  given tuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      splitter: Splitter[Q]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P), Q] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14)),
        colP.decode(chunks(15))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o),
          colP.encode(p)
        )
      )
  )

  given tuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      splitter: Splitter[R]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q), R] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14)),
        colP.decode(chunks(15)),
        colQ.decode(chunks(16))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o),
          colP.encode(p),
          colQ.encode(q)
        )
      )
  )

  given tuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      colR: ColumnCodec[R],
      splitter: Splitter[S]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R), S] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14)),
        colP.decode(chunks(15)),
        colQ.decode(chunks(16)),
        colR.decode(chunks(17))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o),
          colP.encode(p),
          colQ.encode(q),
          colR.encode(r)
        )
      )
  )

  given tuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](
      using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      colR: ColumnCodec[R],
      colS: ColumnCodec[S],
      splitter: Splitter[T]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S), T] =
    to(
      chunks =>
        (
          colA.decode(chunks(0)),
          colB.decode(chunks(1)),
          colC.decode(chunks(2)),
          colD.decode(chunks(3)),
          colE.decode(chunks(4)),
          colF.decode(chunks(5)),
          colG.decode(chunks(6)),
          colH.decode(chunks(7)),
          colI.decode(chunks(8)),
          colJ.decode(chunks(9)),
          colK.decode(chunks(10)),
          colL.decode(chunks(11)),
          colM.decode(chunks(12)),
          colN.decode(chunks(13)),
          colO.decode(chunks(14)),
          colP.decode(chunks(15)),
          colQ.decode(chunks(16)),
          colR.decode(chunks(17)),
          colS.decode(chunks(18))
        ),
      (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) =>
        merge(
          Chunk.apply(
            colA.encode(a),
            colB.encode(b),
            colC.encode(c),
            colD.encode(d),
            colE.encode(e),
            colF.encode(f),
            colG.encode(g),
            colH.encode(h),
            colI.encode(i),
            colJ.encode(j),
            colK.encode(k),
            colL.encode(l),
            colM.encode(m),
            colN.encode(n),
            colO.encode(o),
            colP.encode(p),
            colQ.encode(q),
            colR.encode(r),
            colS.encode(s)
          )
        )
    )

  given tuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](
      using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      colR: ColumnCodec[R],
      colS: ColumnCodec[S],
      colT: ColumnCodec[T],
      splitter: Splitter[U]
  ): RowCodec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T), U] =
    to(
      chunks =>
        (
          colA.decode(chunks(0)),
          colB.decode(chunks(1)),
          colC.decode(chunks(2)),
          colD.decode(chunks(3)),
          colE.decode(chunks(4)),
          colF.decode(chunks(5)),
          colG.decode(chunks(6)),
          colH.decode(chunks(7)),
          colI.decode(chunks(8)),
          colJ.decode(chunks(9)),
          colK.decode(chunks(10)),
          colL.decode(chunks(11)),
          colM.decode(chunks(12)),
          colN.decode(chunks(13)),
          colO.decode(chunks(14)),
          colP.decode(chunks(15)),
          colQ.decode(chunks(16)),
          colR.decode(chunks(17)),
          colS.decode(chunks(18)),
          colT.decode(chunks(19))
        ),
      (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) =>
        merge(
          Chunk.apply(
            colA.encode(a),
            colB.encode(b),
            colC.encode(c),
            colD.encode(d),
            colE.encode(e),
            colF.encode(f),
            colG.encode(g),
            colH.encode(h),
            colI.encode(i),
            colJ.encode(j),
            colK.encode(k),
            colL.encode(l),
            colM.encode(m),
            colN.encode(n),
            colO.encode(o),
            colP.encode(p),
            colQ.encode(q),
            colR.encode(r),
            colS.encode(s),
            colT.encode(t)
          )
        )
    )

  given tuple21[
      A,
      B,
      C,
      D,
      E,
      F,
      G,
      H,
      I,
      J,
      K,
      L,
      M,
      N,
      O,
      P,
      Q,
      R,
      S,
      T,
      U,
      V
  ](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      colR: ColumnCodec[R],
      colS: ColumnCodec[S],
      colT: ColumnCodec[T],
      colU: ColumnCodec[U],
      splitter: Splitter[V]
  ): RowCodec[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U),
    V
  ] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14)),
        colP.decode(chunks(15)),
        colQ.decode(chunks(16)),
        colR.decode(chunks(17)),
        colS.decode(chunks(18)),
        colT.decode(chunks(19)),
        colU.decode(chunks(20))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o),
          colP.encode(p),
          colQ.encode(q),
          colR.encode(r),
          colS.encode(s),
          colT.encode(t),
          colU.encode(u)
        )
      )
  )

  given tuple22[
      A,
      B,
      C,
      D,
      E,
      F,
      G,
      H,
      I,
      J,
      K,
      L,
      M,
      N,
      O,
      P,
      Q,
      R,
      S,
      T,
      U,
      V,
      W
  ](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      colR: ColumnCodec[R],
      colS: ColumnCodec[S],
      colT: ColumnCodec[T],
      colU: ColumnCodec[U],
      colV: ColumnCodec[V],
      splitter: Splitter[W]
  ): RowCodec[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V),
    W
  ] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14)),
        colP.decode(chunks(15)),
        colQ.decode(chunks(16)),
        colR.decode(chunks(17)),
        colS.decode(chunks(18)),
        colT.decode(chunks(19)),
        colU.decode(chunks(20)),
        colV.decode(chunks(21))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o),
          colP.encode(p),
          colQ.encode(q),
          colR.encode(r),
          colS.encode(s),
          colT.encode(t),
          colU.encode(u),
          colV.encode(v)
        )
      )
  )

  given tuple23[
      A,
      B,
      C,
      D,
      E,
      F,
      G,
      H,
      I,
      J,
      K,
      L,
      M,
      N,
      O,
      P,
      Q,
      R,
      S,
      T,
      U,
      V,
      W,
      X
  ](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      colR: ColumnCodec[R],
      colS: ColumnCodec[S],
      colT: ColumnCodec[T],
      colU: ColumnCodec[U],
      colV: ColumnCodec[V],
      colW: ColumnCodec[W],
      splitter: Splitter[X]
  ): RowCodec[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W),
    X
  ] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14)),
        colP.decode(chunks(15)),
        colQ.decode(chunks(16)),
        colR.decode(chunks(17)),
        colS.decode(chunks(18)),
        colT.decode(chunks(19)),
        colU.decode(chunks(20)),
        colV.decode(chunks(21)),
        colW.decode(chunks(22))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o),
          colP.encode(p),
          colQ.encode(q),
          colR.encode(r),
          colS.encode(s),
          colT.encode(t),
          colU.encode(u),
          colV.encode(v),
          colW.encode(w)
        )
      )
  )

  given tuple24[
      A,
      B,
      C,
      D,
      E,
      F,
      G,
      H,
      I,
      J,
      K,
      L,
      M,
      N,
      O,
      P,
      Q,
      R,
      S,
      T,
      U,
      V,
      W,
      X,
      Y
  ](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      colR: ColumnCodec[R],
      colS: ColumnCodec[S],
      colT: ColumnCodec[T],
      colU: ColumnCodec[U],
      colV: ColumnCodec[V],
      colW: ColumnCodec[W],
      colX: ColumnCodec[X],
      splitter: Splitter[Y]
  ): RowCodec[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X),
    Y
  ] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14)),
        colP.decode(chunks(15)),
        colQ.decode(chunks(16)),
        colR.decode(chunks(17)),
        colS.decode(chunks(18)),
        colT.decode(chunks(19)),
        colU.decode(chunks(20)),
        colV.decode(chunks(21)),
        colW.decode(chunks(22)),
        colX.decode(chunks(23))
      ),
    (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o),
          colP.encode(p),
          colQ.encode(q),
          colR.encode(r),
          colS.encode(s),
          colT.encode(t),
          colU.encode(u),
          colV.encode(v),
          colW.encode(w),
          colX.encode(x)
        )
      )
  )

  given tuple25[
      A,
      B,
      C,
      D,
      E,
      F,
      G,
      H,
      I,
      J,
      K,
      L,
      M,
      N,
      O,
      P,
      Q,
      R,
      S,
      T,
      U,
      V,
      W,
      X,
      Y,
      Z
  ](using
      colA: ColumnCodec[A],
      colB: ColumnCodec[B],
      colC: ColumnCodec[C],
      colD: ColumnCodec[D],
      colE: ColumnCodec[E],
      colF: ColumnCodec[F],
      colG: ColumnCodec[G],
      colH: ColumnCodec[H],
      colI: ColumnCodec[I],
      colJ: ColumnCodec[J],
      colK: ColumnCodec[K],
      colL: ColumnCodec[L],
      colM: ColumnCodec[M],
      colN: ColumnCodec[N],
      colO: ColumnCodec[O],
      colP: ColumnCodec[P],
      colQ: ColumnCodec[Q],
      colR: ColumnCodec[R],
      colS: ColumnCodec[S],
      colT: ColumnCodec[T],
      colU: ColumnCodec[U],
      colV: ColumnCodec[V],
      colW: ColumnCodec[W],
      colX: ColumnCodec[X],
      colY: ColumnCodec[Y],
      splitter: Splitter[Z]
  ): RowCodec[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y),
    Z
  ] = to(
    chunks =>
      (
        colA.decode(chunks(0)),
        colB.decode(chunks(1)),
        colC.decode(chunks(2)),
        colD.decode(chunks(3)),
        colE.decode(chunks(4)),
        colF.decode(chunks(5)),
        colG.decode(chunks(6)),
        colH.decode(chunks(7)),
        colI.decode(chunks(8)),
        colJ.decode(chunks(9)),
        colK.decode(chunks(10)),
        colL.decode(chunks(11)),
        colM.decode(chunks(12)),
        colN.decode(chunks(13)),
        colO.decode(chunks(14)),
        colP.decode(chunks(15)),
        colQ.decode(chunks(16)),
        colR.decode(chunks(17)),
        colS.decode(chunks(18)),
        colT.decode(chunks(19)),
        colU.decode(chunks(20)),
        colV.decode(chunks(21)),
        colW.decode(chunks(22)),
        colX.decode(chunks(23)),
        colY.decode(chunks(24))
      ),
    (
        a,
        b,
        c,
        d,
        e,
        f,
        g,
        h,
        i,
        j,
        k,
        l,
        m,
        n,
        o,
        p,
        q,
        r,
        s,
        t,
        u,
        v,
        w,
        x,
        y
    ) =>
      merge(
        Chunk.apply(
          colA.encode(a),
          colB.encode(b),
          colC.encode(c),
          colD.encode(d),
          colE.encode(e),
          colF.encode(f),
          colG.encode(g),
          colH.encode(h),
          colI.encode(i),
          colJ.encode(j),
          colK.encode(k),
          colL.encode(l),
          colM.encode(m),
          colN.encode(n),
          colO.encode(o),
          colP.encode(p),
          colQ.encode(q),
          colR.encode(r),
          colS.encode(s),
          colT.encode(t),
          colU.encode(u),
          colV.encode(v),
          colW.encode(w),
          colX.encode(x),
          colY.encode(y)
        )
      )
  )

package uk.ac.lshtm.keppel.cli.util

fun <T> List<T>.uniquePairs(): Sequence<Pair<T, T>> {
    val list = this
    return sequence {
        list.forEachIndexed { index, item ->
            list.subList(index + 1, list.size).forEach {
                yield(Pair(item, it))
            }
        }
    }
}
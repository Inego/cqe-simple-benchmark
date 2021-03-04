import com.googlecode.cqengine.ConcurrentIndexedCollection
import com.googlecode.cqengine.attribute.support.FunctionalSimpleAttribute
import com.googlecode.cqengine.index.hash.HashIndex
import com.googlecode.cqengine.query.QueryFactory
import kotlin.reflect.KProperty1
import kotlin.system.measureNanoTime

const val SIZE = 10_000_000
val range = 1..SIZE


fun main() {

    println("Starting benchmark, SIZE = $SIZE...")

    println("Generating original collection...")
    val collection = range.map { Record(it) }

    hashMapBenchmark(collection)
    cqEngineBenchmark(collection)
}


private fun hashMapBenchmark(collection: List<Record>) {

    println("Plain hash map benchmark...")

    println("Indexing with a linked hash map...")
    val map = collection.associateBy { it.int }

    println("Performing benchmark...")
    val elapsed = measureNanoTime {
        range.forEach {
            map[it]
        }
    }

    println("Hash map: $elapsed ns")
}


private fun cqEngineBenchmark(collection: List<Record>) {

    println("CQEngine benchmark...")

    val attr = attribute(Record::int)
    val indexedEntries = ConcurrentIndexedCollection<Record>()
    indexedEntries.addIndex(HashIndex.onAttribute(attr))

    println("Adding entries to indexed collection...")
    indexedEntries.addAll(collection)

    println("Performing benchmark...")
    val elapsed = measureNanoTime {
        range.forEach {
            indexedEntries.retrieve(QueryFactory.equal(attr, it))
        }
    }

    println("ConcurrentIndexedCollection: $elapsed ns")
}


inline fun <reified O, reified A> attribute(accessor: KProperty1<O, A>): FunctionalSimpleAttribute<O, A> {
    return FunctionalSimpleAttribute(O::class.java, A::class.java, accessor.javaClass.simpleName) { accessor.get(it) }
}


data class Record(
    val int: Int
)

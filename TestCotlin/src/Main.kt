import com.trustverse.config.ParamsConfig

fun main(args: Array<String>) {
    try {
        val params = ParamsConfig(args)
        params.print()

        val a = intArrayOf(1, 2, 3, 4)
        println(a.filter({x -> x % 2 == 0}))
    } catch (e: Exception) {
        println(e.message)
    }
}

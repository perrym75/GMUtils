import com.trustverse.config.ParamsConfig

fun main(args: Array<String>) {
    try {
        val params = ParamsConfig(args)
        params.print()
    } catch (e: Exception) {
        println(e.message)
    }
}

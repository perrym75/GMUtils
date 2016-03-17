import com.trustverse.config.ParamsConfig
import com.trustverse.parser.LexicalTokenizer
import com.trustverse.utils.iterate

fun main(args: Array<String>) {
    try {
        var i = 0
        iterate { i++; if (i > 10) null else i }.forEach { println(it) }

        val params = ParamsConfig(args)
        params.print()

        val ep = LexicalTokenizer("-(1+2*7) / 2.2 ")
        for (token in ep) {
            println("Token type: ${token.Type} value: ${token.Value}")
        }
    } catch (e: Exception) {
        println(e.message)
    }
}

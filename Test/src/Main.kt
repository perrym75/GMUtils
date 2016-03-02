import com.trustverse.config.ParamsConfig
import com.trustverse.parser.LexicalParser

fun main(args: Array<String>) {
    try {
        val params = ParamsConfig(args)
        params.print()

        val ep = LexicalParser("-(1+2*7) \"2\" / 2.2")
        for (token in ep.tokenize()) {
            println("Token type: ${token.Type} value: ${token.Value}")
        }
    } catch (e: Exception) {
        println(e.message)
    }
}

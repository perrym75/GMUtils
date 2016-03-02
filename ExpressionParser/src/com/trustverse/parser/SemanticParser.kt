package com.trustverse.parser

import java.util.*

/**
 * Created by g.minkailov on 02.03.2016.
 */

class SemanticNode (val Parent: SemanticNode) {
    private val children = LinkedList<SemanticNode>()
    val Children: List<SemanticNode>
    get() = children
}
class SemanticParser(private val lexer: LexicalTokenizer) {
    fun parse() {
        for (token in lexer) {

        }
    }
}
package com.trustverse.parser

/**
 * Created by g.minkailov on 01.03.2016.
 */
enum class LexicalTokenType {
    NUMBER,
    OPERATOR,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    TEXT
}

enum class SymbolType(val Symbols: String) {
    DIGIT("0123456789"),
    NUMBER_DECIMAL_SEPARATOR(".,"),
    OPERATOR("+-*/"),
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    QUOTE("\""),
    LEXEME_SEPARATOR(" \t\r\n");

    companion object {
        fun fromChar(ch: Char): SymbolType {
            for (st in values()) {
                if (ch in st.Symbols) return st
            }

            throw IllegalArgumentException("Symbol '$ch' is not a part of grammar")
        }
    }
}

class LexicalTokenIterator(val expression: String) : Iterator<LexicalToken> {
    private var curTokenType: LexicalTokenType? = null
    private var curSymbolType: SymbolType? = null
    private var prevSymbolType: SymbolType? = null
    private var tokenValue = StringBuilder()
    private var curCharIndex = 0
    private var curToken: LexicalToken? = null

    override operator fun next(): LexicalToken {
        val ret = curToken
        curToken = null
        return ret!!
    }

    override operator fun hasNext(): Boolean {
        evalNext()
        return curToken != null
    }

    fun reset() {
        curTokenType = null
        prevSymbolType = null
        curCharIndex = 0
    }

    fun startNewToken(tokenType: LexicalTokenType?) {
        if (curTokenType != null && !tokenValue.isEmpty()) {
            curToken = LexicalToken(curTokenType!!, tokenValue.toString())
            tokenValue = StringBuilder()
        }
        curTokenType = tokenType
    }

    private fun evalNext() {
        while (curCharIndex < expression.length) {
            val ch = expression[curCharIndex]

            curSymbolType = SymbolType.fromChar(ch)
            if (curSymbolType != SymbolType.DIGIT && prevSymbolType == SymbolType.NUMBER_DECIMAL_SEPARATOR) throw IllegalArgumentException("Only digit can follow '$ch' symbol")

            if (curSymbolType != SymbolType.QUOTE && prevSymbolType == SymbolType.QUOTE && curTokenType == LexicalTokenType.TEXT) {
                tokenValue.append(ch)
                curCharIndex++
                continue
            }

            when (curSymbolType) {
                SymbolType.DIGIT -> {
                    if (curSymbolType != prevSymbolType && prevSymbolType != SymbolType.NUMBER_DECIMAL_SEPARATOR) {
                        startNewToken(LexicalTokenType.NUMBER)
                    }

                    tokenValue.append(ch)
                }

                SymbolType.NUMBER_DECIMAL_SEPARATOR -> {
                    if (curSymbolType != prevSymbolType && prevSymbolType != SymbolType.DIGIT) {
                        startNewToken(LexicalTokenType.NUMBER)
                    }

                    tokenValue.append(ch)
                }

                SymbolType.OPERATOR -> {
                    if (curSymbolType != prevSymbolType) {
                        startNewToken(LexicalTokenType.OPERATOR)
                    } else throw IllegalArgumentException("Two consequent operators")

                    tokenValue.append(ch)
                }

                SymbolType.LEFT_PARENTHESIS -> {
                    startNewToken(LexicalTokenType.LEFT_PARENTHESIS)

                    tokenValue.append(ch)
                }

                SymbolType.RIGHT_PARENTHESIS -> {
                    startNewToken(LexicalTokenType.RIGHT_PARENTHESIS)

                    tokenValue.append(ch)
                }

                SymbolType.QUOTE -> {
                    if (curTokenType == LexicalTokenType.TEXT) {
                        curSymbolType = null
                    } else {
                        startNewToken(LexicalTokenType.TEXT)
                    }

                    tokenValue.append(ch)
                }

                SymbolType.LEXEME_SEPARATOR -> {
                }

                else -> throw UnsupportedOperationException()
            }
            prevSymbolType = curSymbolType
            curCharIndex++

            if (curToken != null) return
        }

        startNewToken(null)
    }
}

class LexicalTokenizer(val expression: String) : Iterable<LexicalToken> {
    val iterator = LexicalTokenIterator(expression)

    override operator fun iterator(): Iterator<LexicalToken> {
        return iterator
    }
}
package com.trustverse.parser

import java.util.*

/**
 * Created by g.minkailov on 01.03.2016.
 */
enum class LexicalTokenType {
    START,
    NUMBER,
    OPERATOR,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    TEXT
}

enum class SymbolType(val Symbols: String) {
    NONE(""),
    DIGIT("0123456789"),
    NUMBER_DECIMAL_SEPARATOR(".,"),
    OPERATOR("+-*/"),
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    QUOTE("\""),
    LEXEME_SEPARATOR(" \r\n")
}

class LexicalToken(val Type: LexicalTokenType, val Value: String)

class LexicalParser(val expression: String) : Iterable<LexicalToken>, Iterator<LexicalToken> {
    private var curTokenType = LexicalTokenType.START
    private var curSymbolType = SymbolType.NONE
    private var prevSymbolType = SymbolType.NONE
    private var tokenValue = StringBuilder()
    private var curCharIndex = 0
    private var curToken: LexicalToken? = null

    override operator fun iterator(): Iterator<LexicalToken> {
        return this
    }

    override operator fun next(): LexicalToken {
        val ret = curToken
        curToken = null
        return ret!!
    }

    private fun getSymbolType(ch: Char): SymbolType {
        for (st in SymbolType.values()) {
            if (ch in st.Symbols) return st
        }

        throw IllegalArgumentException("Symbol '$ch' is not a part of grammar")
    }

    fun startNewToken(tokenType: LexicalTokenType) {
        if (curTokenType != LexicalTokenType.START && !tokenValue.isEmpty()) {
            curToken = LexicalToken(curTokenType, tokenValue.toString())
            tokenValue = StringBuilder()
        }
        curTokenType = tokenType
    }

    override operator fun hasNext(): Boolean {
        while (curCharIndex < expression.length) {
            val ch = expression[curCharIndex]

            curSymbolType = getSymbolType(ch)
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
                    } else throw IllegalArgumentException("Two consequent $ch operators")

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
                        curSymbolType = SymbolType.NONE
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

            if (curToken != null)
                return true
        }

        startNewToken(LexicalTokenType.START)
        if (curToken != null) {
            return true
        }

        curTokenType = LexicalTokenType.START
        prevSymbolType = SymbolType.NONE
        curCharIndex = 0

        return false
    }
}
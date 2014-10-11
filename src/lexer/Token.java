package lexer;

public class Token {
    public enum Kind {
        TOKEN_SELECT, // "select"
        TOKEN_FROM, // "from"
        TOKEN_WHERE, // "where"
        TOKEN_AND, // "and"
        TOKEN_OR, // "or"
        TOKEN_EQ, // "="
        TOKEN_GT, // ">"
        TOKEN_GTE, // ">="
        TOKEN_LT, // "<"
        TOKEN_LTE, // "<="
        TOKEN_NOT, // "<>, !"
        TOKEN_EOF, // ";"
        TOKEN_ID, // Identifier
        TOKEN_LPAREN, // "("
        TOKEN_RPAREN, // ")"
        TOKEN_LIT, // Literal:all float,int, string literal
        TOKEN_COMMA // ","
    }

    public Kind kind; // kind of the token
    public String lexeme; // extra lexeme for this token, if any
    public Integer lineNum; // on which line of the source file this token
                            // appears

    // Some tokens don't come with lexeme but
    // others do.
    public Token(Kind kind, Integer lineNum) {
        this.kind = kind;
        this.lineNum = lineNum;
    }

    public Token(Kind kind, Integer lineNum, String lexeme) {
        this(kind, lineNum);
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        String s;
        s = ": " + ((this.lexeme == null) ? "<NONE>" : this.lexeme)
                + " : at line " + this.lineNum.toString();
        return this.kind.toString() + s;
    }
}

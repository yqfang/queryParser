package com.unionpay.cloudatlas.util.hbase.index.lexer;

import java.util.HashMap;

import com.unionpay.cloudatlas.util.hbase.index.lexer.Token.Kind;

public class Lexer {
    char[] stm; // the statement to be analyzed
    int index; // the pointer to the next char to read
    Integer lineNum;
    static HashMap<String, Kind> symbolmap = new HashMap<String, Kind>();
    static {
        symbolmap.put("select", Kind.TOKEN_SELECT);
        symbolmap.put("from", Kind.TOKEN_FROM);
        symbolmap.put("where", Kind.TOKEN_WHERE);
        symbolmap.put("and", Kind.TOKEN_AND);
        symbolmap.put("or", Kind.TOKEN_OR);
    }
    private char read(){
        return stm[index ++];
    }
    
    private void reset(){
        index --;
    }

    public Lexer(String stm) {
        this.stm = (stm + ";").toCharArray();//to make sure every query ends with ;
        
        lineNum = 1;
    }

    // When called, return the next token (refer to the code "Token.java")
    // from the input stream.
    // Return TOKEN_EOF when reaching the end of the input stream.
    private Token nextTokenInternal() throws Exception {
        char c = read();
        // skip all kinds of "blanks"
        while (' ' == c || '\t' == c || '\n' == c || '\r' == c) {
            if ('\n' == c) {
                lineNum++;
            }
            c = read();
        }
        if (-1 == c)
            return new Token(Kind.TOKEN_EOF, lineNum);
       
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            StringBuffer buffer = new StringBuffer();
            do {
                buffer.append((char) c);
                c = read();
            } while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
                    || (c >= '0' && c <= '9') || (c == '_'));
            reset();
            String str = buffer.toString();
            if(symbolmap.containsKey(str))
                return new Token(symbolmap.get(str), lineNum);
            return new Token(Kind.TOKEN_ID, lineNum, str);
        }

        if (c >= '0' && c <= '9') {
            StringBuffer buffer = new StringBuffer();
            do{
                buffer.append(c);
                c = read();
            } while ((c >= '0' && c <= '9') || c == '.');
            reset();
            return new Token(Kind.TOKEN_LIT, lineNum, buffer.toString());
        }
        if (c == '"'){
            StringBuffer buffer = new StringBuffer();
            c = read();
            do{
                buffer.append(c);
                c = read();
                if (c == ';')
                    throw new Exception("maybe the \" mismatched!");
            } while (c != '"');
            return new Token(Kind.TOKEN_LIT, lineNum, buffer.toString());
        }
        if (c == '\''){
            StringBuffer buffer = new StringBuffer();
            c = read();
            do{
                buffer.append(c);
                c = read();
                if (c == ';')
                    throw new Exception("maybe the \' mismatched!");
            } while (c != '\'');
            return new Token(Kind.TOKEN_LIT, lineNum, buffer.toString());
        }
        
        if (';' == c){
            // The value for "lineNum" is now "null",
            // you should modify this to an appropriate
            // line number for the "EOF" token.
            return new Token(Kind.TOKEN_EOF, lineNum, ";");}
        switch (c) {
        case '>':
            if (read() == '=') {
                return new Token(Kind.TOKEN_GTE, lineNum);
            } else {
                reset();
                return new Token(Kind.TOKEN_GT, lineNum);
            }
        case '<':
            if (read() == '=') {
                return new Token(Kind.TOKEN_LTE, lineNum);
            } else {
                reset();
                if(read() == '>')
                    return new Token(Kind.TOKEN_NOT, lineNum);
                reset();
                return new Token(Kind.TOKEN_LT, lineNum);
            }    
        case '=':
            return new Token(Kind.TOKEN_EQ, lineNum);
        case '!':
            if(read() == '=')
                return new Token(Kind.TOKEN_NOT, lineNum);
            reset();
                throw new Exception("Unknow Token: ! near " + lineNum);
        case '(':
            return new Token(Kind.TOKEN_LPAREN, lineNum);
        case ')':
            return new Token(Kind.TOKEN_RPAREN, lineNum);
        case ',':
            return new Token(Kind.TOKEN_COMMA, lineNum);
        
        default:
           throw new Exception("Known CHAR: " + String.valueOf(c));
        }
    }

    public Token nextToken() {
        Token t = null;

        try {
            t = this.nextTokenInternal();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
           System.out.println(t.toString());
        return t;
    }
}

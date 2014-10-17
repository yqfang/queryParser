package com.unionpay.cloudatlas.util.hbase.index.parser;

import java.util.ArrayList;
import java.util.List;

import com.unionpay.cloudatlas.util.hbase.index.ast.Operator;
import com.unionpay.cloudatlas.util.hbase.index.ast.exp.Comparison;
import com.unionpay.cloudatlas.util.hbase.index.ast.exp.Compound;
import com.unionpay.cloudatlas.util.hbase.index.ast.exp.Or;
import com.unionpay.cloudatlas.util.hbase.index.bean.Query;
import com.unionpay.cloudatlas.util.hbase.index.lexer.Lexer;
import com.unionpay.cloudatlas.util.hbase.index.lexer.Token;
import com.unionpay.cloudatlas.util.hbase.index.lexer.Token.Kind;

public class Parser {
    Lexer lexer;
    Token current;

    public Parser(String stm) {
        lexer = new Lexer(stm);
        current = lexer.nextToken();
    }

    private void advance() {
        current = lexer.nextToken();
    }

    private void eatToken(Kind kind) {
        if (kind == current.kind)
            advance();
        else {
            System.err.println("Expects: " + kind.toString());
            System.err.println("But got: " + current.kind.toString());
            System.exit(1);
        }
    }

    private void error() {
        System.err.println("Syntax error: compilation aborting...\n");
        System.exit(1);
        return;
    }

    // Atom
    // -> LITERAL
    private String parseAtom() {
        String lit = current.lexeme;
        eatToken(Kind.TOKEN_LIT);
        return lit;
    }

    // And
    // -> (condition)
    // -> id <|<=|<>|=|!|>=|> LIT
    private Compound parseAndCondition() {
        switch (current.kind) {
        case TOKEN_ID:
            String left = current.lexeme;
            Operator opt = null;
            advance();
            switch (current.kind) {
            case TOKEN_LT:
                opt = Operator.LT;
                break;
            case TOKEN_LTE:
                opt = Operator.LTE;
                break;
            case TOKEN_NOT:
                opt = Operator.NEQ;
                break;
            case TOKEN_GT:
                opt = Operator.GT;
                break;
            case TOKEN_GTE:
                opt = Operator.GTE;
                break;
            case TOKEN_EQ:
                opt = Operator.EQ;
                break;
            default:
                System.out.println("Expects: " + "<|<=|<>|=|!|>=|>");
                System.out.println("But got: " + current.kind.toString());
                System.exit(1);
            }
            advance();
            String lit = parseAtom();
            return new Comparison(left, lit, opt);
        case TOKEN_LPAREN:
            advance();
            Compound exp = parseCondtition();
            eatToken(Kind.TOKEN_RPAREN);
            return exp;
        default:
            error();
            return null;
        }
    }

    // Or -> And AndRest*
    // AndRest -> and And
    private Compound parseOrCondition() {
        Or or = new Or(Operator.AND);
        Compound and = parseAndCondition();
        while (current.kind == Kind.TOKEN_AND) {
            advance();
            if (and.operator == Operator.AND)
                and.add(parseOrCondition());
            else
                or.add(parseAndCondition());// optimize the brace propagate
        }
        if (!or.hasChildren())
            return and;
        return or.add(and);
    }

    // Compound -> Or or Or
    // -> Or
    private Compound parseCondtition() {
        Compound con = new Compound(Operator.OR);
        Compound or = parseOrCondition();
        while (current.kind == Kind.TOKEN_OR) {
            advance();
            if (or.operator == Operator.OR)
                or.add(parseOrCondition());
            else
                con.add(parseOrCondition());// optimize the brace propagate
        }
        if (!con.hasChildren())
            return or;
        return con.add(or);
    }

    // ColumnList -> id ColumnRest*
    // ColumnRest -> , id
    private List<String> parseColumnList() {
        List<String> cols = new ArrayList<String>();
        cols.add(current.lexeme);
        eatToken(Kind.TOKEN_ID);
        while (current.kind == Kind.TOKEN_COMMA) {
            advance();
            cols.add(current.lexeme);
            eatToken(Kind.TOKEN_ID);
        }
        return cols;
    }

    // Main -> select columnlist from id where condition
    public Query parse() {
        eatToken(Kind.TOKEN_SELECT);
        List<String> cols = parseColumnList();
        eatToken(Kind.TOKEN_FROM);
        String tbl = current.lexeme;
        eatToken(Kind.TOKEN_ID);
        eatToken(Kind.TOKEN_WHERE);
        Compound exp = parseCondtition();
        if (current.kind != Kind.TOKEN_EOF) {
            System.err.println("Expects: " + Kind.TOKEN_EOF.toString());
            System.err.println("But got: " + current.kind.toString());
            System.exit(1);
        }
        System.err.println("Compilation completed!");
        System.err.println("There is no syntax error!");
        return new Query(cols, tbl, exp);
    }
}

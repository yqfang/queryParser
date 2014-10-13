package parser;

import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;
import ast.ColumnList;
import ast.Operator;
import ast.Query;
import ast.exp.Comparison;
import ast.exp.Compound;
import ast.exp.E;
import ast.exp.Or;

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
            System.out.println("Expects: " + kind.toString());
            System.out.println("But got: " + current.kind.toString());
            System.exit(1);
        }
    }

    private void error() {
        System.out.println("Syntax error: compilation aborting...\n");
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
    private E parseAndCondition() {
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
            E exp = parseCondtition();
            eatToken(Kind.TOKEN_RPAREN);
            return exp;
        default:
            error();
            return null;
        }
    }

    // Or      -> And AndRest*
    // AndRest -> and And
    private E parseOrCondition() {
        Or or = new Or(Operator.AND);
        E and = parseAndCondition();
        while (current.kind == Kind.TOKEN_AND) {
            advance();
            if(and.operator == Operator.AND)
                and.add(parseOrCondition());
            else
            or.add(parseAndCondition());//optimize the brace propagate
        }
        if (!or.hasChildren())
        return and;
        return or.add(and);
    }

    // Compound -> Or or Or
    //          -> Or
    private E parseCondtition() {
        Compound con = new Compound(Operator.OR);
        E or = parseOrCondition();
        while (current.kind == Kind.TOKEN_OR) {
            advance();
            if(or.operator == Operator.OR)
                or.add(parseOrCondition());
            else
            con.add(parseOrCondition());//optimize the brace propagate
        }
        if (!con.hasChildren())
        return or;
        return con.add(or);
    }

    // ColumnList -> id ColumnRest*
    // ColumnRest -> , id
    private ColumnList parseColumnList() {
        ColumnList cols = new ColumnList();
        cols.addCol(current.lexeme);
        eatToken(Kind.TOKEN_ID);
        while (current.kind == Kind.TOKEN_COMMA) {
            advance();
            cols.addCol(current.lexeme);
            eatToken(Kind.TOKEN_ID);
        }
        return cols;
    }

    // Main -> select columnlist from id where condition
    public Query parse() {
        eatToken(Kind.TOKEN_SELECT);
        ColumnList cols = parseColumnList();
        eatToken(Kind.TOKEN_FROM);
        String tbl = current.lexeme;
        eatToken(Kind.TOKEN_ID);
        eatToken(Kind.TOKEN_WHERE);
        E exp = parseCondtition();
        if (current.kind != Kind.TOKEN_EOF) {
            System.out.println("Expects: " + Kind.TOKEN_EOF.toString());
            System.out.println("But got: " + current.kind.toString());
            System.exit(1);
        }
        System.out.println("Compilation completed!");
        System.out.println("There is no syntax error.");
        return new Query(cols, tbl, exp);
    }
}

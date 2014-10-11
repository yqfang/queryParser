package parser;

import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;
import ast.ColumnList;
import ast.Query;
import ast.Table;
import ast.exp.And;
import ast.exp.Comparison;
import ast.exp.Comparison.Operator;
import ast.exp.E;
import ast.exp.Or;

public class Parser {
    Lexer lexer;
    Token current;

    public Parser(String stm) {
        lexer = new Lexer(stm + ";");
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
    //Atom
    // -> LITERAL
    private String parseAtom(){
        String lit = current.lexeme;
        eatToken(Kind.TOKEN_LIT);
        return lit;
    }
    // Add
    // -> (condition)

    // -> id <|<=|<>|=|!|>=|> LIT
    private E parseAndCondition() {
        switch (current.kind) {
        case TOKEN_ID:
            String left = current.lexeme;
            Comparison.Operator opt = null; 
            advance();
            switch(current.kind){
            case TOKEN_LT:
                opt = Operator.LT;break;
            case TOKEN_LTE:
                opt = Operator.LTE;break;
            case TOKEN_NOT:
                opt = Operator.NEQ;break;
            case TOKEN_GT:
                opt = Operator.GT;break;
            case TOKEN_GTE:
                opt = Operator.GTE;break;
            case TOKEN_EQ:
                opt = Operator.EQ;break;
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

    // Or -> And and And
    private E parseOrCondition() {
        And and = new And();
        and.add(parseAndCondition());
        while (current.kind == Kind.TOKEN_AND) {
            advance();
            and.add(parseAndCondition());
        }
        return and;
    }

    // Condition -> Or or Or
    // -> Or
    private E parseCondtition() {
        Or or = new Or();
        or.add(parseOrCondition());
        while (current.kind == Kind.TOKEN_OR) {
            advance();
            or.add(parseOrCondition());
        }
        return or;
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

    // Main -> select columnlist from id where conditions
    public Query parse() {
        eatToken(Kind.TOKEN_SELECT);
        ColumnList cols = parseColumnList();
        eatToken(Kind.TOKEN_FROM);
        String tbl = current.lexeme;
        eatToken(Kind.TOKEN_ID);
        eatToken(Kind.TOKEN_WHERE);
        E exp = parseCondtition();
        eatToken(Kind.TOKEN_EOF);
        return new Query(cols, new Table(tbl), exp);
    }
}

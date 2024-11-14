import java.util.Stack;
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
public Expr do_parse() throws Exception {
    return parseT();  // Begin parsing the expression from the top level (Term)
}

// Parses terms with addition or subtraction
private Expr parseT() throws Exception {
    Expr left = parseF();
    while (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
        Token op = consume(peek(TokenType.PLUS, 0) ? TokenType.PLUS : TokenType.MINUS);
        Expr right = parseT();
        left = (op.ty == TokenType.PLUS) ? new PlusExpr(left, right) : new MinusExpr(left, right);
    }
    return left;
}

// Parses factors with multiplication or division
private Expr parseF() throws Exception {
    Expr left = parsePrimary();
    while (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
        Token op = consume(peek(TokenType.TIMES, 0) ? TokenType.TIMES : TokenType.DIV);
        Expr right = parseF();
        left = (op.ty == TokenType.TIMES) ? new TimesExpr(left, right) : new DivExpr(left, right);
    }
    return left;
}

// Parses literals or parenthesized expressions
private Expr parsePrimary() throws Exception {
    if (peek(TokenType.NUM, 0)) {
        Token num = consume(TokenType.NUM);
        return new FloatExpr(Float.parseFloat(num.lexeme));
    } else if (peek(TokenType.LPAREN, 0)) {
        consume(TokenType.LPAREN); // consume '('
        Expr expr = parseT();
        consume(TokenType.RPAREN); // consume ')'
        return expr;
    } else {
        throw new ParseException("Expected a number or '('");
    }
}

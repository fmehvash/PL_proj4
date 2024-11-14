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
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'do_parse'");
        return parseT();
    }

    // Recursive method to parse T
    private Expr parseT() throws Exception {
        Expr left = parseF();
        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = consume(peek(TokenType.PLUS, 0) ? TokenType.PLUS : TokenType.MINUS);
            Expr right = parseT();
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(left, right);
            } else {
                return new MinusExpr(left, right);
            }
        }
        return left;
    }

    // Recursive method to parse F
    private Expr parseF() throws Exception {
        Expr left = parseLit();
        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = consume(peek(TokenType.TIMES, 0) ? TokenType.TIMES : TokenType.DIV);
            Expr right = parseF();
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(left, right);
            } else {
                return new DivExpr(left, right);
            }
        }
        return left;
    }

    // Method to parse Lit
    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token num = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(num.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN); // consume '('
            Expr expr = parseT();
            consume(TokenType.RPAREN); // consume ')'
            return expr;
        } else {
            throw new RuntimeException("Expected number or '('");
        }
    }

    // Method to handle AddOp (not really used due to inline handling in parseT)
    private Token parseAddOp() throws Exception {
        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            return consume(peek(TokenType.PLUS, 0) ? TokenType.PLUS : TokenType.MINUS);
        }
        throw new RuntimeException("Expected '+' or '-'");
    }

    // Method to handle MulOp (not really used due to inline handling in parseF)
    private Token parseMulOp() throws Exception {
        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            return consume(peek(TokenType.TIMES, 0) ? TokenType.TIMES : TokenType.DIV);
        }
        throw new RuntimeException("Expected '*' or '/'");
    }

}

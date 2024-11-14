import java.util.regex.Pattern;

public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
        init_lexer();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
        init_lexer();
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    // @Override
    // protected void init_lexer() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'init_lexer'");
    // }

    @Override
    protected void init_lexer() {
        // Initialize the lexer with the pattern
        this.lex = new LexerImpl();

        Automaton numAutomaton = createAutomatonForNum();
        lex.add_automaton(TokenType.NUM, numAutomaton);
    
        // Create and add automaton for PLUS token
        Automaton plusAutomaton = createAutomatonForPlus();
        lex.add_automaton(TokenType.PLUS, plusAutomaton);
    
        // Repeat the process for other token types
        Automaton minusAutomaton = createAutomatonForMinus();
        lex.add_automaton(TokenType.MINUS, minusAutomaton);
    
        Automaton timesAutomaton = createAutomatonForTimes();
        lex.add_automaton(TokenType.TIMES, timesAutomaton);
    
        Automaton divAutomaton = createAutomatonForDiv();
        lex.add_automaton(TokenType.DIV, divAutomaton);
    
        Automaton lParenAutomaton = createAutomatonForLParen();
        lex.add_automaton(TokenType.LPAREN, lParenAutomaton);
    
        Automaton rParenAutomaton = createAutomatonForRParen();
        lex.add_automaton(TokenType.RPAREN, rParenAutomaton);
    
        Automaton whiteSpaceAutomaton = createAutomatonForWhiteSpace();
        lex.add_automaton(TokenType.WHITE_SPACE, whiteSpaceAutomaton);



        // Add automata for other token types similarly
        // Note: Actual automaton setup to match each regex pattern is omitted for brevity
    }


    private Automaton createAutomatonForNum() {
        AutomatonImpl automaton = new AutomatonImpl();
        
        // Define states
        int startState = 0; // Start state
        int preDecimalState = 1; // State for digits before the decimal point (optional)
        int decimalPointState = 2; // State right after decimal point
        int postDecimalState = 3; // State for digits after the decimal point (required)
    
        // Initialize states - assume startState is the starting point and postDecimalState is the accepting state
        automaton.addState(startState, true, false);
        automaton.addState(preDecimalState, false, false);
        automaton.addState(decimalPointState, false, false);
        automaton.addState(postDecimalState, false, true); // This is an accepting state
    
        // Transitions for digits before the decimal point
        for (char c = '0'; c <= '9'; c++) {
            automaton.addTransition(startState, c, preDecimalState);
            automaton.addTransition(preDecimalState, c, preDecimalState); // Loop on itself for multiple digits
        }
    
        // Transition for the decimal point
        automaton.addTransition(preDecimalState, '.', decimalPointState);
        automaton.addTransition(startState, '.', decimalPointState); // Directly from start to decimal if no digits before
        
        // Transitions for digits after the decimal point
        for (char c = '0'; c <= '9'; c++) {
            automaton.addTransition(decimalPointState, c, postDecimalState);
            automaton.addTransition(postDecimalState, c, postDecimalState); // Loop on itself for multiple digits
        }
    
        return automaton;
    }
    
    private Automaton createAutomatonForPlus() {
        AutomatonImpl automaton = new AutomatonImpl();
        
        // Define states
        int startState = 0; // Start state
        int acceptState = 1; // Accept state
    
        // Initialize states
        automaton.addState(startState, true, false); // Start state, not accepting
        automaton.addState(acceptState, false, true); // Accept state
    
        // Transition for the '+' character
        automaton.addTransition(startState, '+', acceptState);
    
        return automaton;
    }
    
    private Automaton createAutomatonForMinus() {
        AutomatonImpl automaton = new AutomatonImpl();
        
        int startState = 0;
        int acceptState = 1;
    
        automaton.addState(startState, true, false);
        automaton.addState(acceptState, false, true);
    
        automaton.addTransition(startState, '-', acceptState);
    
        return automaton;
    }

    private Automaton createAutomatonForTimes() {
        AutomatonImpl automaton = new AutomatonImpl();
        
        int startState = 0;
        int acceptState = 1;
    
        automaton.addState(startState, true, false);
        automaton.addState(acceptState, false, true);
    
        automaton.addTransition(startState, '*', acceptState);
    
        return automaton;
    }

    private Automaton createAutomatonForDiv() {
        AutomatonImpl automaton = new AutomatonImpl();
        
        int startState = 0;
        int acceptState = 1;
    
        automaton.addState(startState, true, false);
        automaton.addState(acceptState, false, true);
    
        automaton.addTransition(startState, '/', acceptState);
    
        return automaton;
    }

    private Automaton createAutomatonForLParen() {
        AutomatonImpl automaton = new AutomatonImpl();
        
        int startState = 0;
        int acceptState = 1;
        
        automaton.addState(startState, true, false);
        automaton.addState(acceptState, false, true);
        
        automaton.addTransition(startState, '(', acceptState);
        
        return automaton;
    }

    private Automaton createAutomatonForRParen() {
        AutomatonImpl automaton = new AutomatonImpl();
        
        int startState = 0;
        int acceptState = 1;
        
        automaton.addState(startState, true, false);
        automaton.addState(acceptState, false, true);
        
        automaton.addTransition(startState, ')', acceptState);
        
        return automaton;
    }

    private Automaton createAutomatonForWhiteSpace() {
        AutomatonImpl automaton = new AutomatonImpl();
        
        int startState = 0; // Also the accept state in this case
        automaton.addState(startState, true, true); // Start state is also an accept state
        
        // Transitions for each whitespace character back to the same state
        automaton.addTransition(startState, ' ', startState);
        automaton.addTransition(startState, '\n', startState);
        automaton.addTransition(startState, '\r', startState);
        automaton.addTransition(startState, '\t', startState);
        
        return automaton;
    }
    
}

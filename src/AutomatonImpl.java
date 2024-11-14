import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {
        int state;
        char label;
        public StateLabelPair(int state_, char label_) { 
            state = state_; 
            label = label_; 
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, label);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateLabelPair that = (StateLabelPair) o;
            return state == that.state && label == that.label;
        }
    }

    HashSet<Integer> start_states;
    HashSet<Integer> accept_states;
    HashSet<Integer> current_states;
    HashMap<StateLabelPair, HashSet<Integer>> transitions;

    public AutomatonImpl() {
        start_states = new HashSet<>();
        accept_states = new HashSet<>();
        current_states = new HashSet<>();
        transitions = new HashMap<>();
    }

    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        if (is_start) {
            start_states.add(s);
        }
        if (is_accept) {
            accept_states.add(s);
        }
        // Ensure that every state has an entry in the transitions map.
        transitions.putIfAbsent(new StateLabelPair(s, '\0'), new HashSet<>()); // Use '\0' (null char) to initialize.
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        StateLabelPair key = new StateLabelPair(s_initial, label);
        transitions.computeIfAbsent(key, k -> new HashSet<>()).add(s_final);
    }

    @Override
    public void reset() {
        current_states.clear();
        current_states.addAll(start_states);
    }

    @Override
    public void apply(char input) {
        HashSet<Integer> nextStates = new HashSet<>();
        for (int currentState : current_states) {
            StateLabelPair key = new StateLabelPair(currentState, input);
            if (transitions.containsKey(key)) {
                nextStates.addAll(transitions.get(key));
            }
        }
        current_states = nextStates;
    }

    @Override
    public boolean accepts() {
        for (int state : current_states) {
            if (accept_states.contains(state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTransitions(char label) {
        for (StateLabelPair key : transitions.keySet()) {
            if (key.label == label && current_states.contains(key.state)) {
                return true;
            }
        }
        return false;
    }
}





// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.Objects;

// public class AutomatonImpl implements Automaton {

//     class StateLabelPair {
//         int state;
//         char label;
//         public StateLabelPair(int state_, char label_) { state = state_; label = label_; }

//         @Override
//         public int hashCode() {
//             return Objects.hash((Integer) state, (Character) label);
//         }

//         @Override
//         public boolean equals(Object o) {
//             StateLabelPair o1 = (StateLabelPair) o;
//             return (state == o1.state) && (label == o1.label);
//         }
//     }

//     HashSet<Integer> start_states;
//     HashSet<Integer> accept_states;
//     HashSet<Integer> current_states;
//     HashMap<StateLabelPair, HashSet<Integer>> transitions;

//     public AutomatonImpl() {
//         start_states = new HashSet<Integer>();
//         accept_states = new HashSet<Integer>();
//         transitions = new HashMap<StateLabelPair, HashSet<Integer>>();
//     }

//     @Override
//     public void addState(int s, boolean is_start, boolean is_accept) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'addState'");
//     }

//     @Override
//     public void addTransition(int s_initial, char label, int s_final) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'addTransition'");
//     }

//     @Override
//     public void reset() {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'reset'");
//     }

//     @Override
//     public void apply(char input) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'apply'");
//     }

//     @Override
//     public boolean accepts() {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'accepts'");
//     }

//     @Override
//     public boolean hasTransitions(char label) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'hasTransitions'");
//     }

    
// }

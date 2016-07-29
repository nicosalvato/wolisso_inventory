package et.wolisso_inventory.utils

/**
 * Finite State Machine used to control objects status
 */
class FiniteStateMachine {
    def transitions = [:]

    def initialState
    def currentState

    FiniteStateMachine(a_initialState) {
        assert a_initialState, "You need to provide an initial state"
        initialState = a_initialState
        currentState = a_initialState
    }

    def record() {
        Grammar.newInstance(this)
    }

    def reset() {
        currentState = initialState
    }

    def isState(a_state) {
        currentState == a_state
    }

    def registerTransition(a_grammar) {
        assert a_grammar.isValid(), "Invalid transition ($a_grammar)"
        def transition
        def event = a_grammar.event
        def fromState = a_grammar.fromState
        def toState = a_grammar.toState

        if (!transitions[event]) {
            transitions[event] = [:]
        }

        transition = transitions[event]
        assert !transition[fromState], "Duplicate fromState $fromState for transition $a_grammar"
        transition[fromState] = toState
    }

    def fire(a_event) {
        assert currentState, "Invalid current state '$currentState': passed into constructor"
        assert transitions.containsKey(a_event), "Invalid event '$a_event', should be one of ${transitions.keySet()}"
        def transition = transitions[a_event]
        def nextState = transition[currentState]
        assert nextState, "There is no transition from '$currentState' to any other state"
        currentState = nextState
        currentState
    }
}
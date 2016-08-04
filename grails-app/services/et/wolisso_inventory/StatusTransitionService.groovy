package et.wolisso_inventory

import grails.transaction.Transactional
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.enums.ItemStatusTransition
import et.wolisso_inventory.exceptions.IllegalTransitionException

@Transactional
class StatusTransitionService {

	def transitions = [
		(ItemStatusTransition.DECLARE_KO): [(ItemStatus.OK): ItemStatus.KO],
		(ItemStatusTransition.FIX): [(ItemStatus.KO): ItemStatus.FIXING],
		(ItemStatusTransition.RESTORE): [
			(ItemStatus.FIXING): ItemStatus.OK, 
			(ItemStatus.KO): ItemStatus.OK
		]
	]

    def fire(a_transition, status_from) {
    	if (!status_from)
    		throw new IllegalTransitionException("Invalid status $status_from, should be one of ${ItemStatus.values()}")
    	
    	if (!a_transition || !transitions[a_transition])
    		throw new IllegalTransitionException("Invalid transition $a_transition, should be one of ${ItemStatusTransition.values()}")
    	
    	def transition = transitions[a_transition]
    	if (!transition[status_from])
    		throw new IllegalTransitionException("No transition $a_transition available for initial state $status_from")

    	transition[status_from]
    }
}

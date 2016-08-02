package et.wolisso_inventory

import et.wolisso_inventory.Item
import et.wolisso_inventory.enums.ItemStatusTransition

class Report {

	Date dateCreated
	Date lastUpdated
	Item item
	String category
	ItemStatusTransition transition

    static constraints = {
    	category inList: ['OUT_OF_SERVICE', 'CONSUMABLE_MISSING', 'REPARING', 'REPAIRED']
    	transition nullable: true, validator: { val, obj -> 
    		if (obj.category != 'CONSUMABLE_MISSING' && !val) 
    			return ['invalid.transition']
    	}
    }
}

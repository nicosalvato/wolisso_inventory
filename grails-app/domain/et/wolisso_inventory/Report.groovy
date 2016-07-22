package et.wolisso_inventory

import et.wolisso_inventory.Item

class Report {

	Date dateCreated
	Date lastUpdated
	Item item
	String category

    static constraints = {
    	category inList: ['OUT_OF_SERVICE', 'CONSUMABLE_MISSING', 'REPARING', 'REPAIRED']
    }
}

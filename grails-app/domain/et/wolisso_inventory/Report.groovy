package et.wolisso_inventory

import et.wolisso_inventory.Item
import et.wolisso_inventory.enums.ItemStatusTransition

class Report {

    Item item
    String category
    String status = 'ISSUED'

	Date dateCreated
	Date lastUpdated
    
    static constraints = {
    	category inList: ['OK', 'FIXING', 'KO', 'CONSUMABLE_MISSING']
        status inList: ['ISSUED', 'CONFIRMED']
    }

    def confirm() {
        this.status = 'CONFIRMED'
        this
    }

    ItemStatusTransition getItemStatusTransition() {
        if (status == 'CONFIRMED') {
            switch (category) {
                case 'OK':
                    ItemStatusTransition.RESTORE
                    break
                case 'FIXING':
                    ItemStatusTransition.FIX
                    break
                case 'KO':
                    ItemStatusTransition.DECLARE_KO
                    break
                default:
                    null
            }
        }
    }
}

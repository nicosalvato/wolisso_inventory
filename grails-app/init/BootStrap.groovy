import et.wolisso_inventory.Item
import et.wolisso_inventory.Report

class BootStrap {

    def init = { servletContext ->
    	environments {
    		development {
    			(1..5).each {
		    		new Item(
		    			code: "CODE$it",
		    			externalCode: "externalCode$it",
		    			name: "Item $it",
		    			description: "Description for item $it"
		    		).save flush: true	
		    	}
                def categories = ['OUT_OF_SERVICE', 'CONSUMABLE_MISSING', 'REPARING', 'REPAIRED']
                (1..10).each {
                    new Report(
                        item: Item.load(it % 5 + 1),
                        category: categories[it % 4]
                    ).save flush: true
                }
    		}
    	}
    }
    def destroy = {
    }
}

import et.wolisso_inventory.Item
import et.wolisso_inventory.Report
import et.wolisso_inventory.Maintenance

class BootStrap {

    def init = { servletContext ->
    	environments {
    		development {
    			(1..5).each {
		    		new Item(
		    			code: "CODE$it",
		    			externalCode: "externalCode$it",
		    			name: "Item $it",
		    			description: "Description for item $it",
                        price: it % 3 * 250.0,
                        isDonation: it % 3 == 0,
                        deliveryDate: new Date(2016 - 1900 - it, it, 28 % it)
		    		).save flush: true	
		    	}
                def categories = ['OUT_OF_SERVICE', 'CONSUMABLE_MISSING', 'REPARING', 'REPAIRED']
                (1..10).each {
                    new Report(
                        item: Item.load(it % 5 + 1),
                        category: categories[it % 4]
                    ).save()

                    new Maintenance(
                        item: Item.load(it % 5 + 1),
                        cost: it * 230.0,
                        maintenanceDate: new Date(2016 - 1900, it, 28 % it)
                    ).save()
                }
    		}
            test {
                new Item(
                    code: "AAA",
                    name: "Aaa",
                    price: it * 100.0
                ).save flush: true  
            }
    	}
    }
    def destroy = {
    }
}

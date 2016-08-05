import et.wolisso_inventory.Item
import et.wolisso_inventory.Report
import et.wolisso_inventory.Maintenance
import et.wolisso_inventory.Manufacturer
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.enums.ItemStatusTransition

class BootStrap {
    def init = { servletContext ->
    	environments {
    		development {
                def manufacturer = new Manufacturer(country: 'China').save(flush: true)
    			(1..5).each {
		    		new Item(
		    			code: "CODE$it",
		    			externalCode: "externalCode$it",
		    			name: "Item $it",
		    			description: "Description for item $it",
                        price: it % 3 * 250.0,
                        isDonation: it % 3 == 0,
                        deliveryDate: new Date(2016 - 1900 - it, it, 28 % it),
                        status: ItemStatus.OK,
                        manufacturer: manufacturer
		    		).save flush: true	
		    	}
                def categories = ['KO', 'FIX', 'OK', 'CONSUMABLE_MISSING']
                (1..10).each {
                    new Report(
                        item: Item.load(it % 5 + 1),
                        category: categories[it % 4],
                        status: 'ISSUED'
                    ).save()

                    new Maintenance(
                        item: Item.load(it % 5 + 1),
                        cost: it * 230.0,
                        maintenanceDate: new Date(2016 - 1900, it, 28 % it)
                    ).save()
                }
    		}
            test {
                def manufacturer = new Manufacturer(country: 'China').save flush: true
                new Item(
                    code: "DDD",
                    name: "Ddd",
                    price: 100.0,
                    deliveryDate: new Date(),
                    manufacturer: manufacturer
                ).save flush: true
                
                new Item(
                    code: "EEE",
                    name: "Eee",
                    price: 120.0,
                    deliveryDate: new Date(),
                    status: ItemStatus.KO,
                    manufacturer: manufacturer
                ).save(flush: true)
            }
    	}
    }
    def destroy = {
    }
}

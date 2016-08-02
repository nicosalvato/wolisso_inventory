import et.wolisso_inventory.Item
import et.wolisso_inventory.Report
import et.wolisso_inventory.Maintenance
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.enums.ItemStatusTransition

class BootStrap {

    def itemFsm

    def init = { servletContext ->

        def recorder = itemFsm.record()
        recorder.on(ItemStatusTransition.DECLARE_KO).from(ItemStatus.OK).to(ItemStatus.KO)
        recorder.on(ItemStatusTransition.FIX).from(ItemStatus.KO).to(ItemStatus.FIXING)
        recorder.on(ItemStatusTransition.RESTORE).from(ItemStatus.FIXING).to(ItemStatus.OK)
        recorder.on(ItemStatusTransition.RESTORE).from(ItemStatus.KO).to(ItemStatus.OK)

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
                        deliveryDate: new Date(2016 - 1900 - it, it, 28 % it),
                        status: ItemStatus.OK
		    		).save flush: true	
		    	}
                def categories = ['OUT_OF_SERVICE', 'CONSUMABLE_MISSING', 'REPARING', 'REPAIRED']
                (1..10).each {
                    def category = categories[it % 4]
                    new Report(
                        item: Item.load(it % 5 + 1),
                        category: category,
                        transition: category == 'CONSUMABLE_MISSING' ? null : ItemStatusTransition.RESTORE
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
                    price: 100.0,
                    deliveryDate: new Date(),
                    status: ItemStatus.OK
                ).save flush: true  
            }
    	}
    }
    def destroy = {
    }
}

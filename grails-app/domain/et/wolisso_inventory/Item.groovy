package et.wolisso_inventory
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.enums.ItemStatusTransition

class Item {

    def itemFsm

	String code
	String externalCode
	String name
	String description
    BigDecimal price
    boolean isDonation = false
    ItemStatus status = ItemStatus.OK

    Date deliveryDate
	Date dateCreated
	Date lastUpdated

    static constraints = {
    	code nullable: false, blank: false, unique: true, maxSize: 64
    	externalCode nullable: true, blank: true, maxSize: 64
    	name nullable: false, blank: false, maxSize: 128
    	description nullable: true, blank: true, maxSize: 1024
        price min: 0.0
    }

    static transients = ['itemFSM']

    String toString() {
    	name.capitalize()
    }

    Integer maintenanceEpisodes() {
        Maintenance.countByItem(this)
    }

    BigDecimal maintenanceCost() {
        Maintenance.createCriteria().list() {
            eq 'item', this
            projections {
                sqlProjection 'sum(cost) as totalMaintenance', 'totalMaintenance', BIGDECIMAL
            }
        }?.totalMaintenance
    }

    Item fire(ItemStatusTransition event) {
        this.status = itemFsm.fire(event)
        this
    }

    Item resetStatus() {
        this.status = ItemStatus.OK
        itemFsm.reset()
        this
    }
}

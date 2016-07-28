package et.wolisso_inventory

class Item {

	String code
	String externalCode
	String name
	String description
    BigDecimal price
    boolean isDonation = false

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
}

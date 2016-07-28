package et.wolisso_inventory

class Item {

	String code
	String externalCode
	String name
	String description
    BigDecimal price
    boolean isDonated = false

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	code nullable: false, blank: false, unique: true
    	externalCode nullable: true, blank: true, maxSize: 512
    	name nullable: false, blank: false, maxSize: 512
    	description nullable: true, blank: true
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

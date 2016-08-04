package et.wolisso_inventory

class Maintenance {

	Date dateCreated
	Date lastUpdated
	Date maintenanceDate

	BigDecimal cost
	Item item
	boolean ordinary = false
	MaintenanceProvider provider
	String notes

    static constraints = {
    	cost min: 0.0
    	provider nullable: true
    	notes nullable: true, blank: true, maxSize: 1024
    }
}

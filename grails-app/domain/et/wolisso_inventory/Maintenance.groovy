package et.wolisso_inventory

class Maintenance {

	Date dateCreated
	Date lastUpdated
	Date maintenanceDate

	BigDecimal cost
	Item item

    static constraints = {
    	cost min: 0.0
    }
}

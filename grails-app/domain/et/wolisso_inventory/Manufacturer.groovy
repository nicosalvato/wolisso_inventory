package et.wolisso_inventory

class Manufacturer {

	String name
	String description
	String country

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	name nullable: true, blank: true, maxSize: 128
    	description nullable: true, blank: true, maxSize: 256
    }
}

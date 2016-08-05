package et.wolisso_inventory

class Location {

	String name
	String description

	Date dateCreated

    static constraints = {
    	name maxSize: 128, unique: true
    	description nullable: true, blank: true, maxSize: 256
    }
}

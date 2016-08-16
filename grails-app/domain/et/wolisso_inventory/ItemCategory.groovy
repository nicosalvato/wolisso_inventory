package et.wolisso_inventory

class ItemCategory {

	String name
	String description

	Date dateCreated

    static constraints = {
    	name maxSize: 64, unique: true
    	description nullable: true, blank: true, maxSize: 512
    }
}

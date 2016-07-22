package et.wolisso_inventory

class Item {

	String code
	String externalCode
	String name
	String description

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	code nullable: false, blank: false, unique: true
    	externalCode nullable: true, blank: true, maxSize: 512
    	name nullable: false, blank: false, maxSize: 512
    	description nullable: true, blank: true
    }

    String toString() {
    	name.capitalize()
    }
}

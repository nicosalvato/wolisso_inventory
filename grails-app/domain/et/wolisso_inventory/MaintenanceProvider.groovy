package et.wolisso_inventory

class MaintenanceProvider {

	String name
	String description
	String country
	String city
	String address
	String phone
	String cell
	String email

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	name maxSize: 128
    	description nullable: true, blank: true, maxSize: 256
    	city nullable: true, blank: true
    	address nullable: true, blank: true, maxSize: 256
    	phone nullable: true, blank: true, validator: { val, obj ->
			if(val && val != '' && !(val ==~ /^\+(?:[0-9] ?){6,14}[0-9]$/)) return ['invalid.format']
		}
    	cell nullable: true, blank: true, validator: { val, obj ->
			if(val && val != '' && !(val ==~ /^\+(?:[0-9] ?){6,14}[0-9]$/)) return ['invalid.format']
		}
    	email nullable: true, blank: true, email: true
    }
}

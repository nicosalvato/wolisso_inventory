import et.wolisso_inventory.Item

class BootStrap {

    def init = { servletContext ->
    	environments {
    		development {
    			(1..5).each {
		    		new Item(
		    			code: "CODE$it",
		    			externalCode: "externalCode$it",
		    			name: "Item $it",
		    			description: "Description for item $it"
		    		).save flush: true	
		    	}
    		}
    	}
    }
    def destroy = {
    }
}

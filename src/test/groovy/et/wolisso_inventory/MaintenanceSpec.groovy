package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Maintenance)
class MaintenanceSpec extends ConstraintUnitSpec {

	Item item
	
	def setup() {
    	item = new Item(code: 'ABC', name: 'Abc')
    }

	@Unroll("test Maintenance #error constraint for field #field")
    void "test constraints"() {
        when:
        def obj = new Maintenance("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error                  | field              | val
        'nullable'             | 'item'             | null
        'nullable'             | 'cost'             | null
        'min.notmet'           | 'cost'             | -1.2
    }

    void "test validation success"() {
    	expect: "validation successful"
    		new Maintenance(item: item, cost: 300).validate()
    }
}

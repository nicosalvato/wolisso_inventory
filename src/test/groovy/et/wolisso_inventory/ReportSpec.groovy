package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Report)
class ReportSpec extends ConstraintUnitSpec {

	Item item
	
	def setup() {
    	item = new Item(code: 'ABC', name: 'Abc', deliveryDate: new Date())
    }

	@Unroll("test Report #error constraint for field #field")
    void "test report constraints"() {
        when:
        def obj = new Report("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error                  | field              | val
        'nullable'             | 'item'             | null
        'nullable'             | 'category'         | null
        'not.inList'           | 'category'         | 'WRONG'
    }

    void "test validation success"() {
    	expect: "validation successful"
    		new Report(item: item, category: 'OUT_OF_SERVICE').validate()
    }
}

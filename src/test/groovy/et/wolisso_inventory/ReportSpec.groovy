package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Report)
class ReportSpec extends Specification {

	Report report
	Item item
    
    def setup() {
    	item = new Item(code: 'ABC', name: 'Abc')
    }

    def cleanup() {
    }

    void "test wrong validation"() {
    	when: "item is null"
    		report = new Report(category: 'OUT_OF_SERVICE')
		then: "report will not validate"
			assert !report.validate()

		when: "category is null"
    		report = new Report(item: item)
		then: "report will not validate"
			assert !report.validate()

		when: "category is not included in inList constraint options"
    		report = new Report(item: item, category: 'WRONG')
		then: "report will not validate"
			assert !report.validate()  

		when: "item is valid"
			report = new Report(item: item, category: 'OUT_OF_SERVICE') 
		then: "report will validate"
			assert report.validate()
    }
}

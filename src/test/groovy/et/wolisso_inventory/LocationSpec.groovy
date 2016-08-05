package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Location)
class LocationSpec extends ConstraintUnitSpec {

    @Unroll("Test Location #error constraint for field #field")
    void "Test Location constraints"() {
    	setup:
    	new Location(name: 'Test').save(flush: true)
    	/* About the flush: true joke, see http://stackoverflow.com/questions/30047904/unique-constraint-validation-now-passes-in-grails-after-migration-from-2-2-to-2, no comment!*/

        when:
        def obj = new Location("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error				| field			| val
        'nullable'			| 'name'		| null
        'unique'			| 'name'		| 'Test'
        'maxSize.exceeded'	| 'name'		| getLongString(129)
        'maxSize.exceeded'	| 'description'	| getLongString(257)
    }

    void "Test validation success"() {
    	expect:
    	new Location(name: 'Pediatric Ward').validate()
    }
}

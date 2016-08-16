package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ItemCategory)
class ItemCategorySpec extends ConstraintUnitSpec {

	@Unroll("Test ItemCategory #error constraint for field #field")
    void "test ItemCategory constraints"() {
    	setup:
    	new ItemCategory(name: 'Test').save(flush: true)
    	/* About the flush: true joke, see http://stackoverflow.com/questions/30047904/unique-constraint-validation-now-passes-in-grails-after-migration-from-2-2-to-2, no comment!*/

        when:
        def obj = new ItemCategory("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error				| field				| val
        'nullable'			| 'name'			| null
        'maxSize.exceeded'	| 'name'			| getLongString(65)
        'maxSize.exceeded'	| 'description'		| getLongString(513)
        'unique'			| 'name'			| 'Test'
    }

    void "Test successflu validation"() {
    	expect:
    	new ItemCategory(name: 'Yo', description: 'Nice').validate()
    }
}

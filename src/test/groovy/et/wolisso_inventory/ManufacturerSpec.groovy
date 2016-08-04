package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Manufacturer)
class ManufacturerSpec extends ConstraintUnitSpec {
    @Unroll("test Item #error constraint for field #field")
    void "test item constraints"() {
        when:
        def obj = new Manufacturer("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error                  | field              | val
        'nullable'             | 'country'          | null
        'maxSize.exceeded'     | 'name'             | getLongString(129)
        'maxSize.exceeded'     | 'description'      | getLongString(257)
    }

    void "test validation success"() {
        expect: "validation successful"
            new Manufacturer(name: 'Il Forgiatore', 
                country: 'Italy', 
                description: 'Roba buona').validate()
    }
}

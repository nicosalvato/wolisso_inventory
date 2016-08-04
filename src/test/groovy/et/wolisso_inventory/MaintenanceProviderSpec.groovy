package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(MaintenanceProvider)
class MaintenanceProviderSpec extends ConstraintUnitSpec {
    @Unroll("test MaintenanceProvider #error constraint for field #field")
    void "test maintenance provider constraints"() {
        when:
        def obj = new MaintenanceProvider("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error                  | field              | val
        'nullable'             | 'name'             | null
        'nullable'             | 'country'          | null
        'invalid.format'       | 'phone'     		| 'paperino'
        'invalid.format'       | 'cell'             | '12hjk4h213'
        'email.invalid'        | 'email'            | 'canemorto'
        'maxSize.exceeded'     | 'name'             | getLongString(129)
        'maxSize.exceeded'     | 'description'      | getLongString(257)
        'maxSize.exceeded'     | 'address'          | getLongString(257)
    }

    void "test validation success"() {
        expect: "validation successful"
            new MaintenanceProvider(name: 'Il Ciappinaro', country: 'Italy').validate()
    }
}

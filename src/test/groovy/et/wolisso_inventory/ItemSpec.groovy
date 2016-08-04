package et.wolisso_inventory

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import et.wolisso_inventory.enums.ItemStatus

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item)
@Mock(Manufacturer)
class ItemSpec extends ConstraintUnitSpec {

    def manufacturer

    void setup() {
        manufacturer = new Manufacturer(country: 'China')
    }

    @Unroll("test Item #error constraint for field #field")
    void "test item constraints"() {
        setup:
        // Item used to test code unique constraint
        new Item(code: 'CODE', 
            name: 'Eh', 
            price: 1.2, 
            deliveryDate: new Date(), 
            status: ItemStatus.OK,
            manufacturer: manufacturer).save(flush: true)
        /* See http://stackoverflow.com/questions/30047904/unique-constraint-validation-now-passes-in-grails-after-migration-from-2-2-to-2, no comment!*/

        when:
        def obj = new Item("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error                  | field              | val
        'nullable'             | 'code'             | null
        'nullable'             | 'name'             | null
        'nullable'             | 'price'            | null
        'nullable'             | 'deliveryDate'     | null
        'nullable'             | 'status'           | null
        'nullable'             | 'manufacturer'     | null
        'unique'               | 'code'             | 'CODE'
        'maxSize.exceeded'     | 'code'             | getLongString(65)
        'maxSize.exceeded'     | 'name'             | getLongString(129)
        'maxSize.exceeded'     | 'externalCode'     | getLongString(65)
        'min.notmet'           | 'price'            | -1.2
    }

    void "test validation success"() {
        expect: "validation successful"
            new Item(code: 'CC', 
                name: 'Cc', 
                price: 1.2, 
                deliveryDate: new Date(), 
                status: ItemStatus.OK, 
                manufacturer: manufacturer).validate()
    }
}

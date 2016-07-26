package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item)
class ItemSpec extends Specification {

    Item item

    def setup() {
    }

    def cleanup() {
    }

    void "test null code"() {
        when: "item is empty"
        item = new Item()

        then: "shouldn't validate"
        !item.validate()

        when: "code is null"
        item = new Item(name: "Bau")

        then: "shouldn't validate"
        !item.validate()

        when: "name is null"
        item = new Item(code: 'BAU')

        then: "shouldn't validate"
        !item.validate()
    }
}

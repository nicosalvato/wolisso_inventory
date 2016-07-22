package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item)
class ItemSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test null code"() {
        when: "code is null"
        def item = new Item()

        then: "shouldn't validate"
        !item.validate()
    }
}

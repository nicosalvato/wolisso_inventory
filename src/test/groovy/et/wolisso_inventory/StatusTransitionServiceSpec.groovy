package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.enums.ItemStatusTransition

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(StatusTransitionService)
class StatusTransitionServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    @Unroll("test transition #transition from status #statusFrom")
    void "test transitions"() {
        expect:
        statusTo == service.fire(transition, statusFrom)

        where:
        transition                  	| statusFrom         | statusTo
        ItemStatusTransition.DECLARE_KO | ItemStatus.OK      | ItemStatus.KO
        ItemStatusTransition.FIX        | ItemStatus.KO      | ItemStatus.FIXING
        ItemStatusTransition.RESTORE    | ItemStatus.KO      | ItemStatus.OK
        ItemStatusTransition.RESTORE    | ItemStatus.FIXING  | ItemStatus.OK
    }
}

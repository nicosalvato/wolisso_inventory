package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import et.wolisso_inventory.enums.ItemStatusTransition

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
        error        | field      | val
        'nullable'   | 'item'     | null
        'nullable'   | 'category' | null
        'nullable'   | 'status'   | null
        'not.inList' | 'category' | 'WRONG'
        'not.inList' | 'status'   | 'WRONG'
    }

    @Unroll("test getItemStatusTransition method when status is #status and category is #category")
    void "test getItemStatusTransition"() {
        when:
        def obj = new Report(status: status, category: category)

        then:
        transition == obj.itemStatusTransition

        where:
        transition                      | status      | category
        null                            | 'ISSUED'    | 'KO'
        ItemStatusTransition.DECLARE_KO | 'CONFIRMED' | 'KO'
        ItemStatusTransition.FIX        | 'CONFIRMED' | 'FIXING'
        ItemStatusTransition.RESTORE    | 'CONFIRMED' | 'OK'
        null                            | 'CONFIRMED' | 'CONCUMABLE_MISSING'
    }

    void "Test confirm method"() {
        expect: "Report status to change to CONFIRMED"
        new Report(status: 'ISSUED').confirm().status == 'CONFIRMED'
    }

    void "test validation success"() {
        expect: "validation successful"
        new Report(item: item, category: 'KO').validate()
        new Report(item: item, category: 'CONSUMABLE_MISSING', status: 'CONFIRMED').validate()
    }
}

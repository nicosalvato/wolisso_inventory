package et.wolisso_inventory

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

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
        'not.inList' | 'category' | 'WRONG'
    }

    @Unroll("test Report transition constraint for category #val")
    void "test report constraints"() {
        when:
        def obj = new Report("$field": null, category: val)

        then:
        validateConstraints(obj, field, error)

        where:
        error                  | field        | val
        'invalid.transition'   | 'transition' | 'OUT_OF_SERVICE'
        'invalid.transition'   | 'transition' | 'REPARING'
        'invalid.transition'   | 'transition' | 'REPAIRED'
    }

    void "test validation success"() {
        expect: "validation successful"
        new Report(item: item, category: 'OUT_OF_SERVICE', transition: 'FIX').validate()
    }
}

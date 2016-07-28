import spock.lang.Specification

abstract class ConstraintUnitSpec extends Specification {
    String getLongString(Integer length) {
       'a' * length
   }

   void validateConstraints(obj, field, error) {
        def validated = obj.validate()
        if (error && error != 'valid') {
            assert !validated
            assert obj.errors[field]
            assert error == obj.errors[field].code
        } else {
            assert !obj.errors[field]
        }
    }
}
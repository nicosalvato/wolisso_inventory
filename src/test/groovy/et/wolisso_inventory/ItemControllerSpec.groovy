package et.wolisso_inventory

import grails.test.mixin.*
import spock.lang.*
import static org.springframework.http.HttpStatus.*
import et.wolisso_inventory.enums.ItemStatus

@TestFor(ItemController)
@Mock([Item, Manufacturer])
class ItemControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params["code"] = 'TC'
        params["externalCode"] = 'TC'
        params["name"] = 'Test Code'
        params["description"] = 'Test description'
        params['price'] = 100.0
        params['deliveryDate'] = new Date()
        params['status'] = ItemStatus.OK
        params['manufacturer'] = new Manufacturer(country: 'India')
    }

    void "Test the index action returns the correct response"() {

        when:"The index action is executed"
            controller.index()

        then:"The response is correct"
            response.text == '[]'
    }


    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'
            def item = new Item()
            item.validate()
            controller.save(item)

        then:"The create view is rendered again with the correct model"
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors

        when:"The save action is executed with a valid instance"
            def initialCount = Item.count()
            response.reset()
            populateValidParams(params)
            item = new Item(params)

            controller.save(item)

        then:"A redirect is issued to the show action"
            Item.count() == initialCount + 1
            response.status == CREATED.value()
            response.json  

        when: "An instance with duplicated code is saved"
            response.reset()
            populateValidParams(params)
            item = new Item(params)
            controller.save()

        then: "The create view is rendered again"
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors          
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            response.reset()
            def item= new Item(params).save()
            controller.show(item)

        then:"A model is populated containing the domain instance"
            item!= null
            response.status == OK.value()
            response.json
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.status == NOT_FOUND.value()

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def item= new Item()
            item.validate()
            controller.update(item)

        then:"The edit view is rendered again with the invalid instance"
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            item= new Item(params).save(flush: true)
            controller.update(item)

        then:"A redirect is issued to the show action"
            item!= null
            response.status == OK.value()
            response.json.id == item.id
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.status == NOT_FOUND.value()


        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def item= new Item(params).save(flush: true)

        then:"It exists"
            Item.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(item)

        then:"The instance is deleted"
            Item.count() == 0
            response.status == NO_CONTENT.value()
    }
}
package et.wolisso_inventory

import grails.test.mixin.integration.Integration
import grails.transaction.*
import static grails.web.http.HttpHeaders.*
import static org.springframework.http.HttpStatus.*
import spock.lang.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder
import et.wolisso_inventory.enums.ItemStatusTransition
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.exceptions.IllegalTransitionException

@Integration
@Rollback
class ItemFunctionalSpec extends GebSpec {

    RestBuilder getRestBuilder() {
        new RestBuilder()
    }

    String getResourcePath() {
        "${baseUrl}/items"
    }

    Closure getValidJson(aCode) {{ ->
        [
            code: "$aCode",
            name: "Test",
            price: 234.6,
            deliveryDate: new Date(),
            status: "OK",
            manufacturer: Manufacturer.load(1)
        ]
    }}

    Closure getInvalidJson() {{->        
        [
            code: null
        ]
    }}    

    void "Test the index action"() {
        when:"The index action is requested"
        def response = restBuilder.get(resourcePath)

        then:"The response is correct"
        response.status == OK.value()
    }

    void "Test the save action correctly persists an instance"() {
        when:"The save action is executed with no content"
        def response = restBuilder.post(resourcePath)

        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value()
        
        when:"The save action is executed with invalid data"
        response = restBuilder.post(resourcePath) {
            json invalidJson
        }           
        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value()


        when:"The save action is executed with valid data"
        def initialCount = Item.count()
        response = restBuilder.post(resourcePath) {
            json getValidJson('CODE01')
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        response.json.id
        Item.count() == initialCount + 1
    }

    void "Test the update action correctly updates an instance"() {
        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json getValidJson('CODE02')
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        response.json.id

        when:"The update action is called with invalid data"
        def id = response.json.id
        def responseJson = response.json
        response = restBuilder.put("$resourcePath/$id") {
            json invalidJson
        }  

        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value()

        when:"The update action is called with valid data"
        responseJson.code = 'UPDATED-CODE'
        response = restBuilder.put("$resourcePath/$id") {
            responseJson
        }  

        then:"The response is correct"
        response.status == OK.value()        
        response.json

    }    

    void "Test the show action correctly renders an instance"() {
        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json getValidJson('CODE03')
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        response.json.id

        when:"When the show action is called to retrieve a resource"
        def id = response.json.id
        response = restBuilder.get("$resourcePath/$id") 

        then:"The response is correct"
        response.status == OK.value()
        response.json.id == id  
    }  

    void "Test the delete action correctly deletes an instance"() {
        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json getValidJson('CODE04')
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        response.json.id

        when:"When the delete action is executed on an unknown instance"
        def id = response.json.id
        response = restBuilder.delete("$resourcePath/99999") 

        then:"The response is correct"
        response.status == NOT_FOUND.value()
        
        when:"When the delete action is executed on an existing instance"
        response = restBuilder.delete("$resourcePath/$id") 

        then:"The response is correct"
        response.status == NO_CONTENT.value()        
        !Item.get(id)
    }

    void "Test status transitions"() {
        when: "item staus is OK"
        def item = new Item(code: "FSM", name:"FSM test")

        then: "then KO-FIXING-OK cycle works"
        item.fire(ItemStatusTransition.DECLARE_KO).status == ItemStatus.KO
        item.fire(ItemStatusTransition.FIX).status == ItemStatus.FIXING
        item.fire(ItemStatusTransition.RESTORE).status == ItemStatus.OK

        when: "item status is KO"
        item.status = ItemStatus.KO

        then: "it can go back to ok"
        item.fire(ItemStatusTransition.RESTORE).status == ItemStatus.OK

        when: "item wrong ransition is called"
        item.status = ItemStatus.OK
        item.fire(ItemStatusTransition.RESTORE)

        then: "exception is thrown"
        IllegalTransitionException ex = thrown()
        ex.message == 'No transition RESTORE available for initial state OK'
    }
}
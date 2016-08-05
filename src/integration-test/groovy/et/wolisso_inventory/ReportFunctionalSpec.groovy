package et.wolisso_inventory

import grails.test.mixin.integration.Integration
import grails.transaction.*
import static grails.web.http.HttpHeaders.*
import static org.springframework.http.HttpStatus.*
import spock.lang.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.enums.ItemStatusTransition

@Integration
@Rollback
class ReportFunctionalSpec extends GebSpec {

    RestBuilder getRestBuilder() {
        new RestBuilder()
    }

    String getResourcePath() {
        "${baseUrl}/reports"
    }

    Closure getValidJson(String category = null, String status = null) {{->
        [
            item: Item.load(1),
            category: category ?: 'CONSUMABLE_MISSING',
            status: status ?: 'ISSUED'
        ]
    }}

    Closure getInvalidJson() {{->        
        [
            category: 'INVALID'
        ]
    }}    

    void "Test the index action"() {
        when:"The index action is requested"
        def response = restBuilder.get(resourcePath)

        then:"The response is correct"
        response.status == OK.value()
        response.json == []
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
        response = restBuilder.post(resourcePath) {
            json validJson
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        response.json.id
        Report.count() == 1
    }

    void "Test the update action correctly updates an instance"() {
        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json validJson
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        response.json.id

        when:"The update action is called with invalid data"
        def id = response.json.id
        response = restBuilder.put("$resourcePath/$id") {
            json invalidJson
        }  

        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value()

        when:"The update action is called with valid data"
        response = restBuilder.put("$resourcePath/$id") {
            json validJson
        }  

        then:"The response is correct"
        response.status == OK.value()        
        response.json

    }    

    void "Test the show action correctly renders an instance"() {
        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json validJson
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
            json validJson
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
        !Report.get(id)
    }  

    void "Test whether a confirmed report issues correct item status transition"() {
        // KO
        when: "A report with KO category is issued"
        def response = restBuilder.post(resourcePath) {
            json getValidJson('KO', 'ISSUED')
        }
        
        then: "Item status should not change"
        response.json.item.status == ItemStatus.OK as String
        
        when: "A report with KO category is confirmed"
        def id = response.json.id
        response = restBuilder.put("$resourcePath/$id") {
            json getValidJson('KO', 'CONFIRMED')
        }

        then: "Item status should change to KO"
        response.json.item.status == ItemStatus.KO as String

        // FIXING
        when: "A report with FIXING category is issued"
        response = restBuilder.post(resourcePath) {
            json getValidJson('FIXING', 'ISSUED')
        }
        
        then: "Item status should not change"
        response.json.item.status == ItemStatus.KO as String
        
        when: "A report with FIXING category is confirmed"
        id = response.json.id
        response = restBuilder.put("$resourcePath/$id") {
            json getValidJson('FIXING', 'CONFIRMED')
        }

        then: "Item status should change to FIX"
        response.json.item.status == ItemStatus.FIXING as String

        // OK
        when: "A report with OK category is issued"
        response = restBuilder.post(resourcePath) {
            json getValidJson('OK', 'ISSUED')
        }
        
        then: "Item status should not change"
        response.json.item.status == ItemStatus.FIXING as String
        
        when: "A report with OK category is confirmed"
        id = response.json.id
        response = restBuilder.put("$resourcePath/$id") {
            json getValidJson('OK', 'CONFIRMED')
        }

        then: "Item status should change to OK"
        response.json.item.status == ItemStatus.OK as String
    }  
}
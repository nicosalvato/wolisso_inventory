package et.wolisso_inventory

import grails.test.mixin.integration.Integration
import grails.transaction.*
import static grails.web.http.HttpHeaders.*
import static org.springframework.http.HttpStatus.*
import spock.lang.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder

@Integration
@Rollback
class ItemCategoryFunctionalSpec extends GebSpec {

    RestBuilder getRestBuilder() {
        new RestBuilder()
    }

    String getResourcePath() {
        "${baseUrl}/itemcategories"
    }

    Closure getValidJson(String name = null) {{->
        [
            name: name ?: 'Scyalitic lamp',
            description: 'Some very nice description'
        ]
    }}

    Closure getInvalidJson() {{->        
        [ name : null ]
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
        ItemCategory.count() == 1
    }

    void "Test the update action correctly updates an instance"() {
        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json getValidJson('Cat 1')
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
            json getValidJson('Cat 12')
        }  

        then:"The response is correct"
        response.status == OK.value()        
        response.json

    }    

    void "Test the show action correctly renders an instance"() {
        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json getValidJson('Cat 2')
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
            json getValidJson('Cat 3')
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
        !ItemCategory.get(id)
    }    
}
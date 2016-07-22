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
class ItemFunctionalSpec extends GebSpec {

    int cnt = 1

    RestBuilder getRestBuilder() {
        new RestBuilder()
    }

    String getResourcePath() {
        "${baseUrl}/items"
    }

    Closure getValidJson(String aCode) {{ ->
        [
            code: "FTESTCODE${aCode}",
            externalCode: "FTESTEXTCODE-${aCode}",
            name: "Ftest Name ${aCode}",
            description: "Ftest description ${aCode}"
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
            json getValidJson('CODE01')
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        response.json.id
        Item.count() == 1
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
}
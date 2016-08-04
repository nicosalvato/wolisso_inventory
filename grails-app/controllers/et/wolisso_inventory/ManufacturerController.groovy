package et.wolisso_inventory

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ManufacturerController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Manufacturer.list(params), model:[manufacturerCount: Manufacturer.count()]
    }

    def show(Manufacturer manufacturer) {
        respond manufacturer
    }

    @Transactional
    def save(Manufacturer manufacturer) {
        if (manufacturer == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (manufacturer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond manufacturer.errors, view:'create'
            return
        }

        manufacturer.save flush:true

        respond manufacturer, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Manufacturer manufacturer) {
        if (manufacturer == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (manufacturer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond manufacturer.errors, view:'edit'
            return
        }

        manufacturer.save flush:true

        respond manufacturer, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Manufacturer manufacturer) {

        if (manufacturer == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        manufacturer.delete flush:true

        render status: NO_CONTENT
    }
}

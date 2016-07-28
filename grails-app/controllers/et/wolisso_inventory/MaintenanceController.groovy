package et.wolisso_inventory

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MaintenanceController {

    static responseFormats = ['json', 'xml', 'hal']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Maintenance.list(params), model:[maintenanceCount: Maintenance.count()]
    }

    def show(Maintenance maintenance) {
        respond maintenance
    }

    @Transactional
    def save(Maintenance maintenance) {
        if (maintenance == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (maintenance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond maintenance.errors, view:'create'
            return
        }

        maintenance.save flush:true

        respond maintenance, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Maintenance maintenance) {
        if (maintenance == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (maintenance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond maintenance.errors, view:'edit'
            return
        }

        maintenance.save flush:true

        respond maintenance, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Maintenance maintenance) {

        if (maintenance == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        maintenance.delete flush:true

        render status: NO_CONTENT
    }
}

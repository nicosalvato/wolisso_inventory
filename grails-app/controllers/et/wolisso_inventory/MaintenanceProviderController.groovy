package et.wolisso_inventory

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MaintenanceProviderController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond MaintenanceProvider.list(params), model:[maintenanceProviderCount: MaintenanceProvider.count()]
    }

    def show(MaintenanceProvider maintenanceProvider) {
        respond maintenanceProvider
    }

    @Transactional
    def save(MaintenanceProvider maintenanceProvider) {
        if (maintenanceProvider == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (maintenanceProvider.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond maintenanceProvider.errors, view:'create'
            return
        }

        maintenanceProvider.save flush:true

        respond maintenanceProvider, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(MaintenanceProvider maintenanceProvider) {
        if (maintenanceProvider == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (maintenanceProvider.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond maintenanceProvider.errors, view:'edit'
            return
        }

        maintenanceProvider.save flush:true

        respond maintenanceProvider, [status: OK, view:"show"]
    }

    @Transactional
    def delete(MaintenanceProvider maintenanceProvider) {

        if (maintenanceProvider == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        maintenanceProvider.delete flush:true

        render status: NO_CONTENT
    }
}

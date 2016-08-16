package et.wolisso_inventory

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ItemCategoryController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ItemCategory.list(params), model:[itemCategoryCount: ItemCategory.count()]
    }

    def show(ItemCategory itemCategory) {
        respond itemCategory
    }

    @Transactional
    def save(ItemCategory itemCategory) {
        if (itemCategory == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (itemCategory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond itemCategory.errors, view:'create'
            return
        }

        itemCategory.save flush:true

        respond itemCategory, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(ItemCategory itemCategory) {
        if (itemCategory == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (itemCategory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond itemCategory.errors, view:'edit'
            return
        }

        itemCategory.save flush:true

        respond itemCategory, [status: OK, view:"show"]
    }

    @Transactional
    def delete(ItemCategory itemCategory) {

        if (itemCategory == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        itemCategory.delete flush:true

        render status: NO_CONTENT
    }
}

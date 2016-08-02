package et.wolisso_inventory

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ReportController {

    static responseFormats = ['json', 'xml', 'hal']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'lastUpdated'
        params.order = params.order ?: 'desc'
        def reports = Report.createCriteria().list(params) {
            item {
                if (params.itemCode)
                    like 'code', "${params.itemCode}%"
                }
        }
        respond reports, model:[reportCount: reports.totalCount]
    }

    def show(Report report) {
        respond report
    }

    @Transactional
    def save(Report report) {
        if (report == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (report.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond report.errors, view:'create'
            return
        }

        if (report.transition)
            report.item.fire(report.transition).save flush: true
        report.save flush:true

        respond report, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Report report) {
        if (report == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (report.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond report.errors, view:'edit'
            return
        }

        report.save flush:true

        respond report, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Report report) {

        if (report == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        report.delete flush:true

        render status: NO_CONTENT
    }
}

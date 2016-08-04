// Place your Spring DSL code here
import grails.rest.render.hal.*
import et.wolisso_inventory.Item
import et.wolisso_inventory.Report
import et.wolisso_inventory.Maintenance
import et.wolisso_inventory.enums.ItemStatus

beans = {
	// HAL renderers, they kick in when Accept header in the request is set to json+hal
    halItemRenderer(HalJsonRenderer, et.wolisso_inventory.Item)
    halItemCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Item)
	halReportRenderer(HalJsonRenderer, et.wolisso_inventory.Report)
    halReportCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Report)
	halMaintenanceRenderer(HalJsonRenderer, et.wolisso_inventory.Maintenance)
    halMaintenanceCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Maintenance)
}

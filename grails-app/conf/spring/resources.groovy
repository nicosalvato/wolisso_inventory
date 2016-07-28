// Place your Spring DSL code here
import grails.rest.render.hal.*
import et.wolisso_inventory.Item
import et.wolisso_inventory.Report
import et.wolisso_inventory.Maintenance

beans = {
    halItemRenderer(HalJsonRenderer, et.wolisso_inventory.Item)
    halItemCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Item)
	halReportRenderer(HalJsonRenderer, et.wolisso_inventory.Report)
    halReportCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Report)
	halMaintenanceRenderer(HalJsonRenderer, et.wolisso_inventory.Maintenance)
    halMaintenanceCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Maintenance)
}

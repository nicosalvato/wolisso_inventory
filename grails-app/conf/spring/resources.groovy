// Place your Spring DSL code here
import grails.rest.render.hal.*
import et.wolisso_inventory.Item
import et.wolisso_inventory.Report
import et.wolisso_inventory.Maintenance
import et.wolisso_inventory.MaintenanceProvider
import et.wolisso_inventory.Manufacturer
import et.wolisso_inventory.Location
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.cors.CorsFilter

beans = {
	// HAL renderers, they kick in when 'Accept' header in the request is set to 'application/json+hal'
    halItemRenderer(HalJsonRenderer, et.wolisso_inventory.Item)
    halItemCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Item)
	halReportRenderer(HalJsonRenderer, et.wolisso_inventory.Report)
    halReportCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Report)
	halMaintenanceRenderer(HalJsonRenderer, et.wolisso_inventory.Maintenance)
    halMaintenanceCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Maintenance)
    halMaintenanceProviderRenderer(HalJsonRenderer, et.wolisso_inventory.MaintenanceProvider)
    halMaintenanceProviderCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.MaintenanceProvider)
    halManufacturerRenderer(HalJsonRenderer, et.wolisso_inventory.MaintenanceProvider)
    halManufacturerCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.MaintenanceProvider)
    halLocationRenderer(HalJsonRenderer, et.wolisso_inventory.Location)
    halLocationCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Location)
    // CORS
    corsFilter(CorsFilter)
}

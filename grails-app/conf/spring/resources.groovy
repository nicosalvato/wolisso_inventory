// Place your Spring DSL code here
import grails.rest.render.hal.*
import et.wolisso_inventory.Item
import et.wolisso_inventory.Report
import et.wolisso_inventory.Maintenance
import et.wolisso_inventory.enums.ItemStatus
import et.wolisso_inventory.enums.ItemStatusTransition
import et.wolisso_inventory.utils.FiniteStateMachine

beans = {
	// HAL renderers, they kick in when Accept header in the request is set to json+hal
    halItemRenderer(HalJsonRenderer, et.wolisso_inventory.Item)
    halItemCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Item)
	halReportRenderer(HalJsonRenderer, et.wolisso_inventory.Report)
    halReportCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Report)
	halMaintenanceRenderer(HalJsonRenderer, et.wolisso_inventory.Maintenance)
    halMaintenanceCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Maintenance)

    // Finite State Machine initialization for Item status
    def fsm = FiniteStateMachine.newInstance(ItemStatus.OK)
    def recorder = fsm.record()
    recorder.on(ItemStatusTransition.DECLARE_KO).from(ItemStatus.OK).to(ItemStatus.KO)
    recorder.on(ItemStatusTransition.FIX).from(ItemStatus.KO).to(ItemStatus.FIXING)
    recorder.on(ItemStatusTransition.RESTORE).from(ItemStatus.FIXING).to(ItemStatus.OK)
    recorder.on(ItemStatusTransition.RESTORE).from(ItemStatus.KO).to(ItemStatus.OK)
    itemFSM(fsm)
}

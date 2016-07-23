// Place your Spring DSL code here
import grails.rest.render.hal.*
import et.wolisso_inventory.Item

beans = {
    halItemRenderer(HalJsonRenderer, et.wolisso_inventory.Item)
    halItemCollectionRenderer(HalJsonCollectionRenderer, et.wolisso_inventory.Item)
}

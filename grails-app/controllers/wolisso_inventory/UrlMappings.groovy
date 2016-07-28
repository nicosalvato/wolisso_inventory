package wolisso_inventory

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'application', action:'index')
        "/items"(resources: "item")
        "/reports"(resources: "report")
        "/maintenance"(resources: "maintenance")
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}

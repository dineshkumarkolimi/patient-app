package patient.app

class UrlMappings {
    static mappings = {
        "/patient"(controller: 'patient') {
            action = [GET: "show", POST: "save", PATCH: "update", DELETE: "delete"]
        }

        "/patient/edit"(controller: 'patient', action: 'edit')

        "500"(view:'/error')
        "404"(view:'/notFound')

    }
}

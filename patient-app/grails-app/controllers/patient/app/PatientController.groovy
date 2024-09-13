package patient.app

import com.app.Patient
import com.app.Attendant
import com.app.Team
import grails.validation.ValidationException
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import grails.gorm.transactions.Transactional

@Slf4j
class PatientController {
    @Transactional
    def save() {
        try {
            def unassignedAttendants = Attendant.executeQuery("""select a from Attendant a where a.id not in (select p.attendant.id from Patient p where p.attendant is not null)""")
            if (unassignedAttendants) {
                def attendant = unassignedAttendants.first()
                def team = attendant.team

                if (!attendant || !team) {
                    log.error("Attendant or Team not found")
                    respond([error: 'Attendant or Team not found'], [formats: ['json'], status: HttpStatus.NOT_FOUND])
                    return
                }
                params.attendant = attendant
                params.team = team
                params.created_at = new Date()
                params.updated_at = new Date()
                def patient = new Patient(params)
                if (patient.save(flush: true)) {
                    respond patient, [formats: ['json'], status: HttpStatus.CREATED]
                } else {
                    respond(patient.errors, [formats: ['json'], status: HttpStatus.BAD_REQUEST])
                }
            } else {
                log.error("Attendant is not available")
                respond([error: 'Attendants are not available'], [formats: ['json'], status: HttpStatus.NOT_FOUND])
            }
        } catch (ValidationException e) {
            log.error("Validation failed: ${e.message}")
            respond([error: "Invalid data provided"], [formats: ['json'], status: HttpStatus.BAD_REQUEST])
        }
    }

    def show() {
        def patients = Patient.list()
        if (patients) {
            respond patients, [formats: ['json'], status: HttpStatus.OK]
        } else {
            log.error("No patients found to display")
            respond([error: "No patients found to display"], [formats: ['json'], status: HttpStatus.NOT_FOUND])
        }
    }

    @Transactional
    def update() {
        def patient_id = params.id?.toLong()  // Fetch the 'id' from params and convert to Long
        if (!patient_id) {
            respond([error: "Patient ID is missing!"], [formats: ['json'], status: HttpStatus.BAD_REQUEST])
            return
        }
        def patient = Patient.get(patient_id)
        if (!patient) {
            respond([error: "Patient not found!"], [formats: ['json'], status: HttpStatus.NOT_FOUND])
            return
        }
        def jsonBody = request.JSON
        patient.properties = jsonBody
        patient.updated_at = new Date()
        try {
            if (patient.save(flush: true)) {
                respond patient.list(), [formats: ['json'], status: HttpStatus.OK]
            } else {
                respond(patient.errors, [formats: ['json'], status: HttpStatus.BAD_REQUEST])
            }
        } catch (ValidationException e) {
            log.error("Validation Failed: ${e.message}")
            respond([error: "Invalid data provided"], [formats: ['json'], status: HttpStatus.BAD_REQUEST])
        }
    }

    @Transactional
    def delete() {
        try {
            def patient_id = params.id?.toLong()  // Fetch the 'id' from params and convert to Long
            if (!patient_id) {
                respond([error: "Patient ID is missing!"], [formats: ['json'], status: HttpStatus.BAD_REQUEST])
                return
            }
            def patient = Patient.get(patient_id)
            if (!patient) {
                log.warn("Patient with id ${id} not found")
                respond([error: 'Patient not found'], [formats: ['json'], status: HttpStatus.NOT_FOUND])
                return
            }
            patient.delete(flush: true)
            log.info("Deleted patient with id ${id}")
            respond patient.list(), [formats: ['json'], status: HttpStatus.OK]
        } catch (Exception e) {
            log.error("Exception occurred while deleting patient", e)
            respond([error: 'Internal Server Error'], [formats: ['json'], status: HttpStatus.INTERNAL_SERVER_ERROR])
        }
    }

    def edit() {
        def unassignedAttendants = Attendant.executeQuery("""select a from Attendant a where a.id not in (select p.attendant.id from Patient p where p.attendant is not null)""")
        def response = unassignedAttendants.collect { attendant ->
            [
                id: attendant.id,
                name: attendant.name,
                teamId: attendant.team?.id,
                teamName: attendant.team?.name
            ]
        }
        if (response) {
            respond response, [formats: ['json'], status: HttpStatus.OK]
        } else {
            log.error("Attendant/Team is not available to change")
            respond([error: 'Attendant or Team not available'], [formats: ['json'], status: HttpStatus.NO_CONTENT])
        }
    }
}

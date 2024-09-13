package com.app

class Attendant {

    String name
    Date created_at
    Date updated_at

    static hasOne = [patient: Patient]
    static belongsTo = [team: Team]

    static constraints = {
        name nullable:false, blank:false
        team nullable:false
        created_at nullable:false
        updated_at nullable:false
    }

    static mapping = {
        created_at defaultValue: new Date()
        updated_at defaultValue: new Date()
        patient column: 'patient_id'
    }
}
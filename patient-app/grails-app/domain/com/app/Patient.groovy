package com.app

class Patient {
    
    String first_name
    String last_name
    String mobile
    Boolean gender
    String email
    Attendant attendant
    Team team
    Integer age
    Date date_of_birth
    Date created_at
    Date updated_at

    static constraints = {
        first_name nullable: false, blank:false
        last_name nullable: false, blank:false
        mobile nullable:false, matches: /\d{10}/
        email nullable: false, email: true
        gender nullable: false
        age nullable: false, min: 0
        team nullable: false
        attendant nullable: false
        date_of_birth nullable:false
        created_at nullable: false
        updated_at nullable: false
    }

    static mapping = {
        date_of_birth type: 'date'
    }

    // Automatically set created_at and updated_at before insertion
    def beforeInsert() {
        created_at = new Date()
        updated_at = new Date()
    }

    def beforeUpdate() {
        updated_at = new Date()
    }

}
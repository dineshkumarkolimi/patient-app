package com.app

class Team {

    String name
    Integer size
    Date created_at
    Date updated_at

    static hasMany = [attendants: Attendant]

    static constraints = {
        name nullable:false, blank:false
        size nullable:false, min:0
        created_at nullable:false
        updated_at nullable:false
    }

    static mapping = {
        created_at defaultValue: new Date()
        updated_at defaultValue: new Date()
    }
}
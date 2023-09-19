package com.example.sih_version_3.dtaclasses

data class User(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val phoneNumber: String? = null,
    val language: String? = null,
    val role: UserRole? = null,
    val skill: String? = null,
    val uid:String?=null,

) {
    constructor() : this(null, null, null, null, null, null, null)
}




enum class UserRole {
    STUDENT,
    MENTOR
}
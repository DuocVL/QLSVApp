package com.example.qlsvapp

import java.io.Serializable


data class Student(
    var id: Int = 0,
    var name: String,
    var mssv: String,
    var email: String,
    var phone: String,
) :Serializable

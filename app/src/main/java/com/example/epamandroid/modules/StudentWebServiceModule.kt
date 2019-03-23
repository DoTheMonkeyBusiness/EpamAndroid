package com.example.epamandroid.modules

import com.example.epamandroid.backend.IWebService
import com.example.epamandroid.backend.StudentsWebService
import com.example.epamandroid.backend.entities.StudentModel
import org.koin.core.qualifier.named
import org.koin.dsl.module



val studentWebServiceModule = module {
    // single instance of HelloRepository
//    single<IWebService<StudentModel>> { StudentsWebService() }

    single { StudentsWebService()}
}
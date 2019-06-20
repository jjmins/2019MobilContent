package com.jjmin.mbliecontent.di

import com.jjmin.mbliecontent.data.remote.LoginRepository
import com.jjmin.mbliecontent.data.remote.LoginRepositoryImpl
import org.koin.dsl.module

object RepositoryModules{

    val loginmodule = module {
        single {
            LoginRepositoryImpl(get()) as LoginRepository
        }
    }

    val repositotyModule = listOf(loginmodule)
}
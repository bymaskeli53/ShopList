package com.gundogar.shoplist.di

import com.gundogar.shoplist.data.repository.ShoppingRepositoryImpl
import com.gundogar.shoplist.domain.repository.ShoppingRepository
import com.gundogar.shoplist.presentation.add.AddItemViewModel
import com.gundogar.shoplist.presentation.detail.DetailViewModel
import com.gundogar.shoplist.presentation.list.ShoppingListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::ShoppingRepositoryImpl).bind<ShoppingRepository>()

    viewModelOf(::ShoppingListViewModel)
    viewModelOf(::AddItemViewModel)
    viewModelOf(::DetailViewModel)
}

expect val platformModule: Module



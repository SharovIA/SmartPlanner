package com.ivanasharov.smartplanner.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.ivanasharov.smartplanner.DI
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.data.TaskRepository
import com.ivanasharov.smartplanner.data.TaskRepositoryImpl
import com.ivanasharov.smartplanner.di.AppComponent
import com.ivanasharov.smartplanner.di.Module.TaskRepositoryModule
import com.ivanasharov.smartplanner.di.TaskScope
import com.ivanasharov.smartplanner.di.ViewModelFactory
import com.ivanasharov.smartplanner.di.ViewModelKey
import com.ivanasharov.smartplanner.domain.AddTaskInteractor
import com.ivanasharov.smartplanner.domain.AddTaskInteractorImpl
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap

@Component(modules = [AddTaskModule::class])
@TaskScope
interface AddTaskComponent {

    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun resources(resourceProvider : ResourceProvider): Builder

        fun build(): AddTaskComponent
    }

    companion object{
        fun create() = with(DI.appComponent){
            DaggerAddTaskComponent.builder()
                .resources(resources())
                .build()
        }
    }
}

@Module
abstract class AddTaskModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    abstract fun addTaskViewModel(viewModel: AddTaskViewModel): ViewModel

    @Binds
    @TaskScope
    abstract fun addTaskInteractor(addTaskInteractor: AddTaskInteractorImpl): AddTaskInteractor


}
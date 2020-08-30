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
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractor
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractorImpl
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
        fun setResources(resourceProvider: ResourceProvider): Builder

        @BindsInstance
        fun setTaskRepository(taskRepository: TaskRepository): Builder

        fun build(): AddTaskComponent
    }

    /*   companion object {
           fun create() = with(DI.appComponent) {
               DaggerAddTaskComponent.builder()
                   .resources(resources())
                   .getTaskRepository(getTaskRepository())
                   .build()
           }
       }
     */
    companion object {
        fun create() = with(DI.appComponent) {
            DaggerAddTaskComponent.builder()
                .setResources(resources())
                .setTaskRepository(getTaskRepository())
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
    @IntoMap
    @ViewModelKey(CurrentDayViewModel::class)
    abstract fun currentDayViewModel(viewModel: CurrentDayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShowTaskViewModel::class)
    abstract fun showTaskViewModel(viewModel: ShowTaskViewModel): ViewModel



    @Binds
    @TaskScope
    abstract fun addTaskInteractor(addTaskInteractor: AddTaskInteractorImpl): AddTaskInteractor

    @Binds
    @TaskScope
    abstract fun currentDayInteractor(currentTaskInteractor : CurrentTasksInteractorImpl): CurrentTasksInteractor
}
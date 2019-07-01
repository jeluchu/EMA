package es.babel.easymvvm.android.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import es.babel.easymvvm.android.viewmodel.EmaFactory
import es.babel.easymvvm.android.viewmodel.EmaViewModel
import es.babel.easymvvm.core.navigator.EmaBaseNavigator
import es.babel.easymvvm.core.navigator.EmaNavigationState
import es.babel.easymvvm.core.state.EmaBaseState
import es.babel.easymvvm.core.state.EmaExtraData
import es.babel.easymvvm.core.state.EmaState


/**
 * View to handle VM view logic states through [EmaState].
 * The user must provide in the constructor by template:
 *  - The view model class [EmaViewModel] is going to use the view
 *  - The navigation state class [EmaNavigationState] will handle the navigation
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaView<S : EmaBaseState, VM : EmaViewModel<S, NS>, NS : EmaNavigationState> {

    /**
     * The view mdeol seed [EmaViewModel] for the view
     */
    val viewModelSeed: VM

    /**
     * The navigator [EmaBaseNavigator]
     */
    val navigator: EmaBaseNavigator<NS>?

    /**
     * The key id for incoming data through Bundle in fragment instantiation.This is set up when other fragment/activity
     * launches a fragment with arguments provided by Bundle
     */
    val inputState: S?

    /**
     * Create the view model for the view and create 3 observers
     *  - View state observer. Called as well when view is attached to the view model
     *  - Single event observer. Not called when the view is first time attached to the view model
     *  - Navigation observer. Not called when the view is first time attached to the view model
     *
     * This observers handle the events generated by view updates/errors/navigation
     * @param fragmentActivity the scope of the view model
     * @param fragment the scope of view model. If it is provided it will be the scope of the view model
     */
    fun initializeViewModel(fragmentActivity: FragmentActivity, fragment: Fragment? = null) {
        val emaFactory = EmaFactory(viewModelSeed)
        val vm = fragment?.let {
            ViewModelProviders.of(it, emaFactory)[viewModelSeed::class.java]
        } ?: ViewModelProviders.of(fragmentActivity, emaFactory)[viewModelSeed::class.java]

        onViewModelInitalized(vm)
        vm.onStart(inputState?.let { EmaState.Normal(it) })
        vm.observableState.observe(fragment ?: fragmentActivity, Observer(this::onDataUpdated))
        vm.singleObservableState.observe(fragment
                ?: fragmentActivity, Observer(this::onSingleDataSent))
        vm.navigationState.observe(fragment
                ?: fragmentActivity, Observer(this::onNavigation))

    }

    /**
     * Called when view model trigger an update view event
     * @param state of the view
     */
    fun onDataUpdated(state: EmaState<S>) {
        onStateNormal(state.data)
        when (state) {
            is EmaState.Loading -> {
                onStateLoading(state.dataLoading)
            }
            is EmaState.Error -> {
                onStateError(state.error)
            }
        }
    }

    /**
     * Called when view model trigger an only once notified event
     * @param data for extra information
     */
    fun onSingleDataSent(data: EmaExtraData) {
        onSingleEvent(data)
    }

    /**
     * Called when view model trigger an only once notified event for navigation
     * @param navigation state with information about the destination
     */
    fun onNavigation(navigation: EmaNavigationState) {
        navigate(navigation)
    }

    /**
     * Called once the view model have been provided. Here must go the view set up
     * @param viewModel of the view
     */
    fun onViewModelInitalized(viewModel: VM)

    /**
     * Called when view model trigger an update view event
     * @param data with the state of the view
     */
    fun onStateNormal(data: S)

    /**
     * Called when view model trigger a loading event
     * @param data with information about loading
     */
    fun onStateLoading(data: EmaExtraData)

    /**
     * Called when view model trigger an only once notified event.Not called when the view is first time attached to the view model
     * @param data with information about loading
     */
    fun onSingleEvent(data: EmaExtraData)

    /**
     * Called when view model trigger an error event
     * @param error generated by view model
     */
    fun onStateError(error: Throwable)

    /**
     * Called when view model trigger a navigation event
     * @param state with info about destination
     */
    fun navigate(state: EmaNavigationState) {
        navigator?.navigate(state as NS)
    }
}
# NY Times Viewer

The NY Times viewer is a "simple" app of only two screens:
 - An Article List View (where a variety of Lists can be displayed)
 - An Article Detail View

While the app is simple, it is had a full expandable architecture designed to be maintainable.

### Unit Tests

I push for Unit Tests as much as possible with my clients, and when I estimate tasks, I will default include Unit Tests as part of my estimate. My goal is roughly 80% coverage for a front end project, which should include everything except Views and pure Dependency Injection code.

This was a time-constrained project, and I was asked to complete the project first without Unit Tests to ensure I was finished within the constraints given to me. I've included the following Unit Test suites to exemplify my approach to Unit Testing.
 - `ArticleListVM`
 - `TransportService`
 - `ArticlesRepo`


### Core Architecture

#### Navigation

Navigation use Jetpack Compose Navigation in conjunction with the Navigation Compose Type package. This is to ensure proper type safety when passing in navigation arguments, something Compose Navigation is notoriously bad at.

Navigation is designed to keep Navigation _Actions_ separate from their Destinations. This helps decouple Screens from each other. In many navigation systems, a login Screen might do something like this when the user logs in: `navigator.navigate(Destination.Home)`.  This approach couples the Login Screen almost directly to the Home screen, which is inflexible.  With decoupled navigation actions, the Login screen doesn't specify the navigation destination, but only the "action" that took place, ex: `navigator.handle(NavAction.DidLogIn)`.

The normal flow for navigation looks like this:
 - NavAction is passed into the Navigator (A separate DI object interface available throughout the app).
 - The Navigator pushed the action into a StateFlow
 - The AppHost (a composable) responds to the new Action (which it has subscribed to) by asking the NavGraph for an appropriate destination for the action.
 - The NavGraph calculates what the destination is and returns it. This can be as simple as a map, but it can also allow for more complex scenario. For example, keeping track of a login token, and routing the user to the login screen instead.
 - The AppHost then take the destination returned by the NavGraph and submits it to Jetpack Compose Navigation.

#### State Reducers (data flow)

A State Reducer is a simple concept: There is `State`, there are `Actions` that can be taken on that state, and there is some way to submit those actions to the reducer. All state manipulation should occur using a reducer, with very few exceptions. Loading/progress is one of the few exceptions, although it too could be arguably folded into the State proper.

All View Models and Repositories adhere to a State Reducer, as they're _primary_ concern is the management and manipulation of state. By using `StateFlow`, data can "flow" downstream, being transformed as necessary until they reach a UI for display. Actions can then be propagated upstream to affect the desire manipulation.


#### Repositories

Repositories are designed with getting, storing, and caching data. In the case of this app, we're only getting and caching data (persisting data is beyond the scope of this project). Repositories adhere to the State Reducer pattern, where they are responsible for a single state structure that is manipulated by a definitive list of actions.

In the case of the `ArticlesRepo`, it retrieves data from the Transport Service, caches it, and makes it available to View Models in the app via a StateFlow.  It supports actions to refresh the articles, batch new articles in, switch between lists, and refresh sections. The repo will cache data to prevent unnecessary network calls.

#### View Model/View Separation

Generally, all data and actions on that data should be handled exclusively by the View Models. The View collects the view model state, displaying it as needed, while submitting actions back to the View Model.  All View Models should inherit from `BaseViewModel`, which ensure the View Models adhere to the State Reducer pattern.

Some state may be kept in the View, but _only_ if that state is purely UI dependent or is limited by the framework. Examples of this might be a drawer open/close state, which must be created/managed from within a Composable. Since it it entirely UI dependent (and does not affect any business state/logic), it is acceptable to keep that state within the UI/View.

#### Interface Separation

Where possible, Interfaces should be defined separate from their implementations except for:
 - Data Class Models
 - View Models (mostly, this is a limitation of how hilt injects view models).

All Injectable classes (again, excepting View Models) should be defined by their interface, while the DI returns the concrete implementation.

#### Dependency Injection

Hilt is used for dependency injection. All Repositories, Services, and View Models should rely on Hilt to inject the necessary dependencies. Again, those dependencies should be defined by _interfaces_.


### App Functionality

At present, the app has only two screens.

#### Article List Screen

This is the "home" screen, and displays a list of articles. By default, it will show Popular View Articles.  A slide-over navigation menu will allow the user to select from other Popular articles, or they can select from a list sections downloaded from the api. Tapping on a menu item will load in the new articles.

A theme switcher is located on the top right. It will toggle the UI between light and dark themes. By default, the theme will use a theme appropriate to the phone's dark/light mode.

When the user taps on an article, they're brought to a detail view.

#### Article Detail Screen

The detail screen will allow the user to see additional data for the article they selected. This mostly includes an abstract of the article. The article itself is not shown due to limitations in the API (NY Times wants you to pay for that privilege). So instead, a "Read more..." button is provided that will take the user to the article in a browser. 

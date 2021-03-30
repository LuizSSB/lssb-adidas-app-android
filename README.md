
# Adidas Challenge

Android app for the job application of Android developer at Adidas.

The app:
- opens with a paginated listing of products;
- allows searching for products with a specific ID;
- allows selecting a product, in which case it opens a screen with its details and reviews;
- allows writing a review for a product in its detail screen.

## Preview

![Listing preview](https://i.imgur.com/ABCbQ7f.png)

![Product details](https://imgur.com/kTK3re2.png)![Adding Review](https://imgur.com/xfh2fFX.png)

## Features

- Devices: Android smartphones (can run on tablets, but layouts have not been optimized);
- Android: API 16+;
- Language: Kotlin;
- AndroidX facilities:
	- ViewModel / LiveData
	- Paging
	- Navigation
	- RecyclerView
	- ConstraintLayout
	- SwipeRefreshLayout
	- ViewBinding
	- AppCompat
- Dependency injection: Koin;
- Unit tests;
- No error, all tests passing; may contain minor lint alerts.

The app adopts the Model-View-Intent (MVI) architecture. As such, it consists of several layers (from the lowest to the highest level):

* **Service**: interfaces with services external to the app. Each class/interface represents the connection to services related to some kind of entity.
* **Repository**: manages the data. Uses the Service layer to obtain/save the data and, if necessary, manipulates it for the rest of the application. Each class/interface represents the handling of some kind of entity.
* **Controller/ViewModel**: manages the state of the application. Uses the Repository layer to obtain/save the data, process it and then defines the state of a screen. Receives actions from the ViewController layer and processes them. Each class/interface represents the state management of some screen, or piece of it.
* **ViewController**: `Activity`/`Fragment`. Binds the states/actions of the Controller/ViewModel layer the View layer
* **View**: displays the visual content of the app.

Controller/ViewModel is the main layer of the app, as it concentrates its state values and actions. I tried to make the ViewController and View layers "dumb", depending entirely on the Controller/ViewModel layer.

This layer, as the name implies, is composed of two main types: Controllers and ViewModels. In practice, both are the same: both inherit from AndroidX `ViewModel`. Conceptually, Controllers encapsulate specific functions that appear in more than one screen and that need to be linked to Views in, usually, the way (e.g., listing of generic elements). ViewModels are specializations of Controllers that represent the totality of screen (e.g., product listing, product detail, review listing).

The idea of ​​this structure is to allow ViewModels (and Controllers, in more advanced cases) to have references to Controllers who perform the recurring actions they need. The great advantage of this approach is that it favors composition over inheritance, which is very useful, as it avoids the need for multiple inheritance, as well as all the problems arising from inheriting opaque behaviour from super classes. The downside is that AndroidX `ViewModel` was not designed to work that way, so we need to use a little bit of reflection magic to properly clear ViewModels from memory.

All Controllers/ViewModels inherit from `MVIController<TState, TEffect, TIntent>`. The type parameters:

* `TState`: type that carries the status data of the Controller/ViewModel (e.g., loading situation of a listing). Preferably a `data class`.
* `TEffect`: type that indicates some fire-and-forget action that the View must perform (e.g., show error).
* `TIntent`: type that indicates some action performed on the view that must be processed by the Controller/ViewModel.

This class has three important members:

* `val state: LiveData<TState>`: carries the current state of the Controller/ViewModel. The entire state MUST be described and represented exclusively in this object.
* `val effects: Flow<TEffect>`: Kotlin coroutine's flow with the effects triggered by the Controller/ViewModel on the View.
* `fun handleIntent(intent: TIntent)`: method used by ViewControllers to transmit actions from the View to the Controller/ViewModel.

It is the opinion of this author that the source code of the app is sufficiently simple, clear and concise, thus dispensing with documentation. However, comments were added to key points, where design decisions or technical limitations influenced the structure of the code.

## Notes

The app takes some liberties in the way it works with the REST API provided for the task.

The REST API does not have pagination and always returns all products/reviews at once, so the app simulates pagination by cutting off pieces of the response for each query.

The app also simulates the search for products by filtering the products returned by the API by ID, since all the products were registered with the same name ("product"). The app also fakes the price and currency of the queried products, as they come nil from the API.

In the task's  description, it is said to use a second API (port 3002) to query and insert product reviews. However, this second API does not work properly: the query results have erroneous data and the insert responds with status 200, but does not, in fact, insert the review. Because of this, the app show the reviews returned by the product API (port 3001), as they are correct.

## Running the source code

**Note**: these instructions assume the API server running with HTTP in the same machine as Android Studio and all devices in the same network.

Because devices (either virtual or physical) running the app cannot connect to the API server via `localhost`, we need the server's IP address in the local network. The build script of the `app` module, `app/build.gradle`, takes care of finding the local IP address of the host computer and points the app to it.

In any case, for devices running Android 24+, it's necessary to change the network security configuration file, located at `app/main/src/res/xml/network_security_config.xml`, so clear text communication with the servers IP address is allowed. Just add a `<domain includeSubdomains="true">LOCAL_IP</domain>` element to the document, replacing `LOCAL_IP` with the host computer local IP address and it should be fine.

## "Publishing" the app

The task's description mentioned continuous integration and deployment, I thought it would be nice to put something related to it in this app. It's more of an example than anything else, but I believe it demonstrates something.

The app is integrated with Fastlane, a tool used in mobile software development to script and automatize distribution, usually to Google's Play Store or Apple's App Store, but also to whatever the app requires.

The file `fastlane/Fastfile` contains two "lanes" (distribution functions defined in Fastlane). 

The `test` lane simply runs the app's tests (and fails if any of them fails). it can be executed as `fastlane android test`

The `release` lane is a bit more involved, it represents a simple approach to how I would distribute new versions of the app to Google's Play Store. This lane expects the app's version name to follow the regex `[0-9]+\.[0-9]+\.[0-9]+`It also requires a parameter,`bump_type` which indicates how the app's version name should be updated. It must be one of the following:

- `major`: release of new major version. Bumps the most significant version number (e.g. 1.2.3 -> 2.0.0)
- `minor`: release of new features on in the current version . Bumps the middle number (e.g. 1.2.3 -> 1.3.0)
- `patch`: release of fixes to the current version. Bumps the least significant number (e.g. 1.2.3 -> 1.2.4)

It can executed as `fastlane android release bump_type:<bumpType>`, where <bumpType> is one of the values above

The `release`lane works like this:
 1. Checks if `bump_type` options is provided and correct.
 2. Ensures the current branch has no uncommitted files and checkouts to `develop`
 3. Checkouts to a new branch called `release/<bump_type>`
 4. Runs tests
 5. Bumps the app's version code and name
 6. Builds the app
 7. Commits changes
 8. Checkouts to `master`
 9. Merges with `release/<bump_type>`
 10. Tags the commit with `builds/androidrelease/v<new_version_code>`
 11. Pushes to origin
 12. Repeat steps 8, 9, and 11 with `develop`
 13. Deletes `master` (local) and `release/<bump_type>` branches.
 
## Main packages and files

### `com.luizssb.adidas.confirmed`

Root package, contains classes related to the application, as a whole.

- `AppApplication`: subclass of` Application`, used by Koin.
- `DI`: Koin module definition.

### `com.luizssb.adidas.confirmed.dto`

Data transfer objects.

- `Product`: a product.
- `Review`: a review of a specific product.
- `Rating`: enum class with possible rating values for a `Review`.

### `com.luizssb.adidas.confirmed.repository`

Repositories and paging sources for handling data.

- `.paging.DefaultPagingSource<T>`: basic implementation of `PagingSource<Int, T>`.
- `.product.ProductPagingSource`: paging source for product search.
- `.product.ProductRepository`: repository for searching products and acquiring product details.
- `.review.ReviewPagingSource`: paging source for querying reviews of a product.
- `.review.ReviewRepository`: repository for querying product reviews and inserting a review.

### `com.luizssb.adidas.confirmed.service`

Classes and interfaces for connecting to the REST API.

- `PaginationResult`: indicates the result of the query for a product/review listing page.
- `FakeFilterEx`: extension to simulate pagination.
- `.product.ProductService`: interface to consume product-related APIs.
- `.review.ReviewService`: interface to consume review-related APIs.
- `.retrofit. *`: package with definitions of APIs and types for Retrofit.

### `com.luizssb.adidas.confirmed.viewcontroller`

Activities, fragments. adapters, viewHolders.

- `SplashActivity`: introductory activity. It merely and automatically transitions to `MainActivity`.
- `MainActivity`: main activity. It merely carries the NavController for the content fragments.
- `ListingViewControllerEx`: extension for binding `ListingController` to its views.
- `.product.ProductFragment`: fragment with details of a product and a list of its reviews.
- `.product.ProductListFragment`: fragment with listing and search of products.
- `.adapter.*`: adapters and viewHolders for products, reviews, and more
- `.adapter.GenericLoadState*`: adapter and viewHolder for infinite scrolling.

### `com.luizssb.adidas.confirmed.viewmodel`

Controllers e ViewModels.

- `MVIController`: subclass of AndroidX `ViewModel` that represents an MVI controller for the app.
- `DefaultMVIController`: abstract class with standard implementation for the abstract members of` MVIController`. 
	- Also defines `@MVIControllerProperty`, which must be added to getters of `MVIController`s owned by other `MVIController`s, so that they are properly discarded.
- `MVIViewModel`: subClass of `DefaultMVIController` that represents ViewModels.
- `.list.Listing`: definition of the state, effects, intent, and controller types used to manage the listing of generic objects.
- `.product.Products`: definition of state, effects, intention and controller types used to manage the screen with product search/listing.
- `.product.ProductDetail`: definition of the state, effects, intention and controller types used to manage the screen with details of a product.
- `.review.Reviews`: definition of the state, effects, intention and controller types used to manage the screen listing reviews of a product.

### `com.luizssb.adidas.confirmed.utils`

Utility types for the rest of the application.

- `PageRef`: represents the reference to a page of products / reviews that should be queried.
- `.extensions. *`: useful extensions for different types.

**Note**: the above listings omit  the implementations of Services, Repositories and ViewModels, as they merely follow their interfaces. In the code, these implementations are all contained in `*.impl` packages.

## TODO

Due to lack of time, some things could not be implemented:

- Caching with Room and mediation between cache and REST API when querying data for listing (good lord, how AndroidX Paging sux!)
- Tests for `ProductsViewModelImpl`,` ProductDetailViewModelImpl` and `ReviewsViewModelImpl`.
- Tests on fragments.
- Adapt layouts and dimensions for large devices.
- Support to dark mode.

-- Luiz Soares dos Santos Baglie (luizssb.biz {at} gmail {dot} com)
# Flickr Client

#### Feature requirements
- Photos search
  - (bonus) Infinite scroll on search results page
  - App should query Flickr’s API for a given tag (e.g., “moon”)
  - Given a response, it should list the results with links to photo detail pages
- Photo detail page
  - Be sure to include:
  - Hero image
  - Photo title
  - Photo description
  - Date taken
  - Date posted
  - Link back to search results

#### Technology Used
- Flickrj Android library
- Kotlin Coroutines
- Koin for Service Locator/Dependency Injection
- Android Paging 3
- Kotlin Flows
- Jetpack Compose UI with LazyPaging
- Jetpack Compose Material3 for Theming and Composables
- Jetpack ViewModels
- Coil to asynchronously load images from Flickr URL

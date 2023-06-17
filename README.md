# Flickr Client (WIP)

<img src="https://github.com/jakesilver/flickr-client/assets/5214972/2a3e6ed9-f980-46ef-a535-3e82e8db0f44" width="300" />

#### Feature requirements
- Photos search
  - App should query Flickr’s API for a given tag (e.g., “moon”)
  - Given a response, it should list the results with links to photo detail pages
  - Infinite scroll on search results page
- Photo detail page
  - Image
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
- Jetpack Compose Navigation
- Jetpack ViewModels
- Coil to asynchronously load images from Flickr URL

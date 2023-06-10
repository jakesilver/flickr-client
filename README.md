# Flickr Client

<img src="https://github.com/jakesilver/flickr-take-home/assets/5214972/636b96d5-cfa2-461d-8234-00c053acf7e7" width="300" />

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

# Flickr Client (WIP)

- Flickr API Key needed

|Search|Details|
|---|---|
|<img src="https://github.com/jakesilver/flickr-client/assets/5214972/2a3e6ed9-f980-46ef-a535-3e82e8db0f44" width="300" /> | <img src="https://github.com/jakesilver/flickr-client/assets/5214972/98dc5a97-1d30-49e9-8679-976c59078801" width="300" /> |


#### Features
- Photos search
  - App queries Flickr’s API for a given tag (e.g., “moon”)
  - Lists the results with links to photo detail pages
  - Infinite scroll on search results page
- Photo detail page
  - Image
  - Photo title
  - Photo description
  - Date taken
  - Date posted
  - Navigation back to search results

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
- Coil to asynchronously load images from Flickr

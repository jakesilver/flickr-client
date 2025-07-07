# Flickr Client (WIP)

- Flickr API Key needed

|Search|Details|
|---|---|
|<img src="https://github.com/user-attachments/assets/4f921e4d-bec3-4745-8f0c-b27512cb3c7d" alt="Photo Search screen with basset hound search results" width="300" /> | <img src="https://github.com/user-attachments/assets/bab38371-d921-4aa1-bf6e-652e448b7d43" alt="Photo Details screen with a specific basset hound photo titled Major" width="300" /> |


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

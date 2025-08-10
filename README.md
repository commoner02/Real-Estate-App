## Overview
A comprehensive real estate application developed in Java using Android Studio that provides property listing, user authentication, and property management features. The app integrates with Firebase for authentication, database storage, and cloud functions.

## Key Features
- **User Authentication**: Secure login and signup using Firebase Authentication
- **Property Management**:
  - Add new property listings with detailed information
  - View property details with images
  - Mark properties as favorites
- **Data Integration**:
  - Fetch and parse online property data via JSON API
  - Store property data in Firebase Realtime Database
- **User Experience**:
  - Intuitive bottom navigation
  - Responsive UI with smooth transitions
  - Loading indicators for network operations

## Technical Architecture
```
commoner02-real-estate-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/realestateapp/
│   │   │   │   ├── adapters/          # RecyclerView adapters
│   │   │   │   ├── fragments/         # UI fragments
│   │   │   │   ├── listeners/         # Event listeners
│   │   │   │   ├── models/            # Data models
│   │   │   │   ├── network/           # API service interfaces
│   │   │   │   ├── screens/           # Activity classes
│   │   │   │   ├── utils/             # Utility classes
│   │   │   │   └── MainActivity.java  # Entry point
│   │   │   └── res/                   # All resources (layouts, drawables, etc.)
│   └── build.gradle.kts               # App-level dependencies
├── build.gradle.kts                   # Project-level configuration
└── google-services.json               # Firebase configuration
```

## Dependencies
- Firebase (Authentication, Realtime Database, Firestore, Storage)
- Retrofit (for API calls)
- Glide (for image loading)
- Volley (for network requests)
- Material Components (for UI elements)

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/commoner02/Real-Estate-App.git
   ```
2. Open the project in Android Studio
3. Add your Firebase configuration files:
   - Place `google-services.json` in both project root and app/ directories
4. Sync Gradle dependencies
5. Build and run the app

## Configuration
1. Set up Firebase project:
   - Enable Authentication (Email/Password)
   - Enable Realtime Database
   - Configure security rules as needed
2. Update API endpoints in `PropertyService.java` if needed

## Usage
1. **Authentication**:
   - Users can register or login using email/password
   - Password recovery available

2. **Property Management**:
   - Add new properties with details and images
   - Browse properties from local database or online API
   - Mark properties as favorites

3. **Navigation**:
   - Bottom navigation between Home, Favorites, and Account sections

## Code Quality
- Follows Android best practices
- Proper separation of concerns (models, views, controllers)
- Uses modern Android architecture components
- Includes error handling and loading states

## Screenshots
(Will add later)

## License
MIT License - See [LICENSE](LICENSE) file for details.

## Contributing
Contributions are welcome! Please fork the repository and submit pull requests.

## Support
For issues or questions, please open an issue on the GitHub repository.

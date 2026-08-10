# NeedNet 🤝

### A Hyperlocal Help Sharing Android Application

NeedNet is a native Android application designed to connect people who need assistance with people who are willing to offer help within their locality.

The application provides a simple platform for users to register and log in, offer different types of help, select a location using an interactive map, and store help offers in Firebase Firestore. It combines Android UI development, Firebase authentication and cloud storage, location services, reverse geocoding, and map-based interaction.

---

## ✨ Features

### 🔐 User Authentication
- User registration using email and password.
- Secure login using Firebase Authentication.
- Authentication-aware splash screen.
- Automatic navigation based on the user's login state.
- Logout functionality through the navigation drawer.

### 🏠 Home Dashboard
- Welcome screen after successful authentication.
- Dedicated **Find Help** and **Offer Help** flows.
- Navigation drawer for application navigation and logout.
- Card-based interface with click animations.

### 🙋 Offer Help
Users can publish an offer by providing:
- Offer title
- Type of help
- Description/message
- Location

Supported help types include:
- Borrow
- Sell
- Donate
- Rent
- Volunteer

Offer details are stored in Firebase Firestore along with:
- Firebase user ID
- Location/address
- Latitude
- Longitude
- Timestamp

### 📍 Interactive Location Selection
The application includes an OpenStreetMap-based location picker using OSMDroid.

Users can:
- View an interactive map.
- Use their current GPS location.
- Tap on the map to select a location.
- Display the selected address through reverse geocoding.
- Save latitude and longitude together with the selected address.

### 🔎 Find Help
The Find Help interface provides:
- Search bar UI
- RecyclerView-based help request cards
- Category and distance information
- Request Help action
- Animated card interactions

> The current repository contains sample/hardcoded help-request data for the Find Help listing. Firestore-backed publishing is currently implemented for the Offer Help workflow.

---

## 🛠️ Technology Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| Platform | Android |
| UI | XML Layouts, Material Components |
| UI Components | RecyclerView, CardView, Navigation Drawer, ConstraintLayout |
| Authentication | Firebase Authentication |
| Cloud Database | Firebase Firestore |
| Location | Google Play Services Location |
| Maps | OSMDroid / OpenStreetMap |
| Geocoding | Android Geocoder |
| Architecture | Activity-based Android architecture |
| View Binding | Android View Binding |
| Build System | Gradle / Kotlin DSL |
| Testing | JUnit, AndroidX Test, Espresso |
| Minimum SDK | 24 |
| Target SDK | 36 |
| Compile SDK | 36 |
| JVM | Java 11 / Kotlin JVM 11 |
| Version Control | Git / GitHub |

---

## 🏗️ Application Flow

```text
                    ┌───────────────┐
                    │ Splash Screen │
                    └───────┬───────┘
                            │
              ┌─────────────┴─────────────┐
              │                           │
        Logged In                   Not Logged In
              │                           │
              ▼                           ▼
       Welcome Screen                  Login
              │                           │
       ┌──────┴──────┐                    ▼
       │             │                 Register
       ▼             ▼
  Find Help      Offer Help
                    │
                    ▼
             Location Picker
                    │
          ┌─────────┴─────────┐
          │                   │
     Current GPS         Map Selection
          │                   │
          └─────────┬─────────┘
                    ▼
             Address + Lat/Lng
                    │
                    ▼
             Firebase Firestore
                    │
                    ▼
               Help Offer
```

---

## 📍 Location Architecture

NeedNet combines device location services with OSMDroid to provide a map-based location selection experience.

```text
GPS / Map Tap
     │
     ▼
GeoPoint
     │
     ├── Latitude
     ├── Longitude
     │
     ▼
Android Geocoder
     │
     ▼
Readable Address
     │
     ▼
OfferHelpActivity
     │
     ▼
Firebase Firestore
```

The selected location is stored with both human-readable address information and geographic coordinates, making the data suitable for future location-based discovery.

---

## 🔥 Firebase Integration

NeedNet uses Firebase for application authentication and cloud data storage.

### Firebase Authentication

Used for:
- Account creation
- Email/password login
- Current-user session checking
- Logout

### Firebase Firestore

Help offers are stored in the `help_offers` collection.

Example document structure:

```text
help_offers
│
├── userId
├── offerTitle
├── helpType
├── message
├── location
├── latitude
├── longitude
└── timestamp
```

This structure keeps the offer information together with its geographic context.

---

## 📱 Main Screens

### Splash Screen
Checks whether a Firebase-authenticated user already exists and routes the user accordingly.

### Login
Provides email/password authentication through Firebase Authentication.

### Register
Allows new users to create an account using their name, email, and password.

### Welcome Screen
Acts as the application's main dashboard and provides access to:
- Find Help
- Offer Help
- Navigation Drawer
- Logout

### Find Help
Displays help-request cards using RecyclerView and provides a search interface.

### Offer Help
Allows users to create a help offer and select its location before submitting it.

### Location Picker
Provides an interactive OpenStreetMap interface for selecting and saving a location.

---

## 📂 Project Structure

```text
NeedNet/
│
├── app/
│   └── src/
│       ├── androidTest/
│       ├── main/
│       │   ├── java/com/example/neednet/
│       │   │   ├── FindHelpActivity.kt
│       │   │   ├── HelpRequest.kt
│       │   │   ├── HelpRequestAdapter.kt
│       │   │   ├── LocationPickerActivity.kt
│       │   │   ├── OfferHelpActivity.kt
│       │   │   ├── SplashActivity.kt
│       │   │   ├── login.kt
│       │   │   ├── register.kt
│       │   │   └── welcomescr.kt
│       │   │
│       │   ├── res/
│       │   │   ├── anim/
│       │   │   ├── drawable/
│       │   │   ├── layout/
│       │   │   ├── menu/
│       │   │   ├── mipmap-*/
│       │   │   ├── values/
│       │   │   └── xml/
│       │   │
│       │   └── AndroidManifest.xml
│       │
│       └── test/
│
├── gradle/
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

---

## ⚙️ Key Implementation Details

### Authentication State

The splash screen checks the current Firebase authentication state:

```kotlin
if (FirebaseAuth.getInstance().currentUser != null) {
    welcomescr::class.java
} else {
    login::class.java
}
```

This prevents authenticated users from unnecessarily returning to the login screen.

### Location Selection

The location picker uses:

- `FusedLocationProviderClient` for device location.
- `MapView` from OSMDroid for map interaction.
- `GeoPoint` for geographic coordinates.
- `Marker` for selected locations.
- `Geocoder` for converting coordinates into readable addresses.

### Firestore Offer Creation

An offer contains both descriptive and geographic information:

```kotlin
val offerData = hashMapOf(
    "userId" to userId,
    "offerTitle" to offerTitle,
    "helpType" to helpType,
    "message" to message,
    "location" to locationText,
    "latitude" to latitude,
    "longitude" to longitude,
    "timestamp" to System.currentTimeMillis()
)
```

---

## 🔒 Permissions

The application requests permissions required for its location-based functionality:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

Location access is requested at runtime when required.

---

## 🚀 Getting Started

### Prerequisites

- Android Studio
- Android SDK 36
- JDK 11 or compatible Android Studio JDK
- Android device or emulator with API 24+
- A Firebase project configured for the Android application
- Internet access for Firebase and map tiles

### Installation

1. Clone the repository.

2. Open the project in Android Studio.

3. Configure Firebase for the application.

4. Add the Firebase configuration file:

```text
app/google-services.json
```

5. Sync the Gradle project.

6. Build and run the application on an Android device or emulator.

> Do not commit private credentials or sensitive Firebase configuration to a public repository unless the configuration is safe to expose and your Firebase security rules are properly configured.

---

## 🗺️ Map Configuration

NeedNet uses **OSMDroid with OpenStreetMap tiles** for map rendering.

The application initializes the OSMDroid configuration and uses `Mapnik` as the tile source.

For production use, review OpenStreetMap tile usage policies and configure an appropriate user agent and tile strategy.

---

## 🧪 Testing

The project is configured with:

- JUnit
- AndroidX JUnit
- Espresso

The application also includes Android instrumented-test and unit-test source directories.

---

## 📌 Current Project Scope

The current version focuses on:

- Authentication
- Help-offer creation
- Location selection
- Cloud persistence of offers
- Basic help discovery UI

The **Find Help** listing currently uses sample data, while the **Offer Help** workflow is connected to Firestore.

---

## 🔮 Future Improvements

Potential improvements include:

- [ ] Load help requests directly from Firestore.
- [ ] Add real-time Firestore listeners for new requests/offers.
- [ ] Implement search and category filtering.
- [ ] Calculate actual distance between users and help requests.
- [ ] Add map markers for nearby help offers.
- [ ] Add request/offer detail screens.
- [ ] Allow users to accept or respond to help requests.
- [ ] Add profile management.
- [ ] Add image upload for offers.
- [ ] Add notification support.
- [ ] Introduce a repository/ViewModel layer for improved scalability.
- [ ] Add automated unit and UI test coverage.
- [ ] Improve Firebase security rules and validation.

---

## 💡 What This Project Demonstrates

NeedNet demonstrates practical Android development experience across several areas:

- Native Android application development with Kotlin
- Firebase Authentication and Firestore integration
- Location-aware application design
- Map-based user interaction
- Runtime permission handling
- Reverse geocoding
- RecyclerView-based UI
- Navigation Drawer implementation
- View Binding
- Material UI components
- Cloud data persistence
- Git-based development and version control

---

## 👨‍💻 Developer

**Ayush Srivastava**

B.Tech — Mechatronics and Automation Engineering  
Indian Institute of Information Technology Bhagalpur

---

## 📄 License

This project is intended for educational and portfolio purposes.

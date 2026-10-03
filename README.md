# Overview

News Hub is an Android app that lets you browse the latest News headlines. Users set a display name that appears on their profile and home screen, and news is fetched from NewsAPI.

# Features

- Browse the latest news headlines on the home screen
- Filter news by category (for example: business, technology, sports, health)
- Profile screen where you enter a display name
- Display name shown on the profile and home screens

[Software Demo Video](https://youtu.be/76nUlRyxCUk)

# Development Environment

- Language: [Kotlin / Java]
- UI: [Jetpack Compose ]
- IDE: Android Studio
- API: NewsAPI
- Min SDK: [e.g. 11]
- Getting Started
- Prerequisites
- Android Studio (latest stable version)
- An Android emulator or a physical Android device
- A free NewsAPI key from newsapi.org
- Setup
- Clone the repository
- bash
- git clone https://github.com/Ekg23/news-hub.git
- cd news-hub
- Add your NewsAPI key The API key is not stored in the repository. Open (or create) the local.properties file in the project's root folder and add:
- properties
- NEWS_API_KEY=your_api_key_here

The key name must match the one read in your build.gradle file. The app accesses it through BuildConfig. local.properties is listed in .gitignore, so your key stays on your computer.

# Usage

Open the app and go to the Profile screen.
Enter your display name and save.
Return to the Home screen to see your name and the latest headlines.
Tap a category to filter the news.
Troubleshooting
No news loads: check that your API key is correct in local.properties, that the device has internet access, and that you synced Gradle after adding the key.
Build error about BuildConfig: make sure buildConfig is enabled in your build.gradle and the key name matches local.properties.
rateLimited or 429 errors: the free NewsAPI plan has a daily request limit. Try again later.
Author

# Useful Website

{Make a list of websites that you found helpful in this project}

- [Android Studio Documentation](https://developer.android.com/develop)
- [W3 School](https://www.w3schools.com/kotlin/index.php)

Geoffrey Kofi Etu, BYU Pathway

Acknowledgements

News data provided by NewsAPI.

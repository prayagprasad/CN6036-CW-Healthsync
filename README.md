# HealthSync: Personal Health & Fitness Tracker 🏋️‍♂️💧



HealthSync is a data-driven Android application designed to provide a frictionless, highly personalized health-tracking experience. Built with a strict **MVVM architecture**, it automates complex Total Daily Energy Expenditure (TDEE) calculations and utilizes real-time cloud syncing to track daily macronutrients, hydration, and weight trends.



## 🌟 Key Features

* **Smart Onboarding:** Calculates BMI, BMR, and TDEE to generate custom macronutrient targets based on user goals (Cut, Maintain, Bulk).

* **Frictionless Daily Logging:** Additive data-entry system for calories, protein, carbs, fats, and water.

* **Real-Time Dashboard:** Visual percentage indicators for daily macronutrient completion.

* **Historical Trend Analysis:** A "Time Machine" calendar navigator that fetches previous logs and calculates weight trajectory (e.g., +0.5kg ⬆️) compared to the previous day.

* **Secure Cloud Storage:** Powered by Firebase Authentication and Firebase Realtime Database with strict user-specific read/write security rules.



## 🛠️ Tech Stack

* **Language:** Kotlin

* **Architecture:** Model-View-ViewModel (MVVM)

* **Backend:** Firebase Authentication & Firebase Realtime Database

* **UI Design:** XML with Material Design Components



## 🚀 Setup and Installation Instructions

To run this project locally, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone Healthsync.git]https://github.com/prayagprasad/CN6036-CW-Healthsync.git

2. **Open in Android Studio:**
   * Launch Android Studio.
   * Select **File > Open** and navigate to the cloned `CN6036-CW-Healthsync` directory.

3. **Sync Gradle:**
   * Allow Android Studio to download the required dependencies. If it doesn't happen automatically, click **File > Sync Project with Gradle Files**.

4. **Run the Application:**
   * Select an Android Emulator (API 24 or higher recommended) or connect a physical Android device.
   * Click the green **Run (Shift + F10)** button in the top toolbar.
     
*(Note: Firebase is already integrated. Simply create an account within the app to begin testing the Realtime Database functionality).*

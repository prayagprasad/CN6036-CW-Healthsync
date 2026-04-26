# HealthSync 🏋️‍♂️💧

HealthSync is a personalized, data-driven health and fitness tracking Android application. Designed to eliminate "feature bloat," it provides users with an intelligent onboarding flow that calculates their Total Daily Energy Expenditure (TDEE) and macronutrient goals, paired with a frictionless, additive logging system for daily tracking.

## 🚀 Key Features

* **Smart Onboarding:** Calculates BMI, BMR, and TDEE based on user biometrics (height, weight, age, gender) and generates custom caloric and macronutrient goals (Cutting, Maintaining, or Bulking).
* **Additive Daily Logging:** A frictionless data-entry system allowing users to incrementally add calories, protein, carbs, fats, and water throughout the day without overwriting previous entries.
* **Real-Time Progress Tracking:** Visual percentage indicators for daily macronutrient completion.
* **Historical Trend Analysis:** A "Time Machine" date navigator that fetches previous logs and calculates real-time weight trajectory (e.g., +0.5kg ⬆️) compared to the previous day.
* **Secure User Management:** Cloud-based authentication and secure, isolated data storage.

## 🛠️ Tech Stack & Architecture

* **Language:** Kotlin
* **Architecture:** Model-View-ViewModel (MVVM)
* **Backend / Database:** Firebase Realtime Database
* **Authentication:** Firebase Authentication
* **UI/UX:** XML Layouts, Material Design Components

## 📋 Prerequisites

To build and run this project, you will need:
* [Android Studio](https://developer.android.com/studio) (Latest version recommended)
* An Android Emulator or physical device running Android API 24 or higher.
* An active Internet connection (for Firebase services).

## ⚙️ Setup and Installation Instructions

Follow these steps to run the application locally:

**1. Clone the Repository**
```bash
git clone https://github.com/prayagprasad/CN6036-CW-Healthsync.git
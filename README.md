# Harmony Hub

A cutting-edge co-parenting Android application focused on child well-being, conflict reduction, and ease of use.

## Overview

Harmony Hub is a comprehensive co-parenting application designed to facilitate communication, scheduling, expense tracking, and information sharing between co-parents. The app prioritizes child well-being through its features and design.

## Key Features

- **Secure Authentication**: Google Sign-In for user authentication
- **Enhanced Scheduling**: Robust calendar system for co-parenting schedules
- **Secure Messaging**: Communication tools for co-parents
- **Expense Tracking**: Track and share expenses related to children
- **Document Storage**: Store and share important documents
- **Morning & Evening Checker**: Voice and text check-ins for transitions

## Technical Architecture

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Clean Architecture principles
- **Backend**: Firebase (Authentication, Firestore, Storage, Functions)
- **Dependency Injection**: Hilt
- **Asynchronous Operations**: Kotlin Coroutines and Flow

## Module Structure

- **app**: Main application module
- **core**: Core functionality shared across features
  - **ui**: Common UI components
  - **common**: Utilities and extensions
  - **data**: Data repositories and sources
  - **model**: Data models
  - **network**: Network communication
  - **testing**: Testing utilities
- **feature**: Feature modules
  - **auth**: Authentication
  - **schedule**: Calendar and scheduling
  - **messaging**: Communication features
  - **expense**: Expense tracking
  - **storage**: Document storage
  - **checkin**: Morning & Evening checker

## Development Setup

1. Clone the repository
2. Add your `google-services.json` file in the `app` directory
3. Build and run the project using Android Studio

## Requirements

- Android 8.0 (API 26) or higher
- Internet connection for syncing data
- Google account for authentication

## License

This project is proprietary and confidential. Unauthorized copying, distribution, or use is strictly prohibited.

# ExTrack — Expense Tracker (Android / Kotlin)

Offline-first expense tracker app built with Kotlin + Room using MVVM architecture.  
Includes History (soft delete), calendar-based filters, and auto-delete scheduling (WorkManager + DataStore).

## Features
- Add income / expense transactions
- Home shows only active transactions
- Soft delete (deleted items move to History)
- History filters: Today / This Week / This Month / All
- Clear all deleted transactions
- Auto-delete policy in Settings: Never / Day / Week / Month
- Light/Dark theme support

## Tech Stack
- Kotlin
- MVVM
- Room (SQLite) + migrations
- LiveData
- WorkManager
- DataStore (Preferences)
- Material Design 3

## Screenshot
![image alt](https://github.com/ShashankVinesh/ExTrack/blob/f0813c9d1d7896ccee7644ba89958b5a938052c1/ExTrackMockup.png)

## Demo Video
https://github.com/user-attachments/assets/8b8a8108-9459-4ae9-b4b9-1ffbe537621b

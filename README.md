Android Calculator App
A simple and user-friendly calculator app built using Kotlin and Android Studio. This app supports basic arithmetic operations and allows users to switch between Light Mode and Dark Mode seamlessly.



Features
Basic arithmetic operations: Addition, Subtraction, Multiplication, Division.

Input history display.

Calculation memory saved on app pause.

Toggle between Light and Dark Mode.

Dark mode preference saved even after app restart.

Modern and clean UI with a custom app icon.



Tech Stack
Kotlin

Android Studio

View Binding

SharedPreferences (for theme and data persistence)

Material Design Components



How to Run
1. Clone the repository: git clone https://github.com/yourusername/Android-Calculator-App.git
2. Open in Android Studio.
3. Run the app on an emulator or Android device.



Project Structure

app/
 ├── src/
 │    ├── main/
 │    │    ├── java/com/coder/myapplication/
 │    │    │    ├── MainActivity.kt
 │    │    │    └── ChangeThemeActivity.kt
 │    │    ├── res/
 │    │    │    ├── layout/activity_main.xml
 │    │    │    ├── layout/activity_change_theme.xml
 │    │    │    ├── menu/settings_menu.xml
 │    │    │    └── mipmap/ (app icons)
 │    │    └── AndroidManifest.xml
 └── build.gradle



 Folder Descriptions
MainActivity.kt: Calculator logic and UI handling.

ChangeThemeActivity.kt: Dark/Light theme toggle screen.

activity_main.xml: UI layout for calculator screen.

activity_change_theme.xml: UI layout for theme switch screen.

settings_menu.xml: Menu for settings (theme switch).



How to Contribute
Fork the project.

Create a new branch.

Make your changes.

Submit a pull request.




License
This project is licensed under the MIT License.


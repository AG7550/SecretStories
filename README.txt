# Secret stories

Secret stories is a chat aplication that uses encryption when communicating between clients and server.

## Installation

Install Android Studio and choose "Import project(Gradle, Eclipse ADT, etc.)". Choose the folder
 "SecretStoriesUI". The project code should now be viewable in Android Studio and folders and classes 
sorted under "app" and "Gradle Scripts". The app requires Sdk version 21 and maximum version 27. You 
may be prompted to install missing Sdk packages to run the project. Click the prompt and the required 
packages should be automatically downloaded and installed. You may need to clean the project after 
this, Build -> Clean Project. You can run the app either on an emulator or on your phone through usb if 
it uses min Android 5.0(Lollipop) and max Android 8.1.0(Oreo). Press Run(Shift + F10) and select your 
preferred platform. To install and run the app on your phone you may need to enable developer options 
and debugging. Please refer to the this link for directions: https://developer.android.com/studio/debug/dev-options

## Usage

### Main
The app uses bottom navigation to move between the Home, Chats, Contacts, Search and Notifications 
views. Other than the "To Login"-button on the Home screen, there is currently no additional 
functionality to any buttons. When pressing the "To Login"-button, a login screen is opened. 

#### Login
Here, email and password can be entered to attept login. Press "Create new account" to open the 
registration window. 
To login use Email: username@email.com Password: password

#### Create Account
Here user information can be entered. Currently this information is not stored. Both
"CREATE ACCOUNT" and "Already have an Account? Login" takes you back to the login screen.
 

## Trouble shooting

If the projects folders do not sort themselves properly: 
1. Press "Clean Project", Build -> Clean Project.  
2. Press"Sync Project with Gradle Files" in the top right corner. 
3. If problem is still not solved, press "Rebuild Project", Build -> Rebuild Project
4. If the problem persists try removing the project, placing the project folder into a different 
directory. Now try opening it with  "Open an existing Android Studio Project" from the Android Studio 
launcher.
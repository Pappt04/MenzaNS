## MenzaNS - Novi Sad Token Tracking Android Application for Students (README.md)
![MenzaNSGithublogo](https://github.com/user-attachments/assets/cafd796c-a697-4296-b053-bb4672d69298)

**MenzaNS** is a mobile application for students at the University of Novi Sad (UNS) that simplifies token tracking from student canteens ("Menza"). The app utilizes Google's Geofencing API to automate token management based on your location.

### Features

* **Automatic Token Management:** Leverage Geofencing to automatically manage your tokens when you're near a menza. (**Requires Google Maps Platform access**)
* **Optional ISIC Card Storage:** Securely store your ISIC card information for validity checks.
* **User-friendly Interface:** Easily access menus, and track your token balance.

### Installation

**Please note:** Currently, the automatic token management feature requires access to Google Maps Platform. 

1. Download the MenzaNS app from the relevant app store (Play Store or App Store).
2. (Optional) If using the Google Maps Platform for automatic token management, follow to settings to aquire the needed Android platform permissions for the automatic tracking.

### Usage

1. Launch the MenzaNS app.
2. (Optional) Enter your ISIC card information for validity checks.
3. When you're near a menza, the app will automatically detect your location through Geofencing and manage your tokens (if enabled).

### Technology Stack

* Mobile development framework: Kotlin with Jepack compose
* Google Maps Platform (optional), with Geofencing API
* Secure storage for ISIC card information (optional)


### Contributing

We welcome contributions to the MenzaNS project! If you'd like to contribute, please refer to the CONTRIBUTING.md file (coming soon) for guidelines.

### License

For the moment there is not any License active for this project

### Disclaimer

This application is not affiliated with the University of Novi Sad or any student canteen provider. It is entirely run by students for students

### Contact

For any questions or feedback, please feel free to contact us at papptamas2004@gmail.com.

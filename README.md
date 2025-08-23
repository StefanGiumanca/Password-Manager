# 🔐 Password Manager

**Password Manager** is a Java-based desktop application built with JavaFX that helps users securely store, encrypt, and manage their passwords locally.
The main goal of the app is to provide a simple and secure way to organize account credentials without relying on cloud storage or external databases.
🌟 Ideal for personal use, this application ensures that your sensitive data remains encrypted and accessible only to you.

## 🔧 Features

Here’s what the Password Manager app can do:

- 📝 **Add new accounts** — Store credentials (site, username, password) securely.
- 🔐 **Encrypt passwords** — Passwords are stored encrypted in a local SQLite database using AES encryption.
- 👁️ **Show/Hide password** — Toggle password visibility while typing.
- 📄 **View saved accounts** — List of all saved accounts shown in a clear ListView.
- ❌ **Delete one or more accounts** — Select and remove accounts from the database.
- 🔄 **Real-time encryption demo** — Select an account to instantly see its encrypted password.
- 🔑 **Access authentication** — Password prompt on app launch to ensure privacy.
- 💾 **Local persistence** — All data is saved locally; no internet connection needed.
- 📦 **Exportable JAR** — Delivered as a `.jar` file, runnable with JavaFX support.

 ## 🧰 Technologies Used

This project is built using:

- 💻 **Java 23 (JDK 23)** — Core programming language.
- 🎨 **JavaFX** — Used for building the user interface.
- 🗃️ **SQLite** — Lightweight local database for storing encrypted account data.
- 🔐 **AES Encryption** — Custom utility for encrypting and decrypting passwords.
- 🖥️ **IntelliJ IDEA** — Primary IDE used for development.

## 📦 How to Run (.jar version)

### 🔧 Requirements:
- Java 17 or later installed (✅ tested on JDK 23)
- JavaFX SDK installed and configured

### ▶️ Run the application:

**On macOS/Linux:**
```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar Password-Manager.jar
```
**On Windows:**
```bash
java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -jar Password-Manager.jar
```
Replace /path/to/javafx-sdk/lib with the path to your local JavaFX SDK installation.

## 📸 Screenshots

Here are some screenshots of the application in action:

<img width="946" height="526" alt="Screenshot 2025-08-23 at 18 56 40" src="https://github.com/user-attachments/assets/0a08bc70-3ae5-4509-943c-f67752a23b2b" />

<img width="945" height="524" alt="Screenshot 2025-08-23 at 18 57 04" src="https://github.com/user-attachments/assets/83f77091-0179-4d87-b141-ef750b585a00" />

<img width="942" height="519" alt="Screenshot 2025-08-23 at 18 57 20" src="https://github.com/user-attachments/assets/5c4a21b1-dd56-449c-9004-d35a6668865a" />




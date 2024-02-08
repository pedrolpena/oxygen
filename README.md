# Oxygen Titration Program for Linux, Windows, and OSX

## Download

**Windows Release:** [Download the latest release for Windows here](https://github.com/pedrolpena/oxygen/releases/latest)

## Quick Compile and Install Guide for Debian Bookworm
### Prerequisites

Ensure `openjdk-17-jdk` is available on your system.

### Installation Steps

1. **Install Dependencies**
   
   Open a terminal and execute the following commands to install the necessary packages:

   ```
   sudo apt-get install openjdk-17-jdk git make
   ```

2. **Clone Repository and Compile**

   ```
   git clone https://github.com/pedrolpena/oxygen.git
   cd oxygen
   make clean
   make
   ```

3. **Install Application**

   ```
   sudo make install
   ```

4. **Configure User Permissions**

   To access serial ports, your user needs to be in the `dialout` group:

   ```
   sudo usermod -a -G dialout $USER
   ```

   **Note:** You must log out and then log back in for the group change to take effect. Verify by typing `groups` in the terminal. If `dialout` is not listed, reboot your system and check again.

### Running the Program

- Start the program via the menu icon. If it doesn't start, use the terminal command:

  ```
  java -jar /usr/lib/aomloxygentitrator/AOMLOxygenTitrator.jar
  ```


## Permissions to Access the Serial Port

### Under Windows

Serial ports should be available to the user by default.

### Under Linux (Ubuntu 14.04 and Up)

Users must be part of the `dialout` group to access serial ports. Add your user to the `dialout` group with:

```
sudo usermod -a -G dialout $USER
```

After adding, log out and log back in. Use `groups` to verify that `dialout` is listed among the user's groups.

## Screenshots
![Screenshot_2024-02-08_09-46-28](https://github.com/ExplodingTuna/oxygen/assets/146979376/154fae6d-263a-46d9-bb84-7b26c32d4132)

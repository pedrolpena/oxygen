# Oxygen Titration Program for Linux, Windows, and OSX

## Download

**Windows Release:** [Download the latest release for Windows here](https://github.com/pedrolpena/oxygen/releases/latest)

## Quick Compile and Install Guide for Ubuntu 18.04

### Prerequisites

Ensure `openjdk-11-jdk` is available on your system. If not, you may use `openjdk-8-jdk` as an alternative.

### Installation Steps

1. **Install Dependencies**
   
   Open a terminal and execute the following commands to install the necessary packages:

   ```
   sudo apt-get install openjdk-11-jdk librxtx-java git make
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

- **Common Error and Solution:**

  If you encounter the error `java.lang.NullPointerException thrown while loading gnu.io.RXTXCommDriver`, start the program with the following command:

  ```
  java -Djava.ext.dirs -jar /usr/lib/aomloxygentitrator/AOMLOxygenTitrator.jar
  ```

  After installation, go to the configuration tab, choose, and connect a serial port for the fields to become active.

## Installing RXTX Under Linux

### Ubuntu 14.04 and Up

- Install `librxtx-java` using:

  ```
  sudo apt-get update
  sudo apt-get install librxtx-java
  ```

### Fedora 22 and Similar Distributions

1. **Install RXTX**

   For Fedora (use `dnf` as `yum` is deprecated):

   ```
   sudo dnf install rxtx
   ```

2. **Fix Library Path**

   Fedora might place the library in a different directory. Create a symbolic link to the correct path:

   - **64-bit Fedora:**

     ```
     sudo ln -s /usr/lib64/rxtx/librxtxSerial-2.2pre1.so /usr/lib/librxtxSerial.so
     ```

   - **32-bit Fedora:**

     ```
     sudo ln -s /usr/lib/rxtx/librxtxSerial-2.2pre1.so /usr/lib/librxtxSerial.so
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
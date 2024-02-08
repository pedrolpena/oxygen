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

Start the program via the menu icon. If it doesn't start, use the terminal command:

  ```
  java -jar /usr/lib/aomloxygentitrator/AOMLOxygenTitrator.jar
  ```
  
## Screenshots
![image](https://github.com/ExplodingTuna/oxygen/assets/146979376/1a30256d-6c2f-4347-94a4-274bec02ecdc)

![image](https://github.com/ExplodingTuna/oxygen/assets/146979376/8e5462f7-a3b3-4839-b7cb-a3142c59892a)


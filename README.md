# Oxygen Titration Program for Linux, Windows, and OSX

## Disclaimer
This repository is a scientific product and is not official communication of the National Oceanic and Atmospheric Administration, or the United States Department of Commerce. All NOAA GitHub project code is provided on an ‘as is’ basis and the user assumes responsibility for its use. Any claims against the Department of Commerce or Department of Commerce bureaus stemming from the use of this GitHub project will be governed by all applicable Federal law. Any reference to specific commercial products, processes, or services by service mark, trademark, manufacturer, or otherwise, does not constitute or imply their endorsement, recommendation or favoring by the Department of Commerce. The Department of Commerce seal and logo, or the seal and logo of a DOC bureau, shall not be used in any manner to imply endorsement of any commercial product or activity by DOC or the United States Government.

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
![oxygen_program_main_tab](https://github.com/user-attachments/assets/b4936d56-a44d-4185-b1c0-a938ee999ea4)

![oxygen_program_configuration_tab](https://github.com/user-attachments/assets/70470590-17ef-46e0-9028-c8b5f8ce8fb2)


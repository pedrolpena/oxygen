# Oxygen titration program for Linux Windows and OSX

**Download Windows release with the link below**

https://github.com/pedrolpena/oxygen/releases/latest


------------------------------------------------
**QUICK COMPILE AND INSTALL UNDER UBUNTU 18.04**
------------------------------------------------
Open a terminal and copy and paste the steps below to download, compile and install the O2 program.
If openjdk-11-jdk isnt available, try openjdk-8-jdk instead.

There is a test bottle volumes file included in the folder. you'll need this for testing.

```BASH
sudo apt-get install openjdk-11-jdk librxtx-java git make
git clone https://github.com/pedrolpena/oxygen.git
cd oxygen
make clean
make
sudo make install
sudo usermod -a -G dialout $USER
```

The O2 should  now be installed and it should be able to run. 
You need to be in the dialout group to be able to access serial ports. The
last line takes care of this.

Now you have to log out and log back into your session. You can check to see
if the dialout group was added by opening a terminal and typing groups.
groups displays the groups you're a member of. If you don't see the dialout group,
then reboot and try again.

```bash
groups
```
You can start the program from the menu icon now. If it doesn't start, open a terminal and type

```bash
java -jar /usr/lib/aomloxygentitrator/AOMLOxygenTitrator.jar 
```

You'll probably see the error

-----------------------------------------------------------------------------
**java.lang.NullPointerException thrown while loading gnu.io.RXTXCommDriver**
-----------------------------------------------------------------------------

On newer versions of java sometimes an exception is thrown and no serial ports are listed.
```
java.lang.NullPointerException thrown while loading gnu.io.RXTXCommDriver
```

You can start the program with 

```bash
java -Djava.ext.dirs -jar /usr/lib/aomloxygentitrator/AOMLOxygenTitrator.jar


```
Once it's installed you'll have to go to the configuration tab choose and connect a serial port for the fields to become active 


-------------------------------
**INSTALLING RXTX UNDER LINUX**
-------------------------------

-Ubuntu 14.04 and up-

The distribution you are using should have this available via one of its repositories.
under ubuntu 14.04 open a terminal and type<br>
```bash
sudo apt-get update
sudo apt-get install librxtx-java
```

-Fedora 22-<br>

The following instructions worked under fedora 22 and should work
for redhat & centos(may have to add the epel repository first)<br>
```bash
sudo yum install rxtx 
```
(yum has been deprecated and replaced by dnf)<br>
```bash
sudo dnf install rxtx
```


The serial library needs to be "/usr/lib" in order for the program
to work. For whatever reason, fedora places the library in "/usr/lib64/rxtx" or "/usr/lib/rxtx"
To fix this, create symlink "/usr/lib/librxtxSerial.so" that points to 
the nstalled library.

Under 64 bit Fedora create a sym link to the rxtx serial library.
Note that the version number of the library can change so look for a file
in the form of "librxtxSerial-VERSION.so"<br>

```bash
sudo ln -s /usr/lib64/rxtx/librxtxSerial-2.2pre1.so /usr/lib/librxtxSerial.so
```

Under 32 bit Fedora create a sym link to the rxtx serial library.
Note that the version number of the library can change so look for a file
in the form of "librxtxSerial-VERSION.so"<br>

```bash
sudo ln -s /usr/lib/rxtx/librxtxSerial-2.2pre1.so /usr/lib/librxtxSerial.so
```

-----------------------------------------
**PERMISSIONS TO ACCESS THE SERIAL PORT**
-----------------------------------------

-----------------
**UNDER WINDOWS**
-----------------
The serial ports should already be available for use by the user.

---------------
**UNDER LINUX**
---------------

-Ubuntu 14.04 and up-
On ubuntu and other distributions, the user running the program
will not have access to serial ports unless the user is part of the
"dialout" group.

For example, to add user aardvark to the dialout group, open a terminal and type
sudo usermod -a -G dialout aardvark (replace aardvark with the username)
or issue this command for the current user (easier)

```bash
sudo usermod -a -G dialout $USER
```


logout and log back in. 

To list groups the current user is part of, type

groups<br>

dialout should be in the list.<br>







-----------------------------------------------------------------------------
**java.lang.NullPointerException thrown while loading gnu.io.RXTXCommDriver**
-----------------------------------------------------------------------------

On newer versions of java sometimes an exception is thrown and no serial ports are listed.
```
java.lang.NullPointerException thrown while loading gnu.io.RXTXCommDriver
```

You can start the program with 

```
java -Djava.ext.dirs -jar AOMLOxygenTitrator.jar
```

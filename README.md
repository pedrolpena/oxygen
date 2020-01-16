# Oxygen titration program

Windows release

https://github.com/pedrolpena/oxygen/releases/latest

This program also runs on linux. You need to install java and librxtx.
Make sure the user is in the dialout group.

to compile you need ant and a jdk or
make and a jdk

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
java -Djava.ext.dirs -jar IESTelemetry.jar
```

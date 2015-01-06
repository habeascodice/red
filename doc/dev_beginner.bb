[h2]You want to contribute to the Red Matrix?[/h2]
[b]...and don't know really how to start?
Then this is for you...[/b]

You want to lean how to:
[list]
[*] debug the Red Matrix,
[*] contribute code to the project,
[*] optionally - do it all from inside a virtual machine?
[/list]

This manual was tested for
[list]
[*] Lubuntu Linux as host OS
[*] Debian as guest OS running in a VM
[/list]

[h2]Content[/h2]

In this manual you will
[list=1]
[*] Install a Virtual Machine (optional)
[*] Install Apache Web Server
[*] Install PHP, MySQL, phpMyAdmin
[*] Fork the project on github to be able contribute
[*] Install the RED Matrix
[*] Debug the server via eclipse
[*] Contribute your changes via github
[/list]

[h2]Install a Virtual Machine (optional)[/h2]

[h3]Install KVM - Kernel-based Virtual Machine[/h3]

[url=https://wiki.debian.org/KVM]Hier[/url] die Anleitung für Linux Debian.
Zusammenfassung der Anleitung (Schritt-für-Schritt für Linux):
[list=1]
[*] KVM installieren
[code]# sudo apt-get install qemu-kvm libvirt-bin[/code]
[*] Sich selbst zur Gruppe libvirt hinzufügen [code]# sudo adduser <youruser> libvirt[/code]
[*] Grafische Oberfläche zur Verwaltung (Installation, Konfiguration,...) von virtuellen Maschinen installieren [code]# sudo apt-get install virt-manager[/code]
[*] Betriebssystem der Wahl herunterladen, das man halt später in der virtuelle Machine laufen lassen will zum Beispiel Linux Mint Debian oder debian ([url=http://ftp.nl.debian.org/debian/dists/wheezy/main/installer-amd64/current/images/netboot/mini.iso]mini.iso[/url])
[*] Den Virt-Manager starten
- Neue virtuelle Maschine anlegen (Klick auf Icon)
- Als Installations-Medium das ISO-Image wählen, dass im Schritt vorher herunter geladen wurde, in unserem Fall Linux Mint Debian oder Lubuntu für ältere Rechner
- Optional: Virtuelle Maschine konfigurieren, genauer: Wieviel Hauptspeicher darf sie benutzen, wieviele CPUs,...
- Virtuelle Maschine starten. Ergebnis: Linux Mint startet in einem eignen Fenster. Dort kann jetzt im Internet gesurft werden, ohne das der "eigene" Computer verseucht wird.
[*] (optional) Netzwerkfehler nach Neustart des Wirts vermeiden
[code]# virsh net-start default
# virsh net-autostart default[/code]
[/list]

[h3]Install Debian Linux in the VM[/h3]

Download an ISO image to install the current Debian [url=https://www.debian.org/CD/netinst/]here[/url]. Choose "amd64" for a consumer computer / notebook.

Open the Virtual Machine Manager, create a new VM and install Debian from the ISO image you download just befor.


[h2]Install Apache Webserver[/h2]

Make yourself root
[code]su -l[/code]

Create the standard group for the Apache webserver
[code]groupadd www-data[/code]
might exist already

[code]usermod -a -G www-data www-data[/code]

Check if the system is really up to date
[code]apt-get update[/code]

Optional restart services after installation
[code]reboot[/code]

If you restarted, make yourself root
[code]su -l[/code]

Install Apache: [code]
apt-get install apache2 apache2-doc apache2-utils[/code]

Open webbrowser on PC and check [url=localhost]localhost[/url]
Should show you a page like "It works"

(Source [url=http://www.manfred-steger.de/tuts/20-der-eigene-webserver-mit-dem-raspberry-pi#]http://www.manfred-steger.de/tuts/20-der-eigene-webserver-mit-dem-raspberry-pi#[/url])


[h2]Install PHP, MaySQL, phpMyAdmin[/h2]

[code]su -l
apt-get install libapache2-mod-php5 php5 php-pear php5-xcache php5-curl php5-mcrypt php5-xdebug
apt-get install php5-mysql
apt-get install mysql-server mysql-client[/code]
enter and note the mysql passwort

Optional since its already enabled during phpmyadmin setup
[code]
php5enmod mcrypt
[/code]

Install php myadmin
[code]apt-get install phpmyadmin[/code]

Configuring phpmyadmin
- Select apache2 (hint: use the tab key to select)
- Configure database for phpmyadmin with dbconfig-common?: Choose Yes

(Source #^[url=http://www.manfred-steger.de/tuts/20-der-eigene-webserver-mit-dem-raspberry-pi#]http://www.manfred-steger.de/tuts/20-der-eigene-webserver-mit-dem-raspberry-pi#[/url])

[b]Enable rewrite[/b]

The default installation of Apache2 comes with mod_rewrite installed. To check whether this is the case, verify the existence of /etc/apache2/mods-available/rewrite.load

[code]
pi@pi /var/www $ nano /etc/apache2/mods-available/rewrite.load
[/code]

 (You should find the contendt: LoadModule rewrite_module /usr/lib/apache2/modules/mod_rewrite.so)
To enable and load mod_rewrite, do the rest of steps.
Create a symbolic link in /etc/apache2/mods-enabled

[code]
cd /var/www
pi@pi /var/www $ a2enmod rewrite
[/code]

Then open up the following file, and replace every occurrence of "AllowOverride None" with "AllowOverride all".

[code]
pi@pi /var/www $nano /etc/apache2/apache2.conf
[/code]
or
[code]
root@debian:/var# gedit /etc/apache2/sites-enabled/000-default 
[/code]

Finally, restart Apache2.

[code]
pi@pi /var/www $service apache2 restart
[/code]

[b]Test installation[/b]

[code]cd /var/www[/code]

create a php file to test the php installation[code]sudo nano phpinfo.php[/code]

Insert into the file:
[code]
<?php
  phpinfo();
?>
[/code]
(save CTRL+0, ENTER, CTRL+X)

open webbrowser on PC and try #^[url=http://localhost/phpinfo.php]http://localhost/phpinfo.php[/url] (page shows infos on php)

connect phpMyAdmin with MySQL database [code]nano /etc/apache2/apache2.conf
[/code]
- CTRL+V... to the end of the file
- Insert at the end of the file:  (save CTRL+0, ENTER, CTRL+X)[code]Include /etc/phpmyadmin/apache.conf[/code]

restart apache
[code]/etc/init.d/apache2 restart
apt-get update
apt-get upgrade
reboot[/code]

(Source #^[url=http://www.manfred-steger.de/tuts/20-der-eigene-webserver-mit-dem-raspberry-pi#]http://www.manfred-steger.de/tuts/20-der-eigene-webserver-mit-dem-raspberry-pi#[/url])


[b]phpMyAdmin[/b]

open webbrowser on PC and try #^[url=http://localhost/phpmyadmin]http://localhost/phpmyadmin[/url]

(Source #^[url=http://www.manfred-steger.de/tuts/20-der-eigene-webserver-mit-dem-raspberry-pi#]http://www.manfred-steger.de/tuts/20-der-eigene-webserver-mit-dem-raspberry-pi#[/url])


[b]Create an empty database... that is later used by RED[/b]

open webbrowser on PC and try #^[url=http://localhost/phpmyadmin]http://localhost/phpmyadmin[/url]

Create an empty database, for example named "red".
Create a database user, for example "red".
Grand all rights for the user "red" to the database "red".

Note the access details (hostname, username, password, database name).


[h2]Fork the project on github to be able contribute[/h2]

Please follow the instruction in offiical [url=http://git-scm.com/book/en/v2/GitHub-Contributing-to-a-Project] documentation[/url] of git.
It is a good idea to read the whole manual! Git is different to other version control systems in many ways.

You should
[list]
[*] create an account at github.com
[*] fork https://github.com/friendica/red
[*] fork https://github.com/friendica/red-addons
[/list]


[h2]Install RED and its Addons[/h2]

You should have created an account on github and forked the projects befor you procced.

Delete the directory www
[code]pi@pi /var/www/html $ cd ..
rm -R www/
[/code]

Install git (and optionally git-gui a client gui)
[code]apt-get install git git-gui[/code]

Download the main project red and red-addons
[code]
root@debian:/var# git clone https://github.com/einervonvielen/red www
root@debian:/var# cd www/
root@debian:/var/www# git clone https://github.com/einervonvielen/red-addons addon
[/code]

Make this extra folder
[code]
root@debian:/var/www# mkdir -p "store/[data]/smarty3"
[/code]

Create .htconfig.php and make it writable by the webserver
[code]
root@debian:/var# cd www/
root@debian:/var/www# chmod ou+w .htconfig.php
[/code]

Make user www-data (webserver) is the owner all the project files
[code]
root@debian:/var/www# cd ..
root@debian:/var# chown -R www-data:www-data www/
[/code]

Add yourself ("surfer" in this example) to the group www-data. Why? Later you want to modify files in eclipse or in another editor.
Then make all files writable by the group www-date you are now a member of.
[code]
root@debian:/var/www# usermod -G www-data surfer
root@debian:/var# chmod -R  g+w www/
[/code]

Restart the computer (or vm)
If you are still not able to modify the project files you can check the members of the group www-data with
[code]
cat /etc/group
[/code]

Open http://localhost and init the matrix

Befor you register a first user switch of the registration mails.
Open /var/www/.htconfig.php
and make sure "0" is set in this line
[code]
$a->config['system']['verify_email'] = 0;
[/code]
This should be able to change the file as "yourself" (instead of using root or www-data).


Run the poller  to pick up the recent "public" postings of your friends
Set up a cron job or scheduled task to run the poller once every 5-10
minutes to pick up the recent "public" postings of your friends

[code]
sudo crontab -e
[/code]

Add
[code]
*/10 * * * * cd /var/www/; /usr/bin/php include/poller.php
[/code]

If you don't know the path to PHP type
[code]
sudo whereis php
[/code]


[h2]Debug the server via eclipse[/h2]

[h3]Check the configuration of xdebug[/h3]

You shoud have installed xdebug befor
[code]
sudo apt-get install php5-xdebug
[/code]

Configuring Xdebug

Open your terminal and type as root (su -l)
[code]
gedit /etc/php5/mods-available/xdebug.ini
[/code]

if the file is empty try this location
[code]
gedit /etc/php5/conf.d/xdebug.ini
[/code]

That command should open the text editor gedit with the Xdebug configuration file
At the end of the file content append the following text

xdebug.remote_enable=on
xdebug.remote_handler=dbgp
xdebug.remote_host=localhost
xdebug.remote_port=9000

Save changes and close the editor.
In you terminal type to restart the web server.
[code]
sudo service apache2 restart
[/code]


[h3]Install Eclipse and start debugging[/h3]

Install eclipse.
Start eclipse with default worspace (or as you like)

Install the PHP plugin
Menu > Help > Install new software...
Install "PHP Developnent Tools ..."

Menu > Window > Preferences...
> General > Webbrowser > Change to "Use external web browser"
> PHP > Debug > Debug Settings > PHP Debugger > Change to "XDebug"

Menu > File > New Project > Choose PHP > "PHP Project"
> Choose Create project at existing location and "/var/www"

Open index.php and "Debug as..."
Choose as Launch URL: "http://localhost/"

Expected:
[list]
[*] The web browser starts
[*] The debugger will stop at the first php line
[/list]


[h2]Contribute your changes via github[/h2]

(There is a related page in this docs: [zrl=[baseurl]/help/git_for_non_developers]Git for Non-Developers[/zrl])
As stated befor it is recommended to read the official documentation [url=http://git-scm.com/book/en/v2/GitHub-Contributing-to-a-Project]GitHub-Contributing-to-a-Project[/url] of git.

Make sure you have set your data
[code]
surfer@debian:/var/www$ git config --global user.name "Your Name"
surfer@debian:/var/www$ git config --global user.email "your@mail.com"
[/code]



Create a descriptive topic branch
[code]
surfer@debian:/var/www$ git checkout -b doc_dev_beginning
[/code]

Make your changes. In this example it is a new doc file.

Check your modifications
[code]
surfer@debian:/var/www$ git status
[/code]

Add (stage) the new file
[code]
surfer@debian:/var/www$ git add doc/nb-no/dev_beginner.bb
[/code]

Commit the changes to your local branch
[code]
surfer@debian:/var/www$ git commit
[/code]

Make sure your local repository is up-to-date with the main project.
Add the original repository as a remote named “upstream” if not done yet
[code]
surfer@debian:/var/www$ git remote add upstream https://github.com/einervonvielen/red
[/code]

Fetch the newest work from that remote
[code]
surfer@debian:/var/www$ git fetch upstream
[/code]


#include doc/macros/main_footer.bb;
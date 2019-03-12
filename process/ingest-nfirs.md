1. Upload data
   From local data source
   ```
   scp NFIRS.zip <user>@<vm-ip>:~
   ```
2. Unzip data
   Install unzip with `sudo apt-get install -y unzip`
   ```
   cd ~
   mkdir NFIRS
   unzip -d NFIRS/ NFIRS.zip
   ```

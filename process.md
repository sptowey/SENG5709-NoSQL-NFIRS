# 1. Acquire data
1. Download all NFIRS data from [Enigma](https://public.enigma.com/browse/collection/national-fire-incident-reporting-system-nfirs/77c6597b-eb82-4a2f-9d2e-5aedea1cae1d)
   - 105 files, 7.56 GB  
2. Clean file names - remove prefixed GUID and `NFIRS-`
   - Manually removed GUID on download  
   - Run powershell script to remove `NFIRS-` - `get-childitem *.csv  | foreach { rename-item $_ $_.Name.Replace("NFIRS-", "") }` [source](https://superuser.com/questions/236820/how-do-i-remove-the-same-part-of-a-file-name-for-many-files-in-windows-7)  
3. Zip files and save to team's Google Drive to share

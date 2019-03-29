import os
import pandas as pd

def pad8(text):
    return str(text).zfill(8)

def pad12(text):
    return str(text).zfill(12)

#change base_dir for your setup!
base_dir = '~/druid/NFIRS/'
years = ['2009','2010','2011']
file_name = 'DetailedIncidentInformation'
file_ext = '.csv'
count = 0

for year in years:
    full_file_no_ext = os.path.join(base_dir, file_name+year)
    print('Processing {0}'.format(full_file_no_ext))

    df = pd.read_csv(full_file_no_ext+file_ext)
    df['inc_date'] = df['inc_date'].map(pad8)
    df.to_csv(full_file_no_ext + '-Pad' + file_ext)
    count = count + 1
print('Completed ' + '{}'.format(count) + ' files')

import os
import pandas as pd

def pad8(text):
    return str(text).zfill(8)

def pad12(text):
    return str(text).zfill(12)

base_dir = '~/NFIRS/'
years = ['2009','2010','2011']
file_name = 'GeneralIncidentInformation'
file_ext = '.csv'

for year in years:
    full_file_no_ext = os.path.join(base_dir, file_name+year)
    print('Processing {0}'.format(full_file_no_ext))

    df = pd.read_csv(full_file_no_ext+file_ext)
    
    df['inc_date'] = df['inc_date'].map(pad8)
    df['alarm'] = df['alarm'].map(pad12)
    df['arrival'] = df['arrival'].map(pad12)
    df['inc_cont'] = df['inc_cont'].map(pad12)
    df['lu_clear'] = df['lu_clear'].map(pad12)
    
    df.to_csv(full_file_no_ext + '-Pad' + file_ext)

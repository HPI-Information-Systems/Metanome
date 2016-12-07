#!/usr/bin/env python3

import argparse
import os
import glob
import urllib
import re


import requests

# Define API Endpoints for Algorithms and Files
URL_ALGORITHMS = '/api/algorithms/store'
URL_INPUTFILE = '/api/file-inputs/store'

def send(file,url):
        # Check if url is valid (includes http)
        if not re.match(r'http(s?)\:', url):
            url = 'http://' + url


        print("File {0} uploaded using URL {1}\n".format(file,url))
        m = { 'file': open(file,'rb')}

        r = requests.post(url, files=m)
        print("Returned Status code: {0}\n".format(r.status_code))


def main():
    parser = argparse.ArgumentParser(description='Uploads Algorithm Jars or InputFiles to the Metanome platform')
    parser.add_argument('-s','--source', required=True,
                    help='File or Directory to be uploaded to the Metanome Backend')
    parser.add_argument('-ip','--ip',  required=True,
                    help='IP and Port of Metanome Backend')
    parser.add_argument('-t','--type',required=True, choices=['algorithm', 'inputfile'],help='Type of the file/directory uploaded {algorithm|inputfile}')
    args = parser.parse_args()

    if args.type == 'algorithm':
        api_endpoint = URL_ALGORITHMS
    else:
        api_endpoint = URL_INPUTFILE

    if os.path.isdir(args.source):
        print("Sending file(s) in directory {0} to IP {1}".format(args.source,args.ip))
        files = [f for f in os.listdir(args.source) if  os.path.isfile(os.path.join(args.source,f))]
        for file in files:
            send(os.path.join(args.source,file),args.ip + api_endpoint)

    else:
        print("Sending file {0} to IP {1}".format(args.source,args.ip))
        send(args.source,args.ip + api_endpoint)

if __name__ == "__main__":
    main()

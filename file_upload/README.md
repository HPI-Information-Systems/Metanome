## Remote File Upload for Metanome

This python script can be used to upload algorithms JARs or input files / directories to a running Metanome Backend instance. 
It has to be used if the backend is hosted on a remote server were adding file by copying them into the source directories is not possible.

The Python script requires following tools/libraries:

* [Pip](https://pip.pypa.io/en/stable/)
* [Requests 2.x](http://docs.python-requests.org/en/master/#)

To install the required library execute:

```pip install -r requirements.txt```


### Supported Arguments
* -s/--source — Directory or File to upload
* -ip/--ip — IP and Port of the Metanome Backend (Defaul Port: 8081)
* -t/--type — File uploaded is an {algorithm|inputfile} 

All arguments are required for the file upload to the Metanome Backend.

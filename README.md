# object-store-demo-java

Output Sample 

Authenticate needs to run first. Once the Auth Token is received, it is saved as a variable for use along with other REST API Commands
X-Storage-Url: http://hln2329p:8080/v1/AUTH_companyA
X-Auth-Token: AUTH_tkdec160e433e046f384bebfa4afe622dc
Connection: keep-alive
X-Storage-Token: AUTH_tkdec160e433e046f384bebfa4afe622dc
X-Auth-Token-Expires: 84434
Content-Length: 83
X-Trans-Id: txf169c12db4104177bacf7-005951ca18
Date: Tue, 27 Jun 2017 02:59:36 GMT
Content-Type: application/json




Sending 'GET' request to URL : http://hln2329p:8080/v1/AUTH_companyA
Response Code : 200
Because I only listed account as part of the url, it will display all the countainers within
the account. If you provide the account/container, it will list all the objects within the container
cars
current
dispersion_objects
folder1
folder2
read_folder
read_folder-T
test_folder
testfolder
testfolder2
versions




Sending 'PUT' request to URL : http://hln2329p:8080/v1/AUTH_companyA/folder1/testA
Response Code : 201
Etag: a2ed931ecace715a6e61fb0ca273480a
Connection: keep-alive
Last-Modified: Tue, 27 Jun 2017 02:59:37 GMT
Content-Length: 0
X-Trans-Id: txb35cb8ff77554c3a9175f-005951ca18
Date: Tue, 27 Jun 2017 02:59:36 GMT
Content-Type: text/html; charset=UTF-8




Sending 'POST' request to URL : http://hln2329p:8080/v1/AUTH_companyA/folder1/testA
Response Code : 202
POST is responsible for updating/creating metadata for a container/account/object.
 The updated metadata can be seen in the next command, which is head. In this command, I made two custom metadata, 'X-Object-Meta-Brand: BMW' 
and 'X-Object-Meta-Model: x3'. Reference the HEAD request to see an update

Connection: keep-alive
Content-Length: 76
X-Trans-Id: tx8bac4fc2231949e5b4e0a-005951ca18
Date: Tue, 27 Jun 2017 02:59:36 GMT
Content-Type: text/html; charset=UTF-8




Sending 'HEAD' request to URL : http://hln2329p:8080/v1/AUTH_companyA/folder1/testA
Response Code : 200
X-Timestamp: 1498532376.35903
Accept-Ranges: bytes
Etag: a2ed931ecace715a6e61fb0ca273480a
X-Object-Meta-Brand: BMW
Connection: keep-alive
Last-Modified: Tue, 27 Jun 2017 02:59:37 GMT
Content-Length: 16
X-Trans-Id: tx873ca5abf9f04e9eafd70-005951ca18
X-Object-Meta-Model: x3
Date: Tue, 27 Jun 2017 02:59:36 GMT
Content-Type: text/plain


# How to build and Install lib


cd build 
cmake .. # by using 'cmake -DCMAKE_INSTALL_PREFIX=/usr ..' we can change the install directory.
make 
sudo make install # to avoid the permission issue, it will install the headers and libs to /usr/local by default.
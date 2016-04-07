/*
A Webserver normally include 4 parts:
	1. Firstly, a dao is needed to encapsulate your object from your sql server or others;
	2. Secondly, a dao have to connect to the sql server, so, a util-DBHelper is needed;
	3. An entity is needed to realize the operations to get or set attributes of your object,
	such as a student object need to have a method to get or set its student id;
	4. Servlet, as its name tells, it should provide the basic operation of your http server,
	like, post() after it receive a request from the client;

	Progress:
	
	client requset <- servlet  <-  entity  <- dao <-   util <- sql server
	
*/
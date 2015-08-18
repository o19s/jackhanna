# jackhanna
Simple CLI for Zookeeper

Why do we need another CLI for Zookeeper you ask?  

I was bitten by the fact that the CLI that ships with Zookeeper, `zkCli.sh` is jsut one character different then the one that ships with Solr, `zkcli.sh`.  Several hours of debugging later, I decided to write my own!  I wanted to use the same commands I use in Unix, `ls`, `cat`, `rm`, `du` etc to work with data in Zookeeper.

There isn't a good one out there that is just based on Java, you need Go or Python or Scala as dependencies.  I wanted to build a simple one that would allow the basic Unix style operators to work with Zookeeper.

The original version of this was based on the Solr client, and then I ported it to Curator.


## Examples of using the CLI

List out nodes:
`
java -jar jackhanna.jar localhost:2181 ls /environment
java -jar jackhanna.jar localhost:2181 ls --zkPath=/environment
`

View the contents of a node:
`
java -jar jackhanna.jar localhost:2181 cat /environment/somevalue
java -jar jackhanna.jar localhost:2181 cat --zkPath=/environment/somevalue
`

List out the disk usage in Zookeeper:
`
java -jar jackhanna.jar localhost:2181 du /environment
java -jar jackhanna.jar localhost:2181 du --zkPath=/environment
`

Delete nodes in Zookeeper:
`
java -jar jackhanna.jar localhost:2181 rm /environment/properties
java -jar jackhanna.jar localhost:2181 rm --zkPath=/environment/properties
`

Put a single file into Zookeeper:
`
java -jar jackhanna.jar localhost:2181 put --zkPath /environment/config/log4j.xml --path ./src/main/resources/log4j.xml
`

Put a directory of files into Zookeeper:
`
java -jar jackhanna.jar localhost:2181 put --zkPath /environment/configs/resources --path ./src/main/resources
`


Get a single file from Zookeeper:
`
java -jar jackhanna.jar localhost:2181 get --zkPath /environment/config/log4j.xml --path /tmp
`

Get a directory of files from Zookeeper:
`
java -jar jackhanna.jar localhost:2181 get --zkPath /environment/configs/resources --path /tmp
`

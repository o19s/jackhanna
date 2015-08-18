# jackhanna
Simple CLI for Zookeeper

Why do we need another CLI for Zookeeper you ask?  

I was bitten by the fact that the CLI that ships with Zookeeper, `zkCli.sh` is jsut one character different then the one that ships with Solr, `zkcli.sh`.  Several hours of debugging later, I decided to write my own!  I wanted to use the same commands I use in Unix, `ls`, `cat`, `rm`, `du` etc to work with data in Zookeeper.

There isn't a good one out there that is just based on Java, you need Go or Python or Scala as dependencies.  I wanted to build a simple one that would allow the basic Unix style operators to work with Zookeeper.

The original version of this was based on the Solr client, and then I ported it to Curator.

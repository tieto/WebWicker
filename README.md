*NOTE: This project is still in prototype stage, the functionality described below is not yet available*

#WebWicker CI web framework

WebWicker is an easy-to-use, plugin-based web framework for visualizing data from Continuous Integration systems. It is based on existing Open Source components like Apache Wicket, D3, Neo4J and PF4J.

##Current status

WebWicker is still in an early development stage. This repository currently contains a simple proof-of-concept implementation to show the concept of WebWicker together with Ericsson's CI/CD framework [Eiffel](https://github.com/Ericsson/eiffel).

##Vision

The vision of WebWicker is to allow developers to easy create their own custom visualizations as plugins that may easily be shared and reused by others. To allow flexibility, the framework will have extension points for plugins on multiple levels:

* **Webpage plugin**<br/>These plugins are the actual visualization, uses data providers to retrieve the data to visualize.
* **Provider plugin**<br/>Data providers for the web pages that may read data from either the WebWicker persistence layer or an external data source.
* **Source plugin**<br/>A source plugin gives WebWicker opportunity to continuously fetch/receive data from an external data source and store this data in a custom format in the WebWicker persistence layer.
* **Persistence layer plugin**<br/>Allows the user to replace the persistence layer used for storing data internally in WebWicker.
* **Model plugin**<br/>Internal data representation objects.

It will also be possible to define additional extensions points in plugins, allowing a plugin to have its own plugins as well. 

##Components

WebWicker consists of a set of Open Source components matched together to provide an easy extendable and maintainable solution for visualization needs. The components used are:

* [Apache Wicket](https://wicket.apache.org/) - Web application framework
* [PF4J](https://github.com/decebals/pf4j) - Plugin framework
* [Neo4J](https://neo4j.com/) - Graph-based database
* [D3](https://d3js.org/) - JavaScript library for visualization
* [Jetty](http://www.eclipse.org/jetty/) - Embedded web server 
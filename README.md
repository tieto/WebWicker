#WebWicker CI web framework

WebWicker is an easy-to-use, plugin-based web framework for visualizing data from Continuous Integration systems. It is based on existing Open Source components like Apache Wicket, D3, Neo4J and PF4J.

##Current status

WebWicker is still in an early development stage. A first version with a working plugin concept is available for simple testing. The persistence layer is still only an "in memory" implementation, so data will not remain when WebWicker is taken down.  

##Vision

The vision of WebWicker is to allow developers to easy create their own custom visualizations as plugins that may easily be shared and reused by others. To allow flexibility, the framework will have extension points for plugins on multiple levels:

* **Webpage plugin**<br/>These plugins are the actual visualization, uses data providers to retrieve the data to visualize.
* **Provider plugin**<br/>Data providers for the web pages that may read data from either the WebWicker persistence layer or an external data source.
* **Source plugin**<br/>A source plugin gives WebWicker opportunity to continuously fetch/receive data from an external data source and store this data in a custom format in the WebWicker persistence layer.
* **Persistence layer plugin**<br/>Allows the user to replace the persistence layer used for storing data internally in WebWicker.
* **Model plugin**<br/>Internal data representation objects.

It will also be possible to define additional extensions points in plugins, allowing a plugin to have its own plugins as well. 

##Project structure

The current repository contains three different maven projects:

* **WebWickerCore**
* **WebWickerApi**  
* **WebWickerEiffel**

###WebWickerCore

This is the WebWicker application. It contains the plugin manager, configuration handling and some default implementations. It is built as a WAR file, which could be installed in any web server supporting WAR extensions. In the future, this will be built with an embedded web server to make the WAR file executable without having to install a separate web server.

###WebWickerAPI

This is the API for WebWicker. It contains all the defined extension points, interfaces and help classes needed for extending WebWicker. All extension points are designed to follow the factory design pattern. The following extension points are available in the API:

* **WebWickerPage/Factory**</br>This is used for adding new web pages. The factory class defines some additional information on the page type, telling WebWicker how it should be handled when the plugin is loaded.
* **Source/Factory**</br>This is the extension points for adding data sources to WebWicker. A source extension will run in a separate thread, allowing it to continuously monitor and fetch/receive data from an external data source.   
* **Provider/Factory** - *This extension point is not yet activated!*</br>A provider is an interface between the persistence layer (or possibly an external data source) and the web pages. Providers are used to query for data and format it for visualization.
* **PersistenceLayer/Factory** - *This extension point is not yet activated!*</br>This extension point allows users to replace the default persistence layer in WebWicker.

There are also some interfaces that are used within plugins:

* **Configuration**<br/>All factories will receive a Configuration instance in their create method. This is used for accessing the configuration and settings for WebWicker.
* **Model**<br/>All data models that are used within WebWicker must implement this interface to make sure data can be converted to/from JSON format, which is currently used when storing data in the persistence layer.

When using the API in plugin development, add the WebWickerApi project as a *provided* dependency. The WebWicker core application will include the API and provide it to all plugins at runtime.

###WebWickerEiffel

This is a proof of concept implementation of a plugin for WebWicker to be used together with Ericsson's CI/CD framework [Eiffel](https://github.com/Ericsson/eiffel). This plugin provides the following extensions:

* A RabbitMQ message bus source
* Web pages for viewing commits and their status
* Model classes for representing commits and their patch sets

###To be added...

Some more projects will be added further along in development:

* **WebWickerPluginArchetype**<br/>To make it easier for users to develop plugins, a maven template project will be added. This will make it possible for users to set up a skeleton project for developing plugins with dependencies, build commands and project structure in place.

##Components

WebWicker consists of a set of Open Source components matched together to provide an easy extendable and maintainable solution for visualization needs. The components used are:

* [Apache Wicket](https://wicket.apache.org/) - Web application framework
* [PF4J](https://github.com/decebals/pf4j) - Plugin framework
* [Neo4J](https://neo4j.com/) - Graph-based database
* [D3](https://d3js.org/) - JavaScript library for visualization
* [Jetty](http://www.eclipse.org/jetty/) - Embedded web server

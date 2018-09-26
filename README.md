# OCSChat
## Short Description
OCSChat is a basic and small-scale chat app built upon MVVM architecture pattern and uses fire-base services as its backend. 
The app was intended to be a final project for summer training at Over coffee solutions(OCS) company so that I named it OCSChat.
The main advantage of that app is its nonfunctional requirements(such as maintainability, scalability, readability, etc) since 
the main purpose of the project was to build a scalable and maintainable app upon MVVM architecture pattern. The main 
disadvantage of that app is its lack of important functionality(till now) like notifying users when messages come.

## Requirements 
Here I have listed the requirements of the app both the successfully accomplished and the postponed ones all with their 
priority level and accomplishment status. It is important to prioritize the product requirements to avoid consuming much time 
in optional requirements which may leads to delay in the development process. It’s very important to start by building the 
mandatory requirements proceeding to nice_to_have_ones and finally optional ones if there is time.
[requirements.pdf](https://github.com/hossamnasser938/OCSChat/files/2420293/requirements.pdf)

## Initial Mockup
This mockup is not the final appearance of the app but it is an initial visual description for what the app should do so it is 
normal to lack something of the built-in functionality of the app now. I always make such a fast and initial mockup for the app 
before I start. That helps visualizing the final goal and also helps estimating the time required and managing the development 
process.
![app mockup](https://user-images.githubusercontent.com/27894818/46087628-08712000-c1ab-11e8-8fd8-02bcf254e51b.jpeg)

## Tools Used 
In the app I used tools intended to help building a scalable and maintainable app. Usually these tools are used in large-scale 
projects.
    
    • Dagger
Dagger is a library built to solve the problem of dependency. Dependency is a problem appears when classes depend on other 
classes. To visualize the problem I will give a trivial example. Let’s say we have a ‘School’ class. In order to instantiate an 
object of that class you have to instantiate a 'Teacher' object, a 'ClassRoom' object, and a bunch of other objects. Also in 
order to instantiate an object of 'Teacher' class you have to instantiate a bunch of objects and so on. So in order to 
instantiate an object of a specific class you have to instantiate a series of objects. That example was for clarifying purpose 
only but usually this problem appears in layered architecture when every layer depends on the previous layer. Dagger solves this 
problem by what’s called dependency injection in which we provide a class and anywhere we need it dagger will provide it to us 
and we do not have to instantiate an object of that class. Dagger also provides a way to provide a single object of a specific 
class all over the app by using @Singleton annotation besdes @Provides annotation in which we use to say that this class will 
be provided. Also we use @Inject annotation to declare that an object of a specific class needed to be injected here.
    
    • RxJava 
RxJava is the java implementation of Reactive programming which is a design pattern used to solve many problems. This pattern 
can be described as Observing pattern in which an Observable is providing needed data(usually that process is done on a 
background thread) and an Observer consumes this data(usually on main thread). Data can be modified before it is consumed 
through a bunch of Operators available in RxJava.
    
    • Fire-base
Fire-base is used as the backend of the app.
        
        ◦ Fire-base Authentication 
is used to manage signing and registering users.
        
        ◦ Fire-base Real-time database
is used to store more information about users that can not be stored in Fire-base authentication. It stores also messages 
between users.
        
        ◦ Fire-base Storage
is used to store large files related to users such as their profile photos. 

## App Architecture
The app contains of different layers. Some layers are spreaded on different packages.
    
    • View
is the layer that are responsible for interacting with the user whether to display or to take user inputs. It consists of two 
packages
        
        ◦ activity
contains app activities
        
        ◦ fragment
contains app fragments

    • Model
is the layer that are responsible for holding and maintaining app persistent data. It consists of four packages
        
        ◦ model
contains data models(POJO classes)
        
        ◦ api
manages pulling and pushing data from and into fire-base services
        
        ◦ localDatabase
manages pulling and pushing data from and into app local sqlite database
        
        ◦ repository
abstracts data pulling and pushing which means any one can use this layer to pull or push data without knowing whether this 
data should be pulled/pushed from/into api or local database

    • ViewModel
is the layer that sets between the view and model layers. It prevents direct access from view to model and also helps fulfilling 
separation of concerns concept. It consists only one package called viewModel

    •  There are other packages related to android development such as adapter, listener, and util
